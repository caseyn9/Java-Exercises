import java.util.Scanner;

public class Garden {

	public static void main(String[] args) {
		int gardX, gardY;
		int time;
		int noBugs;
		
		Scanner scanner = new Scanner(System.in);
		//enter input.
		gardX = scanner.nextInt();
		gardY = scanner.nextInt();
		System.out.println("time next");
		time = scanner.nextInt();
		noBugs = scanner.nextInt();
		
		
		boolean [][] plants = new boolean[gardX][gardY];
		//populate plants
		for(int i=0; i<plants.length; i++){
			for(int j=0; j<plants[0].length; j++){
				plants[i][j] = true;
			}
		}
		Bug[] bugs = new Bug[noBugs];
		createBugs(bugs);
		int[] result = simulate(bugs, plants, time);
		System.out.println(result[0] +" " + result[1]);
	}
	//TODO re-write to work for x number of bugs and to wake up bugs.
	static void createBugs(Bug[] bugs){
		Scanner scanner = new Scanner(System.in);
		int startX;
		int startY;
		int time;
		char direction;
		for(int i=0 ;i<bugs.length; i++){
			startX = scanner.nextInt();
			startY = scanner.nextInt();
			time = scanner.nextInt();
			direction = scanner.next().charAt(0);
			int[] tmp = {startX, startY};
			bugs[i] = new Bug(tmp,time,direction);
		}
		scanner.close();
	} 
	
	static void moveBugs(Bug[] bugs, boolean[][] plants, int currTime){
		for(int i=0; i<bugs.length;i++){
			if(!bugs[i].isDead()){
				//System.out.println("I should be moving");
				Bug currBug = bugs[i];
				checkCollision(bugs, currBug, i);
				bugs[i].move();
				currBug.wake(currTime);
				checkIfDead(currBug, plants);
				infect(bugs[i],plants);
			}
		}
	}
	
	private static void infect(Bug bug, boolean[][] plants) {
		int[] pos = bug.getPos();
		int x = pos[0]; int y = pos[1];
		if(!bug.isAsleep()){
			// System.out.println( "" + x + ","+ y);
			if(plants[x][y]){
				plants[x][y] = false;
				bug.levelUp();
			}
		}
		
	}
	private static void checkCollision(Bug[] bugs, Bug currBug, int bugIndex){
		for(int i=0; i<bugs.length; i++){
			if(!bugs[i].isAsleep() && currBug.getPos()[0] == bugs[i].getPos()[0] && currBug.getPos()[1] == bugs[i].getPos()[1] && i != bugIndex){
				if(currBug.getStrength() > bugs[i].getStrength()){
					bugs[i].die();
				}
				else{
					currBug.die();
				}
			}
		}
	}
	
	static int [] simulate(Bug[] bugs, boolean[][] plants, int time){
		for(int i=0; i<bugs.length; i++){
			infect(bugs[i], plants);
		}
		for(int i=0; i<time; i++){
			moveBugs(bugs, plants,i);
			stringMe(plants, bugs);
		}
		int[] results = {countPlants(plants), countBugs(bugs)};
		return results;
	}
	private static void checkIfDead(Bug bug, boolean[][] plants) {
		if((bug.getPos()[0]>plants.length-1) || (bug.getPos()[1]>plants[0].length-1) || bug.getPos()[0] < 0 || bug.getPos()[1] < 0){
			//System.out.println(bug.getPos()[0] +"," + bug.getPos()[1] + "   " + plants.length + " kill me");
			bug.die();
		}
		
	}

	static int countPlants(boolean[][]plants){
		int result=0;
		for(int i=0; i<plants.length; i++){
			for(int j=0; j<plants[i].length; j++){
				if(plants[i][j]){
					result++;
				}
			}
		}
		return result;
	}

	static int countBugs(Bug[] bugs){
		int result = 0;
		for(int i=0; i< bugs.length; i++){
			if(!bugs[i].isDead()){
				result++;
			}
		}
		return result;
	}
	
	static void stringMe(boolean[][] plants, Bug[] bugs){
		//System.out.println(" ");
		for(int i=0; i<plants.length; i++){
			for(int j=0; j<plants[i].length; j++){
				if(plants[i][j]){
					System.out.print("0");
					}
				else{
					System.out.print("X");
				}
			}
			System.out.println("");
		}
		System.out.println(" ");
	}
}
