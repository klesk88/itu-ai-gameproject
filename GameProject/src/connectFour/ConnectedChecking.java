package connectFour;

/**
 * This class has methods which are able to check if there is a number of coins
 * in a row in different directions.
 * 
 */
public class ConnectedChecking {
	/**
	 * Check if the game have finished with a tie.
	 * 
	 * @return Winner.NOT_FINISHED if the game hasn't finished or Winner.TIE if
	 *         the game has finished and none player has won.
	 */
	public static IGameLogic.Winner checkTie(final int[][] gameBoard,
			final int x, final int y) {
		// Check the top position of every column
		for (int j = 0; j < y; j++) {
			if (gameBoard[x - 1][j] == Integer.MIN_VALUE) {
				return IGameLogic.Winner.NOT_FINISHED;
			}
		}
		return IGameLogic.Winner.TIE;
	}

	/**
	 * Check if there are four coins connected in vertical.
	 * 
	 * @return Winner.NOT_FINISHED if there aren't four coins in a row in
	 *         vertical or the number of the player who has won if any.
	 */
	public static IGameLogic.Winner checkVerticalPositions(
			final int[][] gameBoard, final int lastX, final int lastY,
			final int connected) {

		// Check the vertical positions
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;
		for (int i = Math.max(0, lastX - (connected - 1)); i <= lastX; i++) {
			// If it is empty or different from the coin before
			if (gameBoard[i][lastY] == Integer.MIN_VALUE
					| gameBoard[i][lastY] != playerConnecting) {
				coinsConnected = 1;
				playerConnecting = gameBoard[i][lastY];
			}
			// If the coins in the current position is the same than before
			// it increment the counter.
			else {
				coinsConnected++;
			}
			// Check if there are four coins in a row of the same player
			if (coinsConnected == connected) {
				return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
						: IGameLogic.Winner.PLAYER2;
			}
		}
		return IGameLogic.Winner.NOT_FINISHED;
	}

	/**
	 * Check if there are four coins connected in horizontal.
	 * 
	 * @return Winner.NOT_FINISHED if there aren't four coins in a row in
	 *         horizontal or the number of the player who has won if any.
	 */
	public static IGameLogic.Winner checkHorizontalPositions(
			final int[][] gameBoard, final int lastX, final int lastY,
			final int connected, final int y) {
		// Check the horizontal positions
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;
		for (int j = Math.max(0, lastY - (connected - 1)); j <= Math.min(y - 1,
				lastY + 3); j++) {
			// If it is empty or different from the coin before
			if (gameBoard[lastX][j] == Integer.MIN_VALUE
					| gameBoard[lastX][j] != playerConnecting) {
				coinsConnected = 1;
				playerConnecting = gameBoard[lastX][j];
			}
			// If the coins in the current position is the same than before
			// it increment the counter.
			else {
				coinsConnected++;
			}
			// Check if there are four coins in a row of the same player
			if (coinsConnected == connected) {
				if (connected == 3) {
					if ((j >= 3 && gameBoard[lastX][j - 3] == Integer.MIN_VALUE)
							| (j < (y - 1) && gameBoard[lastX][j + 1] == Integer.MIN_VALUE)) {
						return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
								: IGameLogic.Winner.PLAYER2;
					}
				} else if (connected == 4) {
					return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
							: IGameLogic.Winner.PLAYER2;
				}
				return IGameLogic.Winner.NOT_FINISHED;
			}
		}
		return IGameLogic.Winner.NOT_FINISHED;
	}

	/**
	 * Check if there are four coins connected in cross from the bottom to the
	 * top.
	 * 
	 * @return Winner.NOT_FINISHED if there aren't four coins in a row in cross
	 *         or the number of the player who has won if any.
	 */
	public static IGameLogic.Winner checkCrossPositionsUp(
			final int[][] gameBoard, final int lastX, final int lastY,
			final int connected, final int x, final int y) {
		// Initialize the vars
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;
		// This var contains the value to calculate where to start to search for
		// 4 in a row
		int border = Math.min(Math.min(lastX, lastY), connected - 1);
		// In this loop check the rows
		for (int i = Math.max(0, lastX - border), j = Math.max(0, lastY
				- border); i < Math.min(lastX + connected, x)
				&& j < Math.min(lastY + 4, y); i++, j++) {
			// If it is empty or different from the coin before
			if (gameBoard[i][j] == Integer.MIN_VALUE
					| gameBoard[i][j] != playerConnecting) {
				coinsConnected = 1;
				playerConnecting = gameBoard[i][j];
			}
			// If the coins in the current position is the same than
			// before it increments the counter.
			else {
				coinsConnected++;
			}
			// Check if there are four coins in a row of the same player
			if (coinsConnected == connected) {
				if (connected == 3) {
					if (((i >= 3 & j >= 3) && gameBoard[i - 3][j - 3] == Integer.MIN_VALUE)
							| (i < (x - 1) & (j < (y - 1)) && gameBoard[i + 1][j + 1] == Integer.MIN_VALUE)) {
						return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
								: IGameLogic.Winner.PLAYER2;
					}
				} else if (connected == 4) {
					return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
							: IGameLogic.Winner.PLAYER2;
				}
				return IGameLogic.Winner.NOT_FINISHED;
			}
		}
		return IGameLogic.Winner.NOT_FINISHED;
	}

	/**
	 * Check if there are four coins connected in cross from the top to the
	 * bottom.
	 * 
	 * @return Winner.NOT_FINISHED if there aren't four coins in a row in cross
	 *         or the number of the player who has won if any.
	 */
	public static IGameLogic.Winner checkCrossPositionsDown(
			final int[][] gameBoard, final int lastX, final int lastY,
			final int connected, final int x, final int y) {
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;

		int border = Math.min(Math.min(x - lastX - 1, lastY), connected - 1);
		for (int i = Math.min(x - 1, lastX + border), j = Math.max(0, lastY
				- border); i >= Math.max(0, lastX - (connected - 1))
				&& j < Math.min(lastY + connected, y); i--, j++) {
			// If it is empty or different from the coin before
			if (gameBoard[i][j] == Integer.MIN_VALUE
					| gameBoard[i][j] != playerConnecting) {
				coinsConnected = 1;
				playerConnecting = gameBoard[i][j];
			}
			// If the coins in the current position is the same than
			// before it increments the counter.
			else {
				coinsConnected++;
			}
			// Check if there are four coins in a row of the same player
			if (coinsConnected == connected) {
				if (connected == 3) {
					if (((i < (x - 3) & j >= 3) && gameBoard[i + 3][j - 3] == Integer.MIN_VALUE)
							| (i > 1 & (j < (y - 1)) && gameBoard[i - 1][j + 1] == Integer.MIN_VALUE)) {
						return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
								: IGameLogic.Winner.PLAYER2;
					}
				} else if (connected == 4) {
					return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
							: IGameLogic.Winner.PLAYER2;
				}
				return IGameLogic.Winner.NOT_FINISHED;
			}
		}
		return IGameLogic.Winner.NOT_FINISHED;
	}
}
