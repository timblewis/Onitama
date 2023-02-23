package onitama;

public interface StateEvaluator<G extends Game<M>, M, E extends Comparable<E>> {
	public E evaluateState(G game);
}
