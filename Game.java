package onitama;

import java.util.List;

public interface Game <M> extends Cloneable {
	public List<M> getLegalMoves();
	public boolean isLegalMove(M move);
	public Result makeMove(M move);
	public Player getCurrentPlayer();
	public Result getCurrentResult();
	public Object clone();
	
	public enum Result {
		UNFINISHED,
		PLAYER_ONE_WIN,
		PLAYER_TWO_WIN,
		DRAW
	}
	
	public enum Player {
		PLAYER_ONE,
		PLAYER_TWO
	}
}
