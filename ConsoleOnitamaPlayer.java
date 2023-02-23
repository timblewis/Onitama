package onitama;

import java.util.Scanner;
import onitama.OnitamaGame.Card;
import onitama.OnitamaGame.Move;
import onitama.OnitamaGame.Point;

public class ConsoleOnitamaPlayer implements Player<OnitamaGame, Move> {
	private static final Scanner reader = new Scanner(System.in);

	@Override
	public Move makeMove(OnitamaGame game) {
		boolean done = false;
		Move m = null;
		while (!done) {
			System.out.print(">");
			String s = reader.nextLine();
			m = parseMove(s);
			done = game.isLegalMove(m);
			if (!done) {
				System.out.println("Illegal move please type a legal move");
			}
		}
		return m;
	}

	private Move parseMove(String s) {
		String[] parts = s.split(" ");
		if (parts.length != 3) {
			return null;
		}
		String startingPointString = parts[1].trim().toLowerCase();
		String endingPointString = parts[2].trim().toLowerCase();
		Card card = Card.valueOf(parts[0].trim().toUpperCase());
		Point startingPoint = new Point(startingPointString.charAt(0)-'a', startingPointString.charAt(1)-'1');
		Point endingPoint = new Point(endingPointString.charAt(0)-'a', endingPointString.charAt(1)-'1');
		return new Move(card, startingPoint, endingPoint);
	}
}
