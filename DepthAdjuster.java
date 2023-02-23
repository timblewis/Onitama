package onitama;

public interface DepthAdjuster<G extends Game<M>, M>{
	public int adjustedDepth(int depth, G game);
}
