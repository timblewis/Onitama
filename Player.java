package onitama;

public interface Player<G extends Game<M>, M> {
	public M makeMove(G game);
}
