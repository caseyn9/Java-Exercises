import java.util.Scanner;

public class Sokoban {

    static int R, C, destR, destC, answer;
    static int[][] arr;
    static int[] box = new int[2];
    static boolean finished = false;
    static final int WALL = -1;
    static final int BOX = 1;
    static final int SPACE = 0;
    

    static void read()throws Exception {
        Scanner sin = new Scanner(System.in);
        R = sin.nextInt();
        C = sin.nextInt();
        arr = new int[R + 2][C + 2];
        for (int i = 0; i < R + 2; i++) {
            for (int j = 0; j < C + 2; j++) {
                arr[i][j] = -1;
            }
        }
        sin.nextLine();
        for (int i = 1; i <= R; i++) {
            String tmp = sin.nextLine();
            for (int j = 1; j <= C; j++) {
                char ch = tmp.charAt(j-1);
                switch (ch) {
                    case '#':
                        arr[i][j] = -1;
                        break;
                    case 'B':
                    	box[0] = i; box[1] = j;
                        arr[i][j] = 1;
                        break;
                    case 'D':
                        destR = i;
                        destC = j;
                    default:
                        arr[i][j] = 0;
                }
            }
        }
        //sin.close();
    }

    
    public static void main(String[] args)throws Exception {
        read();
        answer = 0;
       // toStringg();
        int result = navigate();
        //System.out.println("result " + result);
        System.out.println(answer);
    }	
    
    public static int navigate() {
    	int total = 0;
    	int boxX = box[0]; 
		int boxY = box[1];
    	for(int i=1; i<5; i++){
    		if(box[0] == destR && box[1] == destC){
				return total;
			}
    		//move box next pos
    		if(moveBox(i, box[1], box[0], 0)){
    				total += navigate(boxX,boxY, i, 0);
    		}
    	}
    	return total;
	}


	private static int navigate(int prevX, int prevY, int prevDir, int count){
		int tmpCount = ++count;
    	int[][] array = arr;
    	int[] boxArray = box;
		int total = 0;
    	int boxX = box[0]; 
		int boxY = box[1];
    	for(int i=1; i<5; i++){
    		//check if dest reached
			if(box[0] == destR && box[1] == destC){
				if(answer == 0){
					answer = tmpCount;
				}
				return total + 1;
			}
    		//move box next pos
    		if(moveBox(i, box[1], box[0], prevDir)){
    			//System.out.println(" current count " + tmpCount);
    				total += navigate(boxX,boxY, i, tmpCount);
    		}
    	}
    	if(box[0] == destR && box[1] == destC){
			return total;
		}
    	//dead end, go back
    	box[0] = prevX; 
		box[1] = prevY;
		return 0;
    }
	
	public static String toStringg(){
		System.out.println("\n");
		for(int i=1; i<arr.length-1; i++){
			for(int j=1; j<arr[0].length-1; j++){
				String tmp="";
				if(arr[i][j] == -1)
					tmp = "#";
				else tmp = "@";
				if(box[0] == i && box[1] == j)
					tmp = "B";
				if(i== destR && j== destC)
					tmp = "D";
				System.out.print(tmp);
			}
			System.out.print("\n");
		}
		return null;
		
	}
    
    																				//	1
    public static boolean moveBox(int direction, int boxX, int boxY, int prevDir){	//4	  2				
    	//1=up; 2=right; 3=down; 4=left;
    	//check for collision															3
    	
    	int[] boxArray = box;
    	if((direction == 1) || (direction == 3 )){
    		if(arr[boxY + 1][boxX] == WALL || arr[boxY-1][boxX] == WALL){
    			return false;
    		}
    	}
    	if((direction == 4)|| (direction == 2 )){
    		if(arr[boxY][boxX+1] == WALL || arr[boxY][boxX-1] == WALL){
    			return false;
    		}
    	}
    	
    	switch(direction){
    	case 1: if(prevDir == 3) return false;
    			arr[boxY - 1][boxX] = BOX;
    			box[1] = boxX; box[0] = boxY-1;
    			break;
    	case 2: if(prevDir == 4) return false;
    			arr[boxY][boxX+1] = BOX;
    			box[1] = boxX+1; box[0] = boxY;
				break;
    	case 3: if(prevDir == 1) return false;
    			arr[boxY+1][boxX] = BOX;
    			box[1] = boxX; box[0] = boxY+1;
				break;	
    	case 4: if(prevDir == 2) return false;
    			arr[boxY][boxX-1] = BOX;
    			box[1] = boxX-1; box[0] = boxY;
				break;	
    	}
    	boxArray = box;
		arr[boxY][boxX] = SPACE;
		//toStringg();
		return true;
    }
}