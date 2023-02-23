package onitama;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class OnitamaGame implements Game<OnitamaGame.Move> {

	public static void main(String[] args) {
		for (Card card: Card.values()) {
			printCard(card);
			System.out.println();
		}
	}
	
	private State state;
	public static final Map<Card, List<Point>> CARD_MOVES = new HashMap<>() {{
		put(Card.TIGER, Arrays.asList(new Point(0,2), new Point(0,-1)));
		put(Card.DRAGON, Arrays.asList(new Point(2,1), new Point(-2,1), new Point(1,-1), new Point(-1,-1)));
		put(Card.FROG, Arrays.asList(new Point(-2,0), new Point(-1,1), new Point(1,-1)));
		put(Card.RABBIT, Arrays.asList(new Point(2,0), new Point(1,1), new Point(-1,-1)));
		put(Card.CRAB, Arrays.asList(new Point(2,0), new Point(-2,0), new Point(0,1)));
		put(Card.ELEPHANT, Arrays.asList(new Point(-1,1), new Point(-1,0), new Point(1,1), new Point(1,0)));
		put(Card.GOOSE, Arrays.asList(new Point(-1,1), new Point(-1,0), new Point(1,0), new Point(1,-1)));
		put(Card.ROOSTER, Arrays.asList(new Point(-1,0), new Point(-1,-1), new Point(1,0), new Point(1,1)));
		put(Card.MONKEY, Arrays.asList(new Point(1,1), new Point(1,-1), new Point(-1,1), new Point(-1,-1)));
		put(Card.MANTIS, Arrays.asList(new Point(1,1), new Point(-1,1), new Point(0,-1)));
		put(Card.HORSE, Arrays.asList(new Point(-1,0), new Point(0,1), new Point(0,-1)));
		put(Card.OX, Arrays.asList(new Point(1,0), new Point(0,1), new Point(0,-1)));
		put(Card.CRANE, Arrays.asList(new Point(0,1), new Point(-1,-1), new Point(1,-1)));
		put(Card.BOAR, Arrays.asList(new Point(1,0), new Point(-1,0), new Point(0,1)));
		put(Card.EEL, Arrays.asList(new Point(-1,1), new Point(-1,-1), new Point(1,0)));
		put(Card.COBRA, Arrays.asList(new Point(-1,0), new Point(1,1), new Point(1,-1)));
	}};
	
	public OnitamaGame() {
		state = new State();
	}
	
	public OnitamaGame(Card[] cards) {
		state = new State(cards);
	}
	
	public OnitamaGame(State state) {
		this.state = new State(state);
	}
	
	public State getCurrentState() {
		return state;
	}

	public List<Move> getLegalMoves() {
		List<Move> result = new ArrayList<Move>();
		int cardStartIndex = state.currentPlayer == 1 ? 0: 3;
		for (int x=0; x<5; x++) {
			for (int y=0; y<5; y++) {
				if (state.board[x][y]*state.currentPlayer > 0) {
					for (int i = cardStartIndex; i < cardStartIndex+2; i++) {
						for (Point change : CARD_MOVES.get(state.cards[i])) {
							Point startingPoint = new Point(x,y);
							Point endingPoint = startingPoint.plus(change.mult(state.currentPlayer));
							if (endingPoint.isOnBoard() && state.board[endingPoint.x][endingPoint.y]*state.currentPlayer <= 0) {
								result.add(new Move(state.cards[i], startingPoint, endingPoint));
							}
						}
					}
				}
			}
		}
		return result;
	}

	public boolean isLegalMove(Move move) {
		return isLegalMove(state, move);
	}
	
	public static boolean isLegalMove(State state, Move move) {
		if (move == null || move.cardUsed == null || move.startingSquare == null || move.endingSquare == null) {
			return false;
		}
		int index = Arrays.asList(state.cards).indexOf(move.cardUsed);
		// if it is P1's turn make sure they have the card
		if (state.currentPlayer == 1 && index != 0 && index != 1) {
			return false;
		}
		// if it is P2's turn make sure they have the card
		if (state.currentPlayer == -1 && index != 3 && index != 4) {
			return false;
		}
		// make sure the squares are on the board
		if (!move.endingSquare.isOnBoard() || !move.startingSquare.isOnBoard()) {
			return false;
		}
		// make sure there is a current player's piece on the starting square
		if (state.board[move.startingSquare.x][move.startingSquare.y]*state.currentPlayer <= 0) {
			return false;
		}
		// make sure there isn't a current player's piece on the ending square
		if (state.board[move.endingSquare.x][move.endingSquare.y]*state.currentPlayer > 0) {
			return false;
		}
		// make sure the move is playable with the card
		return CARD_MOVES.get(move.cardUsed).contains(move.endingSquare.subtract(move.startingSquare).mult(state.currentPlayer));
	}

	public Result makeMove(Move move) {
		if (!isLegalMove(move)) {
			System.out.println("Move " + move);
			OnitamaGamePrinter.printState(state);
			throw new IllegalStateException("Illegal move");
		}
		// if you capture a king you win
		if (state.board[move.endingSquare.x][move.endingSquare.y] == 2) {
			state.result = Result.PLAYER_TWO_WIN;
		} else if (state.board[move.endingSquare.x][move.endingSquare.y] == -2) {
			state.result = Result.PLAYER_ONE_WIN;
		}
		// move the piece
		state.board[move.endingSquare.x][move.endingSquare.y] = state.board[move.startingSquare.x][move.startingSquare.y];
		state.board[move.startingSquare.x][move.startingSquare.y] = 0;
		// if your king gets to the opponent's kings starting square you win
		if (state.board[2][4] == 2) {
			state.result = Result.PLAYER_ONE_WIN;
		}
		if (state.board[2][0] == -2) {
			state.result = Result.PLAYER_TWO_WIN;
		}
		// swap the played card
		int index = Arrays.asList(state.cards).indexOf(move.cardUsed);
		Card temp = state.cards[index];
		state.cards[index] = state.cards[2];
		state.cards[2] = temp;
		// swap the current player
		state.currentPlayer *= -1;
		return state.result;
	}

	@Override
	public Player getCurrentPlayer() {
		if (state.currentPlayer == 1) {
			return Player.PLAYER_ONE;
		} else if (state.currentPlayer == -1) {
			return Player.PLAYER_TWO;
		}
		throw new IllegalStateException("Current player is incorrect: " + state.currentPlayer);
	}

	@Override
	public Result getCurrentResult() {
		return state.result;
	}
	
	@Override
	public Object clone() {
		return new OnitamaGame(state);
	}
	
	public static final class State {
		public Result result = Result.UNFINISHED;
		// 1 for player 1, -1 for player 2
		public int currentPlayer = 1;
		// 0 for empty, 1 for P1 pawn, 2 for P1 king, -1 for P2 pawn, -2 for P2 king
		public int[][] board = new int[5][5];
		// player 1 has cards 0 & 1, player 2 has cards 3 & 4, card 2 is in the middle
		public Card[] cards;

		// starting position with specified cards
		public State(Card[] cards) {
			board[0][0] = board [1][0] = board[3][0] = board[4][0] = 1;
			board[2][0] = 2;
			board[0][4] = board [1][4] = board[3][4] = board[4][4] = -1;
			board[2][4] = -2;
			this.cards = Arrays.copyOf(cards, 5);
		}
		
		// starting position with random cards
		public State() {
			this(ThreadLocalRandom.current().ints(0, Card.values().length).distinct().limit(5).mapToObj(i -> Card.values()[i]).toArray(Card[]::new));
		}
		
		public State(State toCopy) {
			result = toCopy.result;
			currentPlayer = toCopy.currentPlayer;
			for (int i=0; i<5; i++) {
				for (int j=0; j<5; j++) {
					board[i][j] = toCopy.board[i][j];
				}
			}
			cards = Arrays.copyOf(toCopy.cards, 5);
		}
	}
	
	public static enum Card {
		TIGER,
		DRAGON,
		FROG,
		RABBIT,
		CRAB,
		ELEPHANT,
		GOOSE,
		ROOSTER,
		MONKEY,
		MANTIS,
		HORSE,
		OX,
		CRANE,
		BOAR,
		EEL,
		COBRA
	}
	
	public static void printCard(Card card) {
		System.out.println(card);
		List<Point> moves = CARD_MOVES.get(card);
		for (int y=2; y>=-2; y--) {
			for (int x=-2; x<=2; x++) {
				if (moves.contains(new Point(x,y))) {
					System.out.print('X');
				} else if (x==0 && y==0){
					System.out.print('P');
				}
				else {
					System.out.print('O');
				}
			}
			System.out.println();
		}
	}
	
	public static final class Move {
		public final Card cardUsed;
		public final Point startingSquare;
		public final Point endingSquare;
		
		public Move(Card cardUsed, Point startingSquare, Point endingSquare) {
			this.cardUsed = cardUsed;
			this.startingSquare = startingSquare;
			this.endingSquare = endingSquare;
		}
		
		public String toString() {
			return cardUsed.name() + " " + startingSquare + " " + endingSquare;
		}
	}
	
	public static final class Point {
		Point(int x, int y) {
			this.x  = x;
			this.y = y;
		}
		public final int x;
		public final int y;
		
		public boolean isOnBoard() {
			return x < 5 && x >= 0 && y < 5 && y >= 0;
		}
		
		public Point plus(Point p) {
			return new Point(x + p.x, y + p.y);
		}
		
		public Point mult(int c) {
			return new Point(c*x, c*y);
		}
		
		public Point subtract(Point p) {
			return new Point(x-p.x, y-p.y);
		}
		
		@Override
	    public boolean equals(Object o) {
			if (!(o instanceof Point)) {
				return false;
			} else {
				Point p = (Point) o;
				return p.x == x && p.y == y;
			}
		}
		
		@Override
		public String toString() {
			return Character.toString((char)('a' + x)) + (char)('1' + y);
		}
	}
}
