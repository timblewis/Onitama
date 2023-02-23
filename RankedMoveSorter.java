package onitama;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RankedMoveSorter<G extends Game<M>, M> implements MoveSorter<G, M> {
	private MoveRanker<G,M> moveRanker;
	
	public RankedMoveSorter(MoveRanker<G, M> moveRanker) {
		this.moveRanker = moveRanker;
	}

	@Override
	public List<M> sort(G game, List<M> moves) {
		Map<M, Integer> moveRanks = moves.stream().collect(Collectors.toMap(Function.identity(), move -> moveRanker.rankMove(game, move)));
		moves.sort(Comparator.comparing(moveRanks::get));
		return moves;
	}
	
}
