package onitama;

public interface MoveRanker<G extends Game<M>, M> {
	public int rankMove(G game, M move);
}
