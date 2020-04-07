//Matthew Wulz
//EightQueen.java

import java.util.*;
import java.lang.Math;

public class EightQueen {

	/**
	 * main: grabs the parameters and performs the needed FlagXXX function
	 * this method also runs the Flag100 method internally
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2 && args.length != 3) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        int flag = Integer.valueOf(args[0]);
        int seed = -1;
        String board = "";
        
        if (args.length == 2) {
        	board = args[1];
        	seed = -1;
        	
        }else if(args.length == 3) {
        	board = args[2];
        	seed = Integer.valueOf(args[1]);
        }
        
        if (flag == 100) {
        	System.out.println(heuristic(board));
    	}else if (flag == 200) {
    		Flag200(board);
    	}else if (flag >= 300 && flag <400) {
    		int cutoff = Math.abs(300-flag);
    		Flag300(seed,cutoff,board);
    	}else if (flag >= 400 && flag < 500) {
    		int cutoff = Math.abs(400-flag);
    		Flag400(seed,cutoff,board);
    	}else if (flag >= 500) {
    		int cutoff = Math.abs(500-flag);
    		Flag500(seed,cutoff,board);
    	}
        

	}
	
	/**
	 * heuristic: Calculates the heuristic function for the given board
	 * 
	 * @param board
	 * @return
	 */
	private static int heuristic(String board) {
		int cost = 0;

		for (int i = 0; i < board.length(); i++) {

			int col1 = i;
			int row1 = Integer.parseInt(board.substring(i, i + 1));

			for (int j = 0; j < board.length(); j++) {
				
				if (i != j) {
					int col2 = j;
					int row2 = Integer.parseInt(board.substring(j, j + 1));

					if (col1 == col2) {
						cost++;
					}
					if (row1 == row2) {
						cost++;
					}
					if (row1 + col1 == row2 + col2) {
						cost++;
					}
					if (row1 - col1 == row2 - col2) {
						cost++;
					}
				}
			}
		}
		
		return cost/2;
	}
	
	/**
	 * findSuccessors: finds successors to the initialState of board
	 * 
	 * @param board
	 * @return
	 */
	private static ArrayList<String> findSuccessors(String board){
		ArrayList<String> successors = new ArrayList<String>();
		
		int minHeuristic = heuristic(board);
		
		String newBoard = "";
		for (int i = 0; i < board.length(); i++) {
			
			for (int j = 0; j < board.length(); j++) {
				
				newBoard = board.substring(0, i) + j;
			
				if (i != 7) {
					newBoard += board.substring(i + 1);
				}
				
				if (!newBoard.equals(board)) {
					
					if (minHeuristic == heuristic(newBoard)) {
						
						minHeuristic = heuristic(newBoard);
						successors.add(newBoard);
						
					} else if (minHeuristic > heuristic(newBoard)) {
						
						successors = new ArrayList<String>();
						minHeuristic = heuristic(newBoard);
						successors.add(newBoard);
					}
				}
			}
		}
		
		return successors;
	}
	
	/**
	 * findFirstMove: used in some of the later steps to find the next move
	 * 
	 * @param board
	 * @return
	 */
	private static String findFirstMove(String board){
		int minHeuristic = heuristic(board);

		String newBoard = "";
		for (int i = 0; i < board.length(); i++) {
			
			for (int j = 0; j < board.length(); j++) {
				
				newBoard = board.substring(0, i) + j;

				if (i != 7) {
					newBoard += board.substring(i + 1);
				}

				if (!newBoard.equals(board)) {
					if (minHeuristic > heuristic(newBoard)) {
						return newBoard;
					}
				}
			}
		}

		return board;
	}
	
	/**
	 * Flag200: prints out the successors with the heuristic
	 * 
	 * @param board
	 */
    private static void Flag200(String board) {
	
    	ArrayList<String> s = findSuccessors(board);
    	for (int i = 0; i < s.size(); i++) {
    		
    		System.out.println(s.get(i));
    	}
    	
    	if (s.size()!= 0) {
    		System.out.println(heuristic(s.get(0)));
    	}
	}

    /**
     * Flag300: uses the hill climbing algo with some random ints
     * 
     * @param seed
     * @param cutoff
     * @param board
     */
	private static void Flag300(int seed, int cutoff, String board) {
		ArrayList<String> successors = new ArrayList<String>();
		
		Random rng = new Random();
		if (seed != -1) rng.setSeed(seed);
		
		System.out.println("0:" + board + " " + heuristic(board));
		if (heuristic(board) == 0) {
			System.out.print("Solved");
			return;
		}
		for (int i = 0; i < cutoff; i++) {
			if (heuristic(board) == 0) {
				System.out.print("Solved");
				break;
			}
			successors = findSuccessors(board);
			
			board = successors.get(rng.nextInt(successors.size()));
			System.out.println((i + 1) + ":" + board + " " + heuristic(board));
		}
	}

	/**
	 * Flag400: the greedy hill cimbing algo
	 * 
	 * @param seed
	 * @param cutoff
	 * @param board
	 */
	private static void Flag400(int seed, int cutoff, String board) {
		ArrayList<String> successors = new ArrayList<String>();
		
		String initalBoard = board; 
		String lastMove = board; 
		
		System.out.println("0:" + board + " " + heuristic(board));
		if (heuristic(board) == 0) {
			System.out.print("Solved");
			return;
		}
		for (int i = 0; i < cutoff; i++) {
			if (heuristic(board) == 0) {
				System.out.print("Solved");
				break;
			}
			successors = findSuccessors(board);
			
			board = findFirstMove(board);
			if (board == lastMove) {
				System.out.println("Local optimum");
				break;
			}
			lastMove = board; 
			System.out.println((i + 1) + ":" + board + " " + heuristic(board));
		}
		
		
	}
    
	/**
	 * Flag500: performs the simulated annealing
	 * 
	 * @param seed
	 * @param cutoff
	 * @param board
	 */
	private static void Flag500(int seed, int cutoff, String board) {
		
		/*for t = 1 to  do
			T = Schedule(t) ; // T is the current temperature, which is
			monotonically decreasing with t
			if T=0 then return current ; //halt when temperature = 0
			next = Select-Random-Successor-State(current)
			deltaE = f(next) - f(current) ; // If positive, next is better
			than current. Otherwise, next is worse than current.
			if deltaE > 0 then current = next ; // always move to a
			better state
			else current = next with probability p = exp(deltaE /
			T) ; // as T  0, p  0; as deltaE  -, p 0
			end*/
		
		//t == temp
		
			
		Random rng = new Random();
		if (seed != -1) rng.setSeed(seed);
		
		double temp = 100;
		double annealRate = .95;

		System.out.println("0:" + board + " " + heuristic(board));
		
		if (heuristic(board) == 0) {
			System.out.print("Solved");
			return;
		}
		
		//System.out.println(cutoff);
		for (int i = 0; i < cutoff; i++) {
			if (heuristic(board) == 0) {
				System.out.print("Solved");
				break;
			}
			
			temp *= annealRate;
			if (temp == 0) {
				break;
			}
			
			int index = rng.nextInt(7);
			int value = rng.nextInt(7);
			double prob = rng.nextDouble();
			
			String nextBoard = "";
			
			nextBoard = board.substring(0, index) + value;
			
			if (index != 7) {
				nextBoard += board.substring(index + 1);
			}
			
			int deltaE = heuristic(nextBoard) - heuristic(board);
			
			if (deltaE < 0) {
				board = nextBoard;
			}else {
				if (prob < Math.exp(-deltaE / temp)) {
					board = nextBoard;
				}
			}
			System.out.println((i + 1) + ":" + board + " " + heuristic(board));
			
		}
	}
}
