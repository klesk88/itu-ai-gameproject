import java.util.Iterator;
import java.util.LinkedList;

public class MiniMax{
	
	private int x = 0;//coloumns
	private int y = 0;//raws
	private int playerID=0;	
	
	
	public MiniMax(int x, int y, int playerID){
		
		this.x = x;
		this.y = y;
		this.playerID = playerID;
	}

	

	
	private LinkedList<Integer> actions(int[][]gameboard){		
		LinkedList <Integer> list_of_action = new LinkedList<Integer>();
		
		for (int i=0; i<x; i++ ){				
					//if there isn't no one in that poisition i put a coin inside that and i change coloumn
					if(gameboard[i][y-1]==Integer.MIN_VALUE){
						//copy_gameboard[i][j] = playerID; // result
						//list_of_action.add(i);//add the coloumn to the list
						list_of_action.add(i);					
					}
		}	
		return list_of_action;// i don't never have to came here 
	}

	public int miniMax(int[][] gameboard){
		
		int v = maxDecision(gameboard,Integer.MIN_VALUE,Integer.MAX_VALUE);
		return v;
	}
	
	private int maxDecision(int[][] gameboard, int alpha, int beta){
		
		if(TerminalState(gameboard)){
			
			return Utility(gameboard,IGameLogic.Winner.PLAYER2);
		}
		// assign the min value of the integers to this variable
		int v = Integer.MIN_VALUE;
		//actions(gameboard);
		for (int a : actions(gameboard)) {

			v = Math.max(v, minDecision(result(gameboard, a), alpha, beta));
			if (v >= beta) {
				return v;
			}

			alpha = Math.max(alpha, v);
		}

		return v;
	}
	
	private int minDecision(int[][] gameboard, int alpha, int beta){
		
		if(TerminalState(gameboard)){
			
			return Utility(gameboard,IGameLogic.Winner.PLAYER2);
		}
		// assign the min value of the integers to this variable
		int v = Integer.MAX_VALUE;
		//actions(gameboard);
		for (int a : actions(gameboard)) {

			v = Math.min(v, maxDecision(result(gameboard, a), alpha, beta));
			if (v <= alpha) {
				return v;
			}

			beta = Math.min(beta, v);
		}

		return v;
	}

	private int[][] result(int[][] gameboard, int a) {
		int[][] copy_gameboard = new int[x][y];
		int d=0;
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				// if i'm in the position point by the action i update the table
				if (i == a && gameboard[i][j] == Integer.MIN_VALUE && d==0) {
					d++;
					copy_gameboard[i][j] = playerID;
				} else {// if not i copy the data
					copy_gameboard[i][j] = gameboard[i][j];
				}
			}
		}

		return copy_gameboard;
	}

	public int Utility(int[][] gameBoard, IGameLogic.Winner player) {
		IGameLogic.Winner result;
		if (this.checkTie(gameBoard) == IGameLogic.Winner.TIE) {
			return 0;
		} else if ((result = this.checkCrossPositions(gameBoard, player)) != IGameLogic.Winner.NOT_FINISHED) {
			if (result == IGameLogic.Winner.PLAYER1 && this.playerID == 1) {
				return 1;
			} else {
				return -1;
			}
		} else if ((result = this.checkHorizontalPositions(gameBoard, player)) != IGameLogic.Winner.NOT_FINISHED) {
			if (result == IGameLogic.Winner.PLAYER1 && this.playerID == 1) {
				return 1;
			} else {
				return -1;
			}
		} else if ((result = this.checkVerticalPositions(gameBoard, player)) != IGameLogic.Winner.NOT_FINISHED) {
			if (result == IGameLogic.Winner.PLAYER1 && this.playerID == 1) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;
	}

	private boolean TerminalState(int[][] gameBoard) {
		IGameLogic.Winner result;
		IGameLogic.Winner player = IGameLogic.Winner.PLAYER2;
		if (this.checkTie(gameBoard) == IGameLogic.Winner.TIE) {
			return true;
		} else if ((result = this.checkCrossPositions(gameBoard, player)) != IGameLogic.Winner.NOT_FINISHED) {
			return true;
		} else if ((result = this.checkHorizontalPositions(gameBoard, player)) != IGameLogic.Winner.NOT_FINISHED) {
			return true;
		} else if ((result = this.checkVerticalPositions(gameBoard, player)) != IGameLogic.Winner.NOT_FINISHED) {
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
		for (int i = 0; i < this.x; i++) {
			if (gameBoard[i][this.y - 1] == Integer.MIN_VALUE) {
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
					return player;
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
					return player;
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
						return player;
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
						return player;
					}
				}
			}
		}
		return IGameLogic.Winner.NOT_FINISHED;
	}
}
