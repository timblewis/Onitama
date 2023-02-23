package onitama;

public interface GameListener<G extends Game<M>, M> {
	public void initializeGame(G game);
	public void finalizeGame(G game);
	public void nextMove(M move, G game);
}
