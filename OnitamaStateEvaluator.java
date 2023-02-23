package onitama;

import onitama.OnitamaGame.Card;
import onitama.OnitamaGame.Move;

public class OnitamaStateEvaluator implements StateEvaluator<OnitamaGame, OnitamaGame.Move, Double> {
	
	public static void main(String[] args) {
		Player<OnitamaGame, OnitamaGame.Move> ai = new AlphaBetaAlgorithm<OnitamaGame, Move, Double>(
				new OnitamaStateEvaluator(),
				new RankedMoveSorter<OnitamaGame, Move>(new OnitamaMoveRanker()),
				new OnitamaDepthAdjuster(),
				8);
		Player<OnitamaGame, OnitamaGame.Move> opponent = new ConsoleOnitamaPlayer();
		OnitamaGame game = new OnitamaGame(new OnitamaGame.Card[]{
				OnitamaGame.Card.COBRA, // P1
				OnitamaGame.Card.MONKEY, // P1
				OnitamaGame.Card.FROG, // Middle
				OnitamaGame.Card.DRAGON, // P2
				OnitamaGame.Card.HORSE}); // P2
		@SuppressWarnings("unchecked")
		GameController<OnitamaGame, Move> gameController = new GameController<OnitamaGame, Move>(
				game,
				ai, // P1
				opponent, // P2
				new OnitamaGamePrinter());
		gameController.playGame();
	}

	@Override
	public Double evaluateState(OnitamaGame game) {
		switch (game.getCurrentResult()) {
			case PLAYER_ONE_WIN:
				return Double.POSITIVE_INFINITY;
			case PLAYER_TWO_WIN:
				return Double.NEGATIVE_INFINITY;
			case DRAW:
				return 0.0;
		}
		double score = 0.0;
		for (int x=0; x<5; x++) {
			for (int y=0; y<5; y++) {
				score += Math.signum(game.getCurrentState().board[x][y]) * (1 - 0.25*(Math.sqrt(((x-2)*(x-2) + (y-2)*(y-2))/8.0)));
			}
		}
		return score;
	}
}
