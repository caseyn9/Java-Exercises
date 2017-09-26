import java.util.Scanner;
import java.lang.Math;

public class GoldHunt {
	static String[][] board;
	static int[] players;

	public static void main(String[] args) {
		
		int nRows,nCols, nPlayers;
		Scanner scanner = new Scanner(System.in);
		nRows = scanner.nextInt();
		nCols = scanner.nextInt();
		scanner.nextLine();
		
		String[] rows = new String[nRows];
		for(int i=0; i<rows.length; i++){
			rows[i] = scanner.nextLine();
			//System.out.println(rows[i]);
		}
		nPlayers = scanner.nextInt();

		//every 3 elements is 1 player
		players = new int[nPlayers*3];
		for(int i=0; i< players.length; i+=3){
			players[i]  =scanner.nextInt();
			players[i+1] = scanner.nextInt();
			players[i+2] = scanner.nextInt();
		}
		board = new String[nRows][nCols];
		
		//populate the board
		for(int i=0; i<board.length; i++){
			int offset = 0;
			String[] tmp = rows[i].split(" ");
			//System.out.println("");
			for(int j=0; j<board[0].length; j++){
				String element = tmp[j+offset];
				//System.out.print(" printing " + tmp[j+offset]);
				if(tmp[j+offset].charAt(0) == 'D'){
					element += "/" + tmp[j+1 + offset];
					element += "/" + tmp[j+2 + offset];
					offset += 2;
							
				}
				board[i][j] = element;
			}
		}
		
		int winner = movePlayers();
		System.out.print(winner);
	}
		
	public String toString(){
		for(int i=0; i<board.length; i++){
			System.out.println("");
			for(int j=0; j<board[0].length; j++){
				System.out.print(board[i][j] + " ");
			}
		}
		return "";
	}

	
	
	public static int movePlayers(){
		//players, board
		int[] playerrs = players;
		String[][] boord = board;
		while(true){
			for(int i=0; i<players.length; i+=3){
				if(i>=0){
					int x = players[i+1];
					int y = players[i+2];
					int xLenght = board.length;
					int yLength = board[0].length;
					if(board[x][y] .equals("G")){
						return players[i];
					}
					else if(board[x][y].charAt(0) == 'D'){
						//System.out.println("moving!");
						String[] tmp = board[x][y].split("/");
						players[i+1] +=Integer.parseInt(tmp[1]);
						players[i+2] += Integer.parseInt(tmp[2]);
						if(players[i+1] >= board.length || players[i+1] < 0){
							players[i+1] = Math.floorMod(players[i+1],board.length);
						}
						if(players[i+2] >= board[0].length || players[i+2] < 0){
							players[i+2] = Math.floorMod(players[i+2],board[0].length);
						}
					}

				}
			}
		}
	}

}
