package onitama;

public class OnitamaMoveRanker implements MoveRanker<OnitamaGame, OnitamaGame.Move>{

	@Override
	public int rankMove(OnitamaGame game, OnitamaGame.Move move) {
		return game.getCurrentState().board[move.endingSquare.x][move.endingSquare.y] * game.getCurrentState().currentPlayer < 0 ? 0: 1;
	}

}
