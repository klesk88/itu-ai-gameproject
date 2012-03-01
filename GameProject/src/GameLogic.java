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
		if ((result = this.checkVerticalPositions()) != Winner.NOT_FINISHED) {
		} else if ((result = this.checkHorizontalPositions()) != Winner.NOT_FINISHED) {
		} else if ((result = this.checkCrossPositions()) != Winner.NOT_FINISHED) {
		} else if ((result = this.checkTie()) == Winner.TIE) {
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

	/**
	 * Check if the game have finished with a tie.
	 * 
	 * @return Winner.NOT_FINISHED if the game hasn't finished or Winner.TIE if
	 *         the game has finished and none player has won.
	 */
	private Winner checkTie() {
		// Check the top position of every column
		for (int j = 0; j < this.y; j++) {
			if (this.gameBoard[this.x - 1][j] == Integer.MIN_VALUE) {
				return Winner.NOT_FINISHED;
			}
		}
		return Winner.TIE;
	}

	/**
	 * Check if there are four coins connected in vertical.
	 * 
	 * @return Winner.NOT_FINISHED if there aren't four coins in a row in
	 *         vertical or the number of the player who has won if any.
	 */
	private Winner checkVerticalPositions() {

		// Check the vertical positions
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;
		for (int i = Math.max(0, this.lastX - 3); i <= this.lastX; i++) {
			// If it is empty or different from the coin before
			if (this.gameBoard[i][this.lastY] == Integer.MIN_VALUE
					| this.gameBoard[i][this.lastY] != playerConnecting) {
				coinsConnected = 1;
				playerConnecting = this.gameBoard[i][this.lastY];
			}
			// If the coins in the current position is the same than before
			// it increment the counter.
			else {
				coinsConnected++;
			}
			// Check if there are four coins in a row of the same player
			if (coinsConnected == 4) {
				return (playerConnecting == 1) ? Winner.PLAYER1
						: Winner.PLAYER2;
			}
		}
		return Winner.NOT_FINISHED;
	}

	/**
	 * Check if there are four coins connected in horizontal.
	 * 
	 * @return Winner.NOT_FINISHED if there aren't four coins in a row in
	 *         horizontal or the number of the player who has won if any.
	 */
	private Winner checkHorizontalPositions() {

		System.out.println("it is called.");
		// Check the horizontal positions
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;
		for (int j = Math.max(0, this.lastY - 3); j <= Math.min(this.y - 1,
				this.lastY + 3); j++) {
			// If it is empty or different from the coin before
			if (this.gameBoard[this.lastX][j] == Integer.MIN_VALUE
					| this.gameBoard[this.lastX][j] != playerConnecting) {
				coinsConnected = 1;
				playerConnecting = this.gameBoard[this.lastX][j];
			}
			// If the coins in the current position is the same than before
			// it increment the counter.
			else {
				coinsConnected++;
			}
			// Check if there are four coins in a row of the same player
			if (coinsConnected == 4) {
				return (playerConnecting == 1) ? Winner.PLAYER1
						: Winner.PLAYER2;
			}
		}
		return Winner.NOT_FINISHED;
	}

	/**
	 * Check if there are four coins connected in cross.
	 * 
	 * @return Winner.NOT_FINISHED if there aren't four coins in a row in cross
	 *         or the number of the player who has won if any.
	 */
	private Winner checkCrossPositions() {
		// Initialize the vars
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;
		// This var contains the value to calculate where to start to search for
		// 4 in a row
		int border = Math.min(Math.min(this.lastX, this.lastY), 3);
		// In this loop check the rows
		for (int i = Math.max(0, this.lastX - border), j = Math.max(0,
				this.lastY - border); i < Math.min(this.lastX + 4, this.x)
				&& j < Math.min(this.lastY + 4, this.y); i++, j++) {
			// If it is empty or different from the coin before
			if (this.gameBoard[i][j] == Integer.MIN_VALUE
					| this.gameBoard[i][j] != playerConnecting) {
				coinsConnected = 1;
				playerConnecting = this.gameBoard[i][j];
			}
			// If the coins in the current position is the same than
			// before it increments the counter.
			else {
				coinsConnected++;
			}
			// Check if there are four coins in a row of the same player
			if (coinsConnected == 4) {
				return (playerConnecting == 1) ? Winner.PLAYER1
						: Winner.PLAYER2;
			}
		}
		playerConnecting = Integer.MIN_VALUE;
		coinsConnected = 1;

		border = Math.min(Math.min(this.x - this.lastX, this.lastY), 3);
		for (int i = Math.min(this.x - 1, this.lastX + border), j = Math.max(0,
				this.lastY - border); i >= Math.max(0, this.lastX - 3)
				&& j < Math.min(this.lastY + 4, this.y); i--, j++) {
			// If it is empty or different from the coin before
			if (this.gameBoard[i][j] == Integer.MIN_VALUE
					| this.gameBoard[i][j] != playerConnecting) {
				coinsConnected = 1;
				playerConnecting = this.gameBoard[i][j];
			}
			// If the coins in the current position is the same than
			// before it increments the counter.
			else {
				coinsConnected++;
			}
			// Check if there are four coins in a row of the same player
			if (coinsConnected == 4) {
				return (playerConnecting == 1) ? Winner.PLAYER1
						: Winner.PLAYER2;
			}
		}
		return Winner.NOT_FINISHED;
	}
}
