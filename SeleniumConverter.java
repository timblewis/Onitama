package onitama;

import onitama.OnitamaGame.Move;
import org.json.*;

public class SeleniumConverter {
	private static Player<OnitamaGame, OnitamaGame.Move> ai = new AlphaBetaAlgorithm<OnitamaGame, Move, Double>(
			new OnitamaStateEvaluator(),
			new RankedMoveSorter<OnitamaGame, Move>(new OnitamaMoveRanker()),
			new OnitamaDepthAdjuster(),
			8);
	
	public static void main(String[] args) {
		String state = args[0];
		//String state = "{\"board\": [[0, 1, 2, 1, 1], [1, 0, 0, 0, 0], [0, 0, 0, 0, 0], [0, -1, -1, 0, 0], [-1, 0, -2, 0, -1]], \"cards\": {\"opponent_cards\": [\"Boar\", \"Elephant\"], \"player_cards\": [\"Monkey\", \"Crab\"], \"middle_card\": \"Dragon\"}}";
		OnitamaGame game = parseState(state);
		OnitamaGame.Move aiMove = ai.makeMove(game);
		printMove(aiMove);
	}
	
	public static OnitamaGame parseState(String state) {
		JSONObject obj = new JSONObject(state);
		OnitamaGame game = new OnitamaGame();
		JSONArray board = obj.getJSONArray("board");
		for (int x=0; x<5; x++) {
			for (int y=0; y<5; y++) {
				game.getCurrentState().board[y][x] = board.getJSONArray(x).getInt(y);
			}
		}
		obj = obj.getJSONObject("cards");
		game.getCurrentState().cards[0] = OnitamaGame.Card.valueOf(obj.getJSONArray("player_cards").getString(0).toUpperCase());
		game.getCurrentState().cards[1] = OnitamaGame.Card.valueOf(obj.getJSONArray("player_cards").getString(1).toUpperCase());
		game.getCurrentState().cards[2] = OnitamaGame.Card.valueOf(obj.getString("middle_card").toUpperCase());
		game.getCurrentState().cards[3] = OnitamaGame.Card.valueOf(obj.getJSONArray("opponent_cards").getString(0).toUpperCase());
		game.getCurrentState().cards[4] = OnitamaGame.Card.valueOf(obj.getJSONArray("opponent_cards").getString(1).toUpperCase());
		return game;
	}
	
	public static void printMove(OnitamaGame.Move move) {
		JSONObject obj = new JSONObject();
		obj.accumulate("card", move.cardUsed.name());
		obj.accumulate("start_square", new int[]{move.startingSquare.x, move.startingSquare.y});
		obj.accumulate("end_square", new int[]{move.endingSquare.x, move.endingSquare.y});
		System.out.println(obj.toString());
	}
}
