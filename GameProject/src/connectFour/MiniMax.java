package connectFour;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the minimax with alpha-beta pruning algorithm.
 * 
 */
public class MiniMax {

	private int x = 0;// columns
	private int y = 0;// rows
	private int playerID = 0;
	// The maximum depth of the tree
	private final int maximumDepth = 10;
	// The possible winning combinations from each position in a 7x6 board
	private int[][] factors = { { 3, 4, 5, 7, 5, 4, 3 },
			{ 4, 5, 8, 10, 8, 5, 4 }, { 5, 8, 11, 15, 11, 8, 5 },
			{ 5, 8, 11, 15, 11, 8, 5 }, { 4, 6, 8, 10, 8, 6, 4 },
			{ 3, 4, 5, 7, 5, 4, 3 } };

	public MiniMax(int x, int y, int playerID) {

		this.x = x;
		this.y = y;
		this.playerID = playerID;
	}

	/**
	 * An action is represented by the column where the coin is put and the
	 * player who is playing that turn.
	 * 
	 */
	private class Action {
		int column;
		int player;

		public Action(int ncolumn, int nplayer) {
			this.column = ncolumn;
			this.player = nplayer;
		}
	}

	/**
	 * Generates all the possible actions from a board and a player.
	 * 
	 * @param gameboard
	 *            The state of the board.
	 * @param player
	 *            The player who is playing this turn.
	 * @return The list of possible movements for that state and player.
	 */
	private List<Action> actions(int[][] gameboard, int player) {
		List<Action> list_of_action = new LinkedList<Action>();

		for (int j = 0; j < y; j++) {
			// if the j position is empty add the position to the list
			if (gameboard[x - 1][j] == Integer.MIN_VALUE) {
				list_of_action.add(new Action(j, player));
			}
		}
		return list_of_action;
	}

	/**
	 * Choose the best column where put a coin.
	 * 
	 * @param gameboard
	 *            The state of the game.
	 * @return The best column where put a coin.
	 */
	public int miniMax(int[][] gameboard) {
		double v = Integer.MIN_VALUE;
		double v1 = Integer.MIN_VALUE;
		double alpha = Integer.MIN_VALUE;
		double beta = Integer.MAX_VALUE;
		int chosenColumn = 0;
		// If it is one of the first two movements it just choose the positions
		// in the center
		if (gameboard[0][this.y / 2 + this.y % 2 - 1] == Integer.MIN_VALUE) {
			return this.y / 2 + this.y % 2 - 1;
		} else if (gameboard[1][this.y / 2 + this.y % 2 - 1] == Integer.MIN_VALUE
				&& gameboard[0][this.y / 2 + this.y % 2 - 1] != this.playerID) {
			return this.y / 2 + this.y % 2 - 1;
		}
		for (Action action : actions(gameboard, this.playerID)) {
			v = Math.max(
					v,
					minDecision(result(gameboard, action), alpha, beta, 0,
							action.column));
			// Since result(gameboard, action) modifies the values in gameboard
			// it is necessary recover the state
			gameboard = recoverResult(gameboard, action);
			System.out.println("Column: " + action.column + " value: " + v);
			alpha = Math.max(alpha, v);
			if (v > v1) {
				chosenColumn = action.column;
				v1 = v;
			}
		}
		return chosenColumn;
	}

	private double maxDecision(int[][] gameboard, double alpha,
			final double beta, int depth, final int lastColumn) {
		depth++;
		// Check if the state is a final state of it is the maximum depth
		if (cutoff_test(gameboard, depth, lastColumn)) {
			return evaluation(gameboard,
					playerID == 1 ? IGameLogic.Winner.PLAYER1
							: IGameLogic.Winner.PLAYER2, lastColumn, depth);
		}
		// Assigns the min value of the integers to this variable
		double v = Integer.MIN_VALUE;
		for (Action action : actions(gameboard, this.playerID)) {
			v = Math.max(
					v,
					minDecision(result(gameboard, action), alpha, beta, depth,
							action.column));
			// Since result(gameboard, action) modifies the values in gameboard
			// it is necessary recover the state
			gameboard = recoverResult(gameboard, action);
			if (v >= beta) {
				return v;
			}
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	private double minDecision(int[][] gameboard, final double alpha,
			double beta, int depth, final int lastColumn) {
		depth++;
		// Check if the state is a final state of it is the maximum depth
		if (cutoff_test(gameboard, depth, lastColumn)) {
			return evaluation(gameboard,
					playerID == 1 ? IGameLogic.Winner.PLAYER1
							: IGameLogic.Winner.PLAYER2, lastColumn, depth);
		}
		// Assigns the min value of the integers to this variable
		double v = Integer.MAX_VALUE;
		for (Action action : actions(gameboard, this.playerID % 2 + 1)) {
			v = Math.min(
					v,
					maxDecision(result(gameboard, action), alpha, beta, depth,
							action.column));
			// Since result(gameboard, action) modifies the values in gameboard
			// it is necessary recover the state
			gameboard = recoverResult(gameboard, action);
			if (v <= alpha) {
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
	}

	/**
	 * Check if the passed state is a final state. It checks if there are 4
	 * coins in a row or the depth is the maximum depth.
	 * 
	 * @param gameBoard
	 *            The state to check.
	 * @param depth
	 *            The maximum depth.
	 * @param lastColumn
	 *            The column where the last movement was done.
	 * @return True if it is a final state or it is the maximum depth.
	 */
	private boolean cutoff_test(final int[][] gameBoard, final int depth,
			final int lastColumn) {
		// Search the x position of the last movement
		int lastX = 0;
		for (int i = this.x - 1; i > 0; i--) {
			if (gameBoard[i][lastColumn] != Integer.MIN_VALUE) {
				lastX = i;
				break;
			}
		}
		// Check if the game has finished
		if ((depth > this.maximumDepth)
				| (ConnectedChecking.checkTie(gameBoard, this.x, this.y) == IGameLogic.Winner.TIE)
				| (ConnectedChecking.checkCrossPositionsDown(gameBoard, lastX,
						lastColumn, 4, this.x, this.y) != IGameLogic.Winner.NOT_FINISHED)
				| (ConnectedChecking.checkCrossPositionsUp(gameBoard, lastX,
						lastColumn, 4, this.x, this.y) != IGameLogic.Winner.NOT_FINISHED)
				| (ConnectedChecking.checkHorizontalPositions(gameBoard, lastX,
						lastColumn, 4, this.y) != IGameLogic.Winner.NOT_FINISHED)
				| (ConnectedChecking.checkVerticalPositions(gameBoard, lastX,
						lastColumn, 4) != IGameLogic.Winner.NOT_FINISHED)) {
			return true;
		}
		return false;
	}

	/**
	 * Modifies the state of the board adding the new action. For efficiency it
	 * doesn't copy the board to a new one, instead it modifies the existing
	 * board.
	 * 
	 * @param gameboard
	 *            The board to modify.
	 * @param action
	 *            The action than is being done.
	 * @return The new state.
	 */
	private int[][] result(int[][] gameboard, final Action action) {
		for (int i = 0; i < this.x; i++) {
			if (gameboard[i][action.column] == Integer.MIN_VALUE) {
				gameboard[i][action.column] = action.player;
				break;
			}
		}
		return gameboard;
	}

	/**
	 * Recovers the state than was before the action passed.
	 * 
	 * @param gameboard
	 *            The state to recover.
	 * @param action
	 *            The action to undo.
	 * @return The modified state.
	 */
	private int[][] recoverResult(int[][] gameboard, final Action action) {
		// If it is the top position of the column
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

	/**
	 * Evaluates a position and return a number than will be positive it is
	 * probably a winning condition or negative if it is a losing position. If
	 * the number is bigger than 1 it is probably a very good idea choose that
	 * position. If it is smaller than -1 it is a very bad idea choose that
	 * position.
	 * 
	 * @param gameBoard
	 *            The state of the game.
	 * @param player
	 *            The who is playing the last turn.
	 * @param lastColumn
	 *            The column where the last coin was put.
	 * @return A number showing the expected "probability" of winning from this
	 *         state.
	 */
	private final double evaluation(final int[][] gameBoard,
			final IGameLogic.Winner player, final int lastColumn,
			final int depth) {
		// The result than it is going to return
		IGameLogic.Winner result;
		int lastX = 0;
		// In value it puts the calculates value for this state
		double value = 0;
		// Search for the lastX
		for (int i = this.x - 1; i > 0; i--) {
			if (gameBoard[i][lastColumn] != Integer.MIN_VALUE) {
				lastX = i;
				break;
			}
		}
		if (ConnectedChecking.checkTie(gameBoard, this.x, this.y) == IGameLogic.Winner.TIE) {
			// It returns here to distinguishes this case to the case where you
			// aren't able to find any good stuff.
			return 0;
		} else if ((result = ConnectedChecking.checkCrossPositionsDown(
				gameBoard, lastX, lastColumn, 4, this.x, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				value = 1 + this.foo(gameBoard, lastX, lastColumn) * 0.01
						* depth;
			} else {
				value = -1 + 0.01 * depth;
			}
		} else if ((result = ConnectedChecking.checkCrossPositionsUp(gameBoard,
				lastX, lastColumn, 4, this.x, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				value = 1 + this.foo(gameBoard, lastX, lastColumn) * 0.01
						* depth;
			} else {
				value = -1 + 0.01 * depth;
			}
		} else if ((result = ConnectedChecking.checkHorizontalPositions(
				gameBoard, lastX, lastColumn, 4, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				value = 1 + this.foo(gameBoard, lastX, lastColumn) * 0.01
						* depth;
			} else {
				value = -1 + 0.01 * depth;
			}
		} else if ((result = ConnectedChecking.checkVerticalPositions(
				gameBoard, lastX, lastColumn, 4)) != IGameLogic.Winner.NOT_FINISHED) {
			if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
					|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
				value = 1 + this.foo(gameBoard, lastX, lastColumn) * 0.01
						* depth;
			} else {
				value = -1 + 0.01 * depth;
			}
		} else {
			if ((result = ConnectedChecking.checkCrossPositionsDown(gameBoard,
					lastX, lastColumn, 3, this.x, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
				if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
						|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
					value += 0.5 + this.foo(gameBoard, lastX, lastColumn);
				} else {
					value += -0.5;
				}
			}
			if ((result = ConnectedChecking.checkCrossPositionsUp(gameBoard,
					lastX, lastColumn, 3, this.x, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
				if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
						|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
					value += 0.5 + this.foo(gameBoard, lastX, lastColumn);
				} else {
					value += -0.5;
				}
			}
			if ((result = ConnectedChecking.checkHorizontalPositions(gameBoard,
					lastX, lastColumn, 3, this.y)) != IGameLogic.Winner.NOT_FINISHED) {
				if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
						|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
					value += 0.5 + this.foo(gameBoard, lastX, lastColumn);
				} else {
					value += -0.5;
				}
			}
			if ((result = ConnectedChecking.checkVerticalPositions(gameBoard,
					lastX, lastColumn, 3)) != IGameLogic.Winner.NOT_FINISHED) {
				if ((result == IGameLogic.Winner.PLAYER1 && this.playerID == 1)
						|| (result == IGameLogic.Winner.PLAYER2 && this.playerID == 2)) {
					value += 0.5 + this.foo(gameBoard, lastX, lastColumn);
				} else {
					value += -0.5;
				}
			}
		}
		if (value != 0) {
			return value;
		} else {
			return this.foo(gameBoard, lastX, lastColumn);
		}
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
