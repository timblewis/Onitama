package onitama;

import java.util.List;

public interface MoveSorter<G extends Game<M>, M> {
	public List<M> sort(G game, List<M> moves);
}