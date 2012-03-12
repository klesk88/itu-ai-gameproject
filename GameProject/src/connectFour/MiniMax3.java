package connectFour;

import java.util.LinkedList;
import connectFour.*;
import java.util.List;

public class MiniMax3 {

	private int x = 0;// columns
	private int y = 0;// raws
	private int playerID = 0;
	private final int maximumDepth = 10;
	private long timeCheck = 0;
	private long timeRunning = 0;
	private int[][] factors = { { 3, 4, 5, 7, 5, 4, 3 },
			{ 4, 5, 8, 10, 8, 5, 4 }, { 5, 8, 11, 15, 11, 8, 5 },
			{ 5, 8, 11, 15, 11, 8, 5 }, { 4, 6, 8, 10, 8, 6, 4 },
			{ 3, 4, 5, 7, 5, 4, 3 } };

	public MiniMax3(int x, int y, int playerID) {

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
		this.timeRunning = System.currentTimeMillis();
		// if (gameboard[0][this.y / 2 + this.y % 2 - 1] == Integer.MIN_VALUE) {
		// return this.y / 2 + this.y % 2 - 1;
		// } else if (gameboard[1][this.y / 2 + this.y % 2 - 1] ==
		// Integer.MIN_VALUE
		// && gameboard[0][this.y / 2 + this.y % 2 - 1] != this.playerID) {
		// return this.y / 2 + this.y % 2 - 1;
		// }
		for (Action action : actions(gameboard, this.playerID)) {
			v = Math.max(
					v,
					minDecision(result(gameboard, action), alpha, beta, 0,
							action.column));
			gameboard = recoverResult(gameboard, action);
			System.out.println("Column: " + action.column + " value: " + v);
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
			gameboard = recoverResult(gameboard, action);
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
			gameboard = recoverResult(gameboard, action);
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
		if (ConnectedChecking.checkTie(gameBoard, this.x, this.y) == IGameLogic.Winner.TIE) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		} else if (ConnectedChecking.checkCrossPositions(gameBoard, lastX,
				lastColumn, 4, this.x, this.y) != IGameLogic.Winner.NOT_FINISHED) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		} else if (ConnectedChecking.checkHorizontalPositions(gameBoard, lastX,
				lastColumn, 4, this.y) != IGameLogic.Winner.NOT_FINISHED) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		} else if (ConnectedChecking.checkVerticalPositions(gameBoard, lastX,
				lastColumn, 4) != IGameLogic.Winner.NOT_FINISHED) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		}
		// else if ((System.currentTimeMillis() - this.timeRunning) > 10000) {
		// this.timeCheck += System.currentTimeMillis() - start;
		// return true;
		// }
		else if (depth > this.maximumDepth) {
			this.timeCheck += System.currentTimeMillis() - start;
			return true;
		}
		this.timeCheck += System.currentTimeMillis() - start;
		return false;
	}

	private int[][] result(int[][] gameboard, Action action) {
		// int[][] copy_gameboard = new int[this.x][this.y];
		// for (int i = 0; i < this.x; i++) {
		// for (int j = 0; j < this.y; j++) {
		// copy_gameboard[i][j] = gameboard[i][j];
		// }
		// }

		for (int i = 0; i < this.x; i++) {
			if (gameboard[i][action.column] == Integer.MIN_VALUE) {
				gameboard[i][action.column] = action.player;
				break;
			}
		}

		return gameboard;
	}

	private int[][] recoverResult(int[][] gameboard, Action action) {
		// int[][] copy_gameboard = new int[this.x][this.y];
		// for (int i = 0; i < this.x; i++) {
		// for (int j = 0; j < this.y; j++) {
		// copy_gameboard[i][j] = gameboard[i][j];
		// }
		// }

		if (gameboard[this.x - 1][action.column] != Integer.MIN_VALUE) {
			gameboard[this.x - 1][action.column] = Integer.MIN_VALUE;
		} else {

			for (int i = 0; i < this.x; i++) {
				if (gameboard[i][action.column] == Integer.MIN_VALUE) {
					gameboard[i - 1][action.column] = Integer.MIN_VALUE;
					break;
				}
			}
		}
		return gameboard;
	}

	public double evaluation(int[][] gameBoard, IGameLogic.Winner player,
			int lastColumn) {
		long start = System.currentTimeMillis();
		IGameLogic.Winner result;
		int lastX = 0;
		double value = 0;
		for (int i = this.x - 1; i > 0; i--) {
			if (gameBoard[i][lastColumn] != Integer.MIN_VALUE) {
				lastX = i;
				break;
			}
		}
		if (ConnectedChecking.checkTie(gameBoard, this.x, this.y) == IGameLogic.Winner.TIE) {
			this.timeCheck += System.currentTimeMillis() - start;
			value = 0;
		} else if ((result = ConnectedChecking.checkCrossPositions(gameBoard,
				lastX, lastColumn, 4, this.x, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				value = 1 + this.foo(gameBoard, lastX, lastColumn);
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				value = -1 + this.foo(gameBoard, lastX, lastColumn);
			}
		} else if ((result = ConnectedChecking.checkHorizontalPositions(
				gameBoard, lastX, lastColumn, 4, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				value = 1 + this.foo(gameBoard, lastX, lastColumn);
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				value = -1 + this.foo(gameBoard, lastX, lastColumn);
			}
		} else if ((result = ConnectedChecking.checkVerticalPositions(
				gameBoard, lastX, lastColumn, 4)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				value = 1 + this.foo(gameBoard, lastX, lastColumn);
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				value = -1 + this.foo(gameBoard, lastX, lastColumn);
			}
		} else if ((result = ConnectedChecking.checkCrossPositions(gameBoard,
				lastX, lastColumn, 3, this.x, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				value = 0.5 + this.foo(gameBoard, lastX, lastColumn);
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				value = -0.5 + this.foo(gameBoard, lastX, lastColumn);
			}
		} else if ((result = ConnectedChecking.checkHorizontalPositions(
				gameBoard, lastX, lastColumn, 3, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				value = 0.5 + this.foo(gameBoard, lastX, lastColumn);
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				value = -0.5 + this.foo(gameBoard, lastX, lastColumn);
			}
		} else if ((result = ConnectedChecking.checkVerticalPositions(
				gameBoard, lastX, lastColumn, 3)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				this.timeCheck += System.currentTimeMillis() - start;
				value = 0.5 + this.foo(gameBoard, lastX, lastColumn);
			} else {
				this.timeCheck += System.currentTimeMillis() - start;
				value = -0.5 + this.foo(gameBoard, lastX, lastColumn);
			}
		} // else if ((result = this.checkCrossPositions(gameBoard, lastX,
			// lastColumn, 2)) != IGameLogic.Winner.NOT_FINISHED) {
			// if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
			// || (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
			// this.timeCheck += System.currentTimeMillis() - start;
			// return 0.5;
			// } else {
			// this.timeCheck += System.currentTimeMillis() - start;
			// return -0.5;
			// }
			// } else if ((result = this.checkHorizontalPositions(gameBoard,
			// lastX,
			// lastColumn, 2)) != IGameLogic.Winner.NOT_FINISHED) {
			// if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
			// || (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
			// this.timeCheck += System.currentTimeMillis() - start;
			// return 0.5;
			// } else {
			// this.timeCheck += System.currentTimeMillis() - start;
			// return -0.5;
			// }
			// } else if ((result = this.checkVerticalPositions(gameBoard,
			// lastX,
			// lastColumn, 2)) != IGameLogic.Winner.NOT_FINISHED) {
			// if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
			// || (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
			// this.timeCheck += System.currentTimeMillis() - start;
			// return 0.5;
			// } else {
			// this.timeCheck += System.currentTimeMillis() - start;
			// return -0.5;
			// }
			// }
		this.timeCheck += System.currentTimeMillis() - start;
		return value;
	}

	private double foo(int[][] gameBoard, int lastX, int lastY) {
		double result = 0.0;
		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
				if (Math.abs(i - lastX) < 3 && Math.abs(j - lastY) < 3
						& gameBoard[i][j] != Integer.MIN_VALUE) {
					if ((gameBoard[i][j] == 1 && this.playerID == 1)
							|| (gameBoard[i][j] == 2 && this.playerID == 2)) {
						result += 0.01 * this.factors[i][j];
					}
				}
			}
		}
		return result;
	}

}
