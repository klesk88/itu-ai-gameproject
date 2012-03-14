package connectFour;

public class GameLogic implements IGameLogic {
	/** The number of rows of the board. */
	private int x = 0;
	/** The number of columns of the board. */
	private int y = 0;
	/** The id of the player who is using this GameLogic. */
	private int playerID;
	/** The board which contains the state of the game. */
	private int[][] gameBoard;
	/** This contains the last x coordinate where a coins where put. */
	private int lastX;
	/** This contains the last y coordinate where a coins where put. */
	private int lastY;
	/** The minimax implementation. */
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
		} else if ((result = ConnectedChecking.checkCrossPositionsDown(
				this.gameBoard, this.lastX, this.lastY, 4, this.x, this.y)) != Winner.NOT_FINISHED) {
		} else if ((result = ConnectedChecking.checkCrossPositionsUp(
				this.gameBoard, this.lastX, this.lastY, 4, this.x, this.y)) != Winner.NOT_FINISHED) {
		} else if ((result = ConnectedChecking.checkTie(this.gameBoard, this.x,
				this.y)) == Winner.TIE) {
		}
		return result;
	}

	public void insertCoin(int column, int playerID) {
		for (int i = 0; i < this.y; i++) {
			// If the position is free it stores the new coin and finishes the
			// method
			if (this.gameBoard[i][column] == Integer.MIN_VALUE) {
				this.gameBoard[i][column] = playerID;
				this.lastX = i;
				this.lastY = column;
				break;
			}
		}
	}

	public int decideNextMove() {
		return mini_max.miniMax(gameBoard);
	}
}
