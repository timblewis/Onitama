package onitama;

public class OnitamaDepthAdjuster implements DepthAdjuster<OnitamaGame, OnitamaGame.Move> {

	@Override
	public int adjustedDepth(int depth, OnitamaGame game) {
		int p1Pieces = 0;
		int p2Pieces = 0;
		for(int x=0; x<5; x++) {
			for (int y=0; y<5; y++) {
				switch ((int)Math.signum(game.getCurrentState().board[x][y])) {
				case 1:
					p1Pieces++;
					break;
				case -1:
					p2Pieces++;
					break;
				}
			}
		}
		return (int) Math.round(2*depth*Math.log(30)/Math.log(36*p1Pieces*p2Pieces));
	}
	
}
