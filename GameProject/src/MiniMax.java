import java.util.LinkedList;
import java.util.List;

public class MiniMax {

	private int x = 0;// columns
	private int y = 0;// raws
	private int playerID = 0;
	private final int maximumDepth = 2;
	private long timeCheck = 0;

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
		this.timeCheck = 0;
		double v = Integer.MIN_VALUE;
		double v1 = Integer.MIN_VALUE;
		double alpha = Integer.MIN_VALUE;
		double beta = Integer.MAX_VALUE;
		int b = 0;
		for (Action action : actions(gameboard, this.playerID)) {
			v = Math.max(
					v,
					minDecision(result(gameboard, action), alpha, beta, 0,
							action.column));

			alpha = Math.max(alpha, v);
			if (v > v1) {
				b = action.column;
				v1 = v;
			}
		}
		System.out.println("Time: "
				+ Long.toString(System.currentTimeMillis() - start));
		// int v = maxDecision(gameboard,Integer.MIN_VALUE,Integer.MAX_VALUE);
		System.out.println("Time checking: " + this.timeCheck);
		System.out.println();
		return b;
	}

	private double maxDecision(int[][] gameboard, double alpha, double beta,
			int depth, int lastColumn) {
		depth++;
		if (cutoff_test(gameboard, depth, lastColumn)) {
			return evaluation(gameboard,
					playerID == 1 ? IGameLogic.Winner.PLAYER1
							: IGameLogic.Winner.PLAYER2, lastColumn);
		}
		// assign the min value of the integers to this variable
		double v = Integer.MIN_VALUE;
		for (Action action : actions(gameboard, this.playerID)) {
			v = Math.max(
					v,
					minDecision(result(gameboard, action), alpha, beta, depth,
							action.column));
			if (v >= beta) {
				return v;
			}

			alpha = Math.max(alpha, v);
		}

		return v;
	}

	private double minDecision(int[][] gameboard, double alpha, double beta,
			int depth, int lastColumn) {
		depth++;
		if (cutoff_test(gameboard, depth, lastColumn)) {
			return evaluation(gameboard,
					playerID == 1 ? IGameLogic.Winner.PLAYER1
							: IGameLogic.Winner.PLAYER2, lastColumn);
		}
		// assign the min value of the integers to this variable
		double v = Integer.MAX_VALUE;
		for (Action action : actions(gameboard, this.playerID % 2 + 1)) {

			v = Math.min(
					v,
					maxDecision(result(gameboard, action), alpha, beta, depth,
							action.column));
			if (v <= alpha) {
				return v;
			}

			beta = Math.min(beta, v);
		}

		return v;
	}

	private boolean cutoff_test(int[][] gameBoard, int depth, int lastColumn) {
		IGameLogic.Winner player = this.playerID == 1 ? IGameLogic.Winner.PLAYER1
				: IGameLogic.Winner.PLAYER2;
		int lastX = 0;
		for (int i = this.x - 1; i > 0; i--) {
			if (gameBoard[i][lastColumn] != Integer.MIN_VALUE) {
				lastX = i;
				break;
			}
		}
		long start = System.currentTimeMillis();
		if (this.checkTie(gameBoard) == IGameLogic.Winner.TIE) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		} else if (this.checkCrossPositions(gameBoard, lastX, lastColumn, 4) != IGameLogic.Winner.NOT_FINISHED) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		} else if (this.checkHorizontalPositions(gameBoard, lastX, lastColumn,
				4) != IGameLogic.Winner.NOT_FINISHED) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		} else if (this.checkVerticalPositions(gameBoard, lastX, lastColumn, 4) != IGameLogic.Winner.NOT_FINISHED) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		} else if (depth > this.maximumDepth) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		}
		this.timeCheck += System.currentTimeMillis() - start;
		return false;
	}

	private int[][] result(int[][] gameboard, Action action) {
		int[][] copy_gameboard = new int[this.x][this.y];
		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
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

	public double evaluation(int[][] gameBoard, IGameLogic.Winner player,
			int lastColumn) {
		long start = System.currentTimeMillis();
		IGameLogic.Winner result;
		int lastX = 0;
		for (int i = this.x - 1; i > 0; i--) {
			if (gameBoard[i][lastColumn] != Integer.MIN_VALUE) {
				lastX = i;
				break;
			}
		}
		if (this.checkTie(gameBoard) == IGameLogic.Winner.TIE) {
			this.timeCheck += System.currentTimeMillis() - start;
			return 0;
		} else if ((result = this.checkCrossPositions(gameBoard, lastX,
				lastColumn, 4)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				return 1;
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				return -1;
			}
		} else if ((result = this.checkHorizontalPositions(gameBoard, lastX,
				lastColumn, 4)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				return 1;
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				return -1;
			}
		} else if ((result = this.checkVerticalPositions(gameBoard, lastX,
				lastColumn, 4)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				return 1;
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				return -1;
			}
		} else if ((result = this.checkCrossPositions(gameBoard, lastX,
				lastColumn, 3)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				return 0.5;
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				return -1;
			}
		} else if ((result = this.checkHorizontalPositions(gameBoard, lastX,
				lastColumn, 3)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				return 0.5;
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				return -1;
			}
		} else if ((result = this.checkVerticalPositions(gameBoard, lastX,
				lastColumn, 3)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				return 0.5;
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				return -1;
			}
		} 
//		else if ((result = this.checkCrossPositions(gameBoard, lastX,
//				lastColumn, 2)) != IGameLogic.Winner.NOT_FINISHED) {
//			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
//					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
//				this.timeCheck += System.currentTimeMillis() - start;
//				return 0.5;
//			} else {
//				this.timeCheck += System.currentTimeMillis() - start;
//				return -1;
//			}
//		} else if ((result = this.checkHorizontalPositions(gameBoard, lastX,
//				lastColumn, 2)) != IGameLogic.Winner.NOT_FINISHED) {
//			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
//					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
//				this.timeCheck += System.currentTimeMillis() - start;
//				return 0.5;
//			} else {
//				this.timeCheck += System.currentTimeMillis() - start;
//				return -1;
//			}
//		} else if ((result = this.checkVerticalPositions(gameBoard, lastX,
//				lastColumn, 2)) != IGameLogic.Winner.NOT_FINISHED) {
//			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
//					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
//				this.timeCheck += System.currentTimeMillis() - start;
//				return 0.5;
//			} else {
//				this.timeCheck += System.currentTimeMillis() - start;
//				return -1;
//			}
//		}
		this.timeCheck += System.currentTimeMillis() - start;
		return 0;
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
			int lastX, int lastY, int connected) {

		// Check the vertical positions
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;
		for (int i = Math.max(0, lastX - 3); i <= lastX; i++) {
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
	private IGameLogic.Winner checkHorizontalPositions(int[][] gameBoard,
			int lastX, int lastY, int connected) {
		// Check the horizontal positions
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;
		for (int j = Math.max(0, lastY - 3); j <= Math.min(this.y - 1,
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
				return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
						: IGameLogic.Winner.PLAYER2;
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
	private IGameLogic.Winner checkCrossPositions(int[][] gameBoard, int lastX,
			int lastY, int connected) {
		// Initialize the vars
		int playerConnecting = Integer.MIN_VALUE;
		int coinsConnected = 1;
		// This var contains the value to calculate where to start to search for
		// 4 in a row
		int border = Math.min(Math.min(lastX, lastY), 3);
		// In this loop check the rows
		for (int i = Math.max(0, lastX - border), j = Math.max(0, lastY
				- border); i < Math.min(lastX + 4, this.x)
				&& j < Math.min(lastY + 4, this.y); i++, j++) {
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
				return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
						: IGameLogic.Winner.PLAYER2;
			}
		}
		playerConnecting = Integer.MIN_VALUE;
		coinsConnected = 1;

		border = Math.min(Math.min(this.x - lastX, lastY), 3);
		for (int i = Math.min(this.x - 1, lastX + border), j = Math.max(0,
				lastY - border); i >= Math.max(0, lastX - 3)
				&& j < Math.min(lastY + 4, this.y); i--, j++) {
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
				return (playerConnecting == 1) ? IGameLogic.Winner.PLAYER1
						: IGameLogic.Winner.PLAYER2;
			}
		}
		return IGameLogic.Winner.NOT_FINISHED;
	}
}
