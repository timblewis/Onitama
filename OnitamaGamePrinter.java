package onitama;

import java.util.List;

import onitama.OnitamaGame.Move;
import onitama.OnitamaGame.Point;
import onitama.OnitamaGame.State;

public class OnitamaGamePrinter implements GameListener<OnitamaGame, Move>{
	
	public static void main(String[] args) {
		ConsoleOnitamaPlayer player = new ConsoleOnitamaPlayer();
		GameController<OnitamaGame, Move> gameController = new GameController<OnitamaGame, Move>(new OnitamaGame(), player, player, new OnitamaGamePrinter());
		gameController.playGame();
	}

	@Override
	public void initializeGame(OnitamaGame game) {
		printState(game.getCurrentState());
	}

	public void finalizeGame(OnitamaGame game) {
		System.out.println("Game complete. Result: " + game.getCurrentState().result);
	}

	@Override
	public void nextMove(Move move, OnitamaGame game) {
		System.out.println("Previous move: " + move.cardUsed.name() + " " + move.startingSquare + " " + move.endingSquare);
		printState(game.getCurrentState());
	}
	
	public static void printState(State state) {
		System.out.println("Current player's turn: " + (state.currentPlayer == 1 ? "Player 1" : "Player 2"));
		System.out.println();
		System.out.println("Player 2 cards:");
		System.out.println(state.cards[3].name() + "   " + state.cards[4].name());
		for (int y=-2; y<=2; y++) {
			List<Point> moves1 = OnitamaGame.CARD_MOVES.get(state.cards[3]);
			List<Point> moves2 = OnitamaGame.CARD_MOVES.get(state.cards[4]);
			for (int x=2; x>=-2; x--) {
				if (moves1.contains(new Point(x,y))) {
					System.out.print('X');
				} else if (x==0 && y==0){
					System.out.print('P');
				}
				else {
					System.out.print('O');
				}
			}
			System.out.print("   ");
			for (int x=2; x>=-2; x--) {
				if (moves2.contains(new Point(x,y))) {
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
		System.out.println();
		System.out.println("  Board:  " + state.cards[2].name());
		for (int y=4; y>=0; y--) {
			int cardY = (y-2)*state.currentPlayer;
			System.out.print((char)('1' + y));
			System.out.print(' ');
			for (int x=0; x<5; x++) {
				switch (state.board[x][y]) {
					case -2:
						System.out.print('R');
						break;
					case -1:
						System.out.print('r');
						break;
					case 2:
						System.out.print('B');
						break;
					case 1:
						System.out.print('b');
						break;
					default:
						System.out.print('O');
				}
			}
			System.out.print("   ");
			for (int x=-2; x<=2; x++) {
				List<Point> moves = OnitamaGame.CARD_MOVES.get(state.cards[2]);
				if (moves.contains(new Point(x*state.currentPlayer,cardY))) {
					System.out.print('X');
				} else if (x==0 && cardY==0){
					System.out.print('P');
				}
				else {
					System.out.print('O');
				}
			}
			System.out.println();
		}
		System.out.println("  abcde");
		System.out.println();
		System.out.println("Player 1 cards:");
		System.out.println(state.cards[0].name() + "   " + state.cards[1].name());
		for (int y=2; y>=-2; y--) {
			List<Point> moves1 = OnitamaGame.CARD_MOVES.get(state.cards[0]);
			List<Point> moves2 = OnitamaGame.CARD_MOVES.get(state.cards[1]);
			for (int x=-2; x<=2; x++) {
				if (moves1.contains(new Point(x,y))) {
					System.out.print('X');
				} else if (x==0 && y==0){
					System.out.print('P');
				}
				else {
					System.out.print('O');
				}
			}
			System.out.print("   ");
			for (int x=-2; x<=2; x++) {
				if (moves2.contains(new Point(x,y))) {
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
}
