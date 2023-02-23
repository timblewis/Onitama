package onitama;

import java.util.List;

import onitama.Game.Result;

public class AlphaBetaAlgorithm<G extends Game<M>, M, E extends Comparable<E>> implements Player<G, M> {
	private StateEvaluator<G, M, E> evaluator;
	private MoveSorter<G, M> moveSorter;
	private DepthAdjuster<G, M> depthAdjuster;
	private int depth;
	private int adjustedDepth;
	
	private int internalNodes;
	private int leafNodes;
	private int totalMoves;
	private int totalMovesNotPruned;

	public AlphaBetaAlgorithm(StateEvaluator<G, M, E> evaluator, int depth) {
		this.evaluator = evaluator;
		this.depth = depth;
	}
	
	public AlphaBetaAlgorithm(StateEvaluator<G, M, E> evaluator, MoveSorter<G, M> moveSorter, DepthAdjuster<G, M> depthAdjuster, int depth) {
		this.evaluator = evaluator;
		this.moveSorter = moveSorter;
		this.depthAdjuster = depthAdjuster;
		this.depth = depth;
	}
	
	@Override
	public M makeMove(G game) {
		internalNodes = 0;
		leafNodes = 0;
		totalMoves = 0;
		totalMovesNotPruned = 0;
		adjustedDepth = depthAdjuster == null ? depth : depthAdjuster.adjustedDepth(depth, game);
//		long startTime = System.currentTimeMillis();
		MoveEval moveEval = recursiveFindBest(game, adjustedDepth, null);
//		long endTime = System.currentTimeMillis();
//		System.out.println("Elapsed Time: " + (endTime-startTime) + " ms");
//		System.out.println("Depth: " + adjustedDepth);
//		System.out.println("Eval: " + moveEval.eval);
//		System.out.println("Internal Nodes: " + internalNodes);
//		System.out.println("Leaf Nodes: " + leafNodes);
//		System.out.println("Average Moves Per Position: " + (totalMoves/(double)internalNodes));
//		System.out.println("Average Moves Pruned: " + ((totalMoves - totalMovesNotPruned)/(double)internalNodes));
		return moveEval.move;
	}
	
	private MoveEval recursiveFindBest(G game, int depth, WrappedEval alpha) {
		if (depth <= 0 || game.getCurrentResult() != Game.Result.UNFINISHED) {
			leafNodes++;
			if(game.getCurrentResult() == Result.PLAYER_ONE_WIN || game.getCurrentResult() == Result.PLAYER_TWO_WIN) {
				return new MoveEval(null, new WrappedEval(game.getCurrentResult()));
			} else {
				return new MoveEval(null, new WrappedEval(evaluator.evaluateState(game)));
			}
		} else {
			internalNodes++;
			MoveEval bestMoveEval = null;
			WrappedEval bestEval = null;
			List<M> moves = game.getLegalMoves();
			totalMoves += moves.size();
			if (moveSorter != null) {
				moves = moveSorter.sort(game, moves);
			}
			for (M move : moves) {
				totalMovesNotPruned++;
				@SuppressWarnings("unchecked")
				G clone = (G) game.clone();
				clone.makeMove(move);
				MoveEval moveEval = recursiveFindBest(clone, depth-1, bestEval);
				moveEval.eval.incrementTurn();
//				if (depth == this.adjustedDepth) {
//					System.out.println("Move: " + move + " Score: " + moveEval.eval);
//				}
				if (bestMoveEval == null
						|| (game.getCurrentPlayer() == Game.Player.PLAYER_ONE && bestMoveEval.eval.compareTo(moveEval.eval) < 0)
						|| (game.getCurrentPlayer() == Game.Player.PLAYER_TWO && bestMoveEval.eval.compareTo(moveEval.eval) > 0)) {
					bestMoveEval = new MoveEval(move, moveEval.eval);
					bestEval = moveEval.eval;
				}
				if (alpha != null && 
						((game.getCurrentPlayer() == Game.Player.PLAYER_ONE && bestMoveEval.eval.compareTo(alpha) >= 0)
								|| (game.getCurrentPlayer() == Game.Player.PLAYER_TWO && bestMoveEval.eval.compareTo(alpha) <= 0))) {
					break;
				}
			}
			return bestMoveEval;
		}
	}
	
	private class MoveEval {
		public M move;
		public WrappedEval eval;
		public MoveEval(M move, WrappedEval eval) {
			this.move = move;
			this.eval = eval;
		}
	}
	
	private class WrappedEval implements Comparable<WrappedEval> {
		private E eval;
		private int turnsUntilResult;
		private Game.Result result;
		
		public WrappedEval(E eval) {
			this.eval = eval;
		}
		
		public WrappedEval(Result result) {
			this.result = result;
		}
		
		public void incrementTurn() {
			turnsUntilResult++;
		}

		@Override
		public int compareTo(AlphaBetaAlgorithm<G, M, E>.WrappedEval wrappedEval) {
			if (result == Result.PLAYER_ONE_WIN) {
				if (wrappedEval.result == Result.PLAYER_ONE_WIN) {
					return wrappedEval.turnsUntilResult - turnsUntilResult;
				} else {
					return 1;
				}
			} else if (result == Result.PLAYER_TWO_WIN) {
				if (wrappedEval.result == Result.PLAYER_TWO_WIN) {
					return turnsUntilResult - wrappedEval.turnsUntilResult;
				} else {
					return -1;
				}
			} else {
				if (wrappedEval.result == Result.PLAYER_ONE_WIN) {
					return -1;
				} else if (wrappedEval.result == Result.PLAYER_TWO_WIN) {
					return 1;
				} else {
					return eval.compareTo(wrappedEval.eval);
				}
			}
		}
		
		public String toString() {
			if (eval != null) {
				return eval.toString();
			} else if (result == Result.PLAYER_ONE_WIN) {
				return "#" + (turnsUntilResult+1)/2;
			} else if (result == Result.PLAYER_TWO_WIN) {
				return "#-" + (turnsUntilResult+1)/2;
			}
			return "ERROR";
		}
	}
}
