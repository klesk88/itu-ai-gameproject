package connectFour;

public class GameLogic implements IGameLogic {
	private int x = 0;
	private int y = 0;
	private int playerID;
	private int[][] gameBoard;
	/** This contains the last x coordinate where a coins where put. */
	private int lastX;
	/** This contains the last y coordinate where a coins where put. */
	private int lastY;
	private MiniMax mini_max;

	public GameLogic() {
		// TODO Write your implementation for this method
	}

	public void initializeGame(int x, int y, int playerID) {
		// Rows
		this.x = y;
		// Columns
		this.y = x;
		this.playerID = playerID;
		mini_max = new MiniMax(this.x, this.y, playerID);
		this.lastX = Integer.MIN_VALUE;
		this.lastY = Integer.MIN_VALUE;
		this.gameBoard = new int[this.x][this.y];
		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
				this.gameBoard[i][j] = Integer.MIN_VALUE;
			}
		}
	}

	public Winner gameFinished() {
		Winner result = Winner.NOT_FINISHED;
		if ((result = ConnectedChecking.checkVerticalPositions(this.gameBoard,
				this.lastX, this.lastY, 4)) != Winner.NOT_FINISHED) {
		} else if ((result = ConnectedChecking.checkHorizontalPositions(
				this.gameBoard, this.lastX, this.lastY, 4, this.y)) != Winner.NOT_FINISHED) {
		} else if ((result = ConnectedChecking.checkCrossPositions(
				this.gameBoard, this.lastX, this.lastY, 4, this.x, this.y)) != Winner.NOT_FINISHED) {
		} else if ((result = ConnectedChecking.checkTie(this.gameBoard, this.x,
				this.y)) == Winner.TIE) {
		}
		return result;
	}

	public void insertCoin(int column, int playerID) {
		for (int i = 0; i < this.y; i++) {
			// If the position is already busy continue to the next
			if (this.gameBoard[i][column] != Integer.MIN_VALUE) {
				continue;
			}
			// If the position is free it stores the new coin and finishes the
			// method
			this.gameBoard[i][column] = playerID;
			this.lastX = i;
			this.lastY = column;
			break;
		}
	}

	public int decideNextMove() {

		/*
		 * for (int i = 0; i < this.x; i++) { for (int j = 0; j < this.y; j++) {
		 * // If the position is already occupated continue to the next if
		 * (this.gameBoard[i][j] != Integer.MIN_VALUE) { continue; } return i; }
		 * } return 0;
		 */
		return mini_max.miniMax(gameBoard);

	}
}
