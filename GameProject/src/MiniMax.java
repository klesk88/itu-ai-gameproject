import java.util.Iterator;
import java.util.LinkedList;



public class MiniMax{
	
	private int x = 0;//coloumns
	private int y = 0;//raws
	private int playerID=0;	
	private LinkedList <Integer> list_of_action = new LinkedList<Integer>();
	
	public MiniMax(int x, int y, int playerID){
		
		this.x = x;
		this.y = y;
		this.playerID = playerID;
	}
	
	private class Values{
		
		int coloumns=0;
		int rows=0;
		
		protected Values(int x, int y){
			
			coloumns=x;
			rows=y;
		}
	}
	
	
	
	private boolean terminalState(int[][]gameboard){
	
		return false;
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
	//private utility(int[][] gameboard, )
	public int maxDecision(int[][] gameboard, int alpha, int beta){
		
		if(terminalState(gameboard)){
			
			//return Utility(state,player(state));
		}
		//assign the min value of the integers to this variable
		int v = Integer.MIN_VALUE;
		actions(gameboard);
		for (int a:list_of_action){
			
			v = Math.max(v,minDecision(result(gameboard,a),alpha,beta));
			if(v>=beta){
				return v;
			}
			
			alpha = Math.max(alpha, v);
		}
		
		return v;
	}
	
	public int minDecision(int[][] gameboard, int alpha, int beta){
		
		int v=0;
		
		return v;
	}
	
	private int[][]result(int[][] gameboard, int a){
		int[][] copy_gameboard = new int[x][y];
		for(int i=0; i<x;i++){
			for (int j=0;j<y;j++){
				//if i'm in the position point by the action i update the table
				if(i==a && gameboard[i][j] == Integer.MIN_VALUE){
					
					copy_gameboard[i][j]=playerID;
				}else{//if not i copy the data
					copy_gameboard[i][j] = gameboard[i][j];
				}
			}
		}
		
		return copy_gameboard;
	}

}
