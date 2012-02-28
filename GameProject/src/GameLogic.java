public class GameLogic implements IGameLogic {
	private int x = 0;
	private int y = 0;
	private int playerID;
	private int[][] gameBoard;

	public GameLogic() {
		// TODO Write your implementation for this method
	}

	public void initializeGame(int x, int y, int playerID) {
		this.x = x;
		this.y = y;
		this.playerID = playerID;
		this.gameBoard = new int[x][y];
		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
				this.gameBoard[i][j] = Integer.MIN_VALUE;
			}
		}
		// TODO Write your implementation for this method
	}

	public Winner gameFinished() {
		Winner result = Winner.NOT_FINISHED;
		if ((result = this.checkVerticalPositions()) != Winner.NOT_FINISHED) {
		} else if ((result = this.checkHorizontalPositions()) != Winner.NOT_FINISHED) {
		} else if ((result = this.checkTie()) == Winner.TIE) {
		}
		return result;
	}

	public void insertCoin(int column, int playerID) {
		for (int i = 0; i < this.y; i++) {
			// If the position is already busy continue to the next
			if (this.gameBoard[column][i] != Integer.MIN_VALUE) {
				continue;
			}
			// If the position is free it stores the new coin and finishes the
			// method
			this.gameBoard[column][i] = playerID;
			break;
		}
	}

	public int decideNextMove() {
		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
				// If the position is already occupated continue to the next
				if (this.gameBoard[i][j] != Integer.MIN_VALUE) {
					continue;
				}
				return i;
			}
		}
		return 0;
	}

	private Winner checkTie() {
		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
				if (this.gameBoard[i][j] == Integer.MIN_VALUE) {
					return Winner.NOT_FINISHED;
				}
			}
		}
		return Winner.TIE;
	}

	private Winner checkVerticalPositions() {

		// Check the vertical positions
		for (int i = 0; i < this.x; i++) {
			int coinBefore = Integer.MIN_VALUE;
			int coinsConnected = 1;
			for (int j = 0; j < this.y; j++) {
				// If there isn't more coins it finish the checking
				if (this.gameBoard[i][j] == Integer.MIN_VALUE) {
					break;
				}
				// If the coins in the current position is the same than before
				// it increment the counter.
				else if (this.gameBoard[i][j] == coinBefore) {
					coinsConnected++;
				}
				// Else restart the counter.
				else {
					coinsConnected = 1;
					coinBefore = this.gameBoard[i][j];
				}
				// Check if there are four coins in a row of the same player
				if (coinsConnected == 4) {
					return (this.playerID == 1) ? Winner.PLAYER1
							: Winner.PLAYER2;
				}
			}
		}
		return Winner.NOT_FINISHED;
	}

	private Winner checkHorizontalPositions() {

		// Check the horizontal positions
		for (int j = 0; j < this.y; j++) {
			int playerConnecting = Integer.MIN_VALUE;
			int coinsConnected = 1;
			for (int i = 0; i < this.x; i++) {
				// If there isn't more coins it finish the checking
				if (this.gameBoard[i][j] == Integer.MIN_VALUE) {
					break;
				}
				// If the coins in the current position is the same than before
				// it increment the counter.
				else if (this.gameBoard[i][j] == playerConnecting) {
					coinsConnected++;
				}
				// Else restart the counter.
				else {
					coinsConnected = 1;
					playerConnecting = this.gameBoard[i][j];
				}
				// Check if there are four coins in a row of the same player
				if (coinsConnected == 4) {
					return (this.playerID == playerConnecting) ? Winner.PLAYER1
							: Winner.PLAYER2;
				}
			}
		}
		return Winner.NOT_FINISHED;
	}

}
