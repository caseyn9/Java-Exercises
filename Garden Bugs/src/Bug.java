
public class Bug {
	int[] currPos;
	int strength, sleepTime;
	char direction;
	boolean asleep, dead;

	Bug(int [] currPos, int sleepTime, char direction){
		this.currPos = currPos;
		this.sleepTime = sleepTime;
		this.direction = direction;
		asleep = true;
		dead = false;
		strength = 0;
	}
	
	/*
	abc
	d e
	fgh
	*/
	int[] getNextPos(){
		int[] pos = currPos;
		switch (direction){
		case 'A': 
			pos[0] = pos[0] -1;
			pos[1] = pos[1] -1;	
			break;
		case 'B':
			pos[0] = pos[0] -1;
			pos[1] = pos[1] -0;	
			break;
		case'C':
			pos[0] = pos[0] -1;
			pos[1] = pos[1] +1;	
			break;
		case 'D':
			pos[0] = pos[0] +0;
			pos[1] = pos[1] -1;	
			break;
		case 'E':
			pos[0] = pos[0] +0;
			pos[1] = pos[1] +1;	
			break;
		case 'F':
			pos[0] = pos[0] -1;
			pos[1] = pos[1] +1;	
			break;
		case 'G':
			pos[0] = pos[0] +1;		//y
			pos[1] = pos[1] +0;		//x	
			break;
		default:
			pos[0] = pos[0] +1;
			pos[1] = pos[1] +1;	
		}
		return pos;
		
	}
	
	void fight(Bug enemy){
		if (getStrength()>enemy.getStrength()){
			enemy.die();
		}
		else die();
	}
	
	void die(){
		asleep = true;
		dead = true;
	}

	int getStrength(){
		return strength;
	}

	void wake(int currTime){
		if(!dead && currTime >= sleepTime)
			asleep = false;
	}

	int[] getPos(){
		return currPos;
	}
	
	void move(){
		if(!asleep)
			currPos = getNextPos();
	}

	boolean isDead(){
		return dead;
	}
	
	boolean isAsleep(){
		return asleep;
	}

	void levelUp(){
		strength++;
	}
}
