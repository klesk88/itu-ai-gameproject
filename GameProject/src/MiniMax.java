import java.util.LinkedList;
import java.util.List;

public class MiniMax {

	private int x = 0;// columns
	private int y = 0;// raws
	private int playerID = 0;

	public MiniMax(int x, int y, int playerID) {

		this.x = x;
		this.y = y;
		this.playerID = playerID;
	}

	private class Action {
		int column;
		int player;

		public Action(int ncolumn, int nplayer) {
			this.column = ncolumn;
			this.player = nplayer;
		}
	}

	private List<Action> actions(int[][] gameboard, int player) {
		List<Action> list_of_action = new LinkedList<Action>();

		for (int j = 0; j < y; j++) {
			// if there isn't no one in that position i put a coin inside that
			// and i change column
			if (gameboard[x - 1][j] == Integer.MIN_VALUE) {
				// copy_gameboard[i][j] = playerID; // result
				// list_of_action.add(i);//add the column to the list
				list_of_action.add(new Action(j, player));
			}
		}
		return list_of_action;// i don't never have to came here
	}

	public int miniMax(int[][] gameboard) {
		long start = System.currentTimeMillis();
		int v = Integer.MIN_VALUE;
		int v1 = Integer.MIN_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int b = 0;
		for (Action action : actions(gameboard, this.playerID)) {
			v = Math.max(
					v,
					minDecision(result(gameboard, action), alpha,
							beta));

			alpha = Math.max(alpha, v);
			if (v > v1) {
				b = action.column;
				v1 = v;
			}

		}
		System.out.println("Time: "
				+ Long.toString(System.currentTimeMillis() - start));
		// int v = maxDecision(gameboard,Integer.MIN_VALUE,Integer.MAX_VALUE);
		return b;
	}

	private int maxDecision(int[][] gameboard, int alpha, int beta) {
		if (TerminalState(gameboard)) {
			return Utility(gameboard, playerID == 1 ? IGameLogic.Winner.PLAYER1
					: IGameLogic.Winner.PLAYER2);
		}
		// assign the min value of the integers to this variable
		int v = Integer.MIN_VALUE;
		for (Action action : actions(gameboard, this.playerID)) {

			v = Math.max(v, minDecision(result(gameboard, action), alpha, beta));
			if (v >= beta) {
				return v;
			}

			alpha = Math.max(alpha, v);
		}

		return v;
	}

	private int minDecision(int[][] gameboard, int alpha, int beta) {
		if (TerminalState(gameboard)) {
			return Utility(gameboard, playerID == 1 ? IGameLogic.Winner.PLAYER1
					: IGameLogic.Winner.PLAYER2);
		}
		// assign the min value of the integers to this variable
		int v = Integer.MAX_VALUE;
		for (Action action : actions(gameboard, this.playerID % 2 + 1)) {

			v = Math.min(v, maxDecision(result(gameboard, action), alpha, beta));
			if (v <= alpha) {
				return v;
			}

			beta = Math.min(beta, v);
		}

		return v;
	}

	private int[][] result(int[][] gameboard, Action action) {
		int[][] copy_gameboard = new int[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				copy_gameboard[i][j] = gameboard[i][j];
			}
		}

		for (int i = 0; i < this.x; i++) {
			if (gameboard[i][action.column] == Integer.MIN_VALUE) {
				copy_gameboard[i][action.column] = action.player;
				break;
			}
		}

		return copy_gameboard;
	}

	public int Utility(int[][] gameBoard, IGameLogic.Winner player) {
		IGameLogic.Winner result;
		if (this.checkTie(gameBoard) == IGameLogic.Winner.TIE) {
			return 0;
		} else if ((result = this.checkCrossPositions(gameBoard, player)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				return 1;
			} else {
				return -1;
			}
		} else if ((result = this.checkHorizontalPositions(gameBoard, player)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				return 1;
			} else {
				return -1;
			}
		} else if ((result = this.checkVerticalPositions(gameBoard, player)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;
	}

	public boolean TerminalState(int[][] gameBoard) {
		IGameLogic.Winner player = this.playerID == 1 ? IGameLogic.Winner.PLAYER1
				: IGameLogic.Winner.PLAYER2;
		if (this.checkTie(gameBoard) == IGameLogic.Winner.TIE) {
			return true;
		} else if (this.checkCrossPositions(gameBoard, player) != IGameLogic.Winner.NOT_FINISHED) {
			return true;
		} else if (this.checkHorizontalPositions(gameBoard, player) != IGameLogic.Winner.NOT_FINISHED) {
			return true;
		} else if (this.checkVerticalPositions(gameBoard, player) != IGameLogic.Winner.NOT_FINISHED) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the game have finished with a tie.
	 * 
	 * @return Winner.NOT_FINISHED if the game hasn't finished or Winner.TIE if
	 *         the game has finished and none player has won.
	 */
	private IGameLogic.Winner checkTie(int[][] gameBoard) {
		// Check the top position of every column
		for (int j = 0; j < this.y; j++) {
			if (gameBoard[this.x - 1][j] == Integer.MIN_VALUE) {
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
	private IGameLogic.Winner checkVerticalPositions(int[][] gameBoard,
			IGameLogic.Winner player) {

		// Check the vertical positions
		for (int i = 0; i < this.x; i++) {
			int playerConnecting = Integer.MIN_VALUE;
			int coinsConnected = 1;
			for (int j = 0; j < this.y && gameBoard[i][j] != Integer.MIN_VALUE; j++) {
				// If it is empty or different from the coin before
				if (gameBoard[i][j] != playerConnecting) {
					coinsConnected = 1;
					playerConnecting = gameBoard[i][j];
				}
				// If the coins in the current position is the same than before
				// it increment the counter.
				else {
					coinsConnected++;
				}
				// Check if there are four coins in a row of the same player
				if (coinsConnected == 4) {
					return playerConnecting == 1 ? IGameLogic.Winner.PLAYER1
							: IGameLogic.Winner.PLAYER2;
				}
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
	private IGameLogic.Winner checkHorizontalPositions(int[][] gameBoard,
			IGameLogic.Winner player) {

		// Check the horizontal positions
		for (int j = 0; j < this.y; j++) {
			int playerConnecting = Integer.MIN_VALUE;
			int coinsConnected = 1;
			for (int i = 0; i < this.x; i++) {
				// If it is empty or different from the coin before
				if (gameBoard[i][j] == Integer.MIN_VALUE
						| gameBoard[i][j] != playerConnecting) {
					coinsConnected = 1;
					playerConnecting = gameBoard[i][j];
				}
				// If the coins in the current position is the same than before
				// it increment the counter.
				else {
					coinsConnected++;
				}
				// Check if there are four coins in a row of the same player
				if (coinsConnected == 4) {
					return playerConnecting == 1 ? IGameLogic.Winner.PLAYER1
							: IGameLogic.Winner.PLAYER2;
				}
			}
		}
		return IGameLogic.Winner.NOT_FINISHED;
	}

	/**
	 * Check if there are four coins connected in cross.
	 * 
	 * @return Winner.NOT_FINISHED if there aren't four coins in a row in cross
	 *         or the number of the player who has won if any.
	 */
	private IGameLogic.Winner checkCrossPositions(int[][] gameBoard,
			IGameLogic.Winner player) {
		for (int x = 0; x < this.x; x++) {
			for (int y = 0; y < this.y; y++) {
				// Initialize the vars
				int playerConnecting = Integer.MIN_VALUE;
				int coinsConnected = 1;
				// In this loop check the rows
				for (int i = x, j = y; i < this.x && j < this.y; i++, j++) {
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
					if (coinsConnected == 4) {
						return playerConnecting == 1 ? IGameLogic.Winner.PLAYER1
								: IGameLogic.Winner.PLAYER2;
					}
				}
			}
		}
		for (int x = 0; x < this.x; x++) {
			for (int y = 0; y < this.y; y++) {
				// Initialize the vars
				int playerConnecting = Integer.MIN_VALUE;
				int coinsConnected = 1;
				// In this loop check the rows
				for (int i = x, j = this.y - 1; i < this.x && j >= 0; i++, j--) {
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
					if (coinsConnected == 4) {
						return playerConnecting == 1 ? IGameLogic.Winner.PLAYER1
								: IGameLogic.Winner.PLAYER2;
					}
				}
			}
		}
		return IGameLogic.Winner.NOT_FINISHED;
	}
}
