package onitama;

import onitama.Game.Result;

public class GameController<G extends Game<M>, M> {
	G game;
	Player<G, M> player1;
	Player<G, M> player2;
	GameListener<G, M>[] listeners;
	
	public GameController(G game, Player<G, M> player1, Player<G, M> player2, GameListener<G, M> ... listeners) {
		this.game = game;
		this.player1 = player1;
		this.player2 = player2;
		this.listeners = listeners;
	}
	
	public Game.Result playGame() {
		for (GameListener<G, M> listener : listeners) {
			listener.initializeGame(game);
		}
		while (game.getCurrentResult() == Result.UNFINISHED) {
			M move;
			if (game.getCurrentPlayer() == Game.Player.PLAYER_ONE) {
				move = player1.makeMove(game);
				game.makeMove(move);
			} else {
				move = player2.makeMove(game);
				game.makeMove(move);
			}
			for (GameListener<G, M> listener : listeners) {
				listener.nextMove(move, game);
			}
		}
		for (GameListener<G, M> listener : listeners) {
			listener.finalizeGame(game);
		}
		return game.getCurrentResult();
	}
}
