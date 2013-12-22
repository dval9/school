/**
 *Tom Crowfoot
 *10037477
 *CPSC 219 Assignment 5
 *Version 0.5
 */
 import java.util.Random;
//subclass of humanoid, computer controlled, elite enemy
//constant MAX_HIT_POINTS DEFAULT RANGE
//attack method, randomly decide who to attack
public class UrukHai extends Humanoid{
    
	public static final char DEFAULT_APPEARANCE='U';
	public final int MAX_HIT_POINTS=14;
	public final int DEFAULT_RANGE=2;
	public final int MAX_DAMAGE=9;
	public final int MIN_DAMAGE=3;
	private Random aRandom;
	private int damage;

    public UrukHai(char anAppearance,int aRow,int aColumn){
		super(anAppearance,aRow,aColumn);
		setHitPoints(MAX_HIT_POINTS);
		setRange(DEFAULT_RANGE);
		aRandom=new Random();
    }
	
	//returns urukhai attack damage
	public int attack(){
		damage=aRandom.nextInt(MAX_DAMAGE-MIN_DAMAGE+1)+MIN_DAMAGE;
		if(Mode.debug)
			System.out.println(">>>urukhai at "+getRow()+","+getColumn()+" attacks for "+damage);
		return damage;
	}
	
	//similar to move() that other objects use to attack, but has larger range so more options
	public Coordinate attackAt(){
		int direction;
		Coordinate aCoordinate;
		direction=aRandom.nextInt(25)+1;
		aCoordinate=positionToAttack(direction);
		return aCoordinate;
    }

	//similar to positionToMove() that other objects use to attack, but more options due to larger range
    public Coordinate positionToAttack(int position){
		Coordinate destination=new Coordinate();
		switch(position){
			case 1:
				destination.setRow(getRow()+1);
				destination.setColumn(getColumn()-1);
			break;
			case 2:
				destination.setRow(getRow()+1);
				destination.setColumn(getColumn());
			break;
			case 3:
				destination.setRow(getRow()+1);
				destination.setColumn(getColumn()+1);
			break;
			case 4:
				destination.setRow(getRow());
				destination.setColumn(getColumn()-1);
			break;
			case 5:
				destination.setRow(getRow());
				destination.setColumn(getColumn());
			break;
			case 6:
				destination.setRow(getRow());
				destination.setColumn(getColumn()+1);
			break;
			case 7:
				destination.setRow(getRow()-1);
				destination.setColumn(getColumn()-1);
			break;
			case 8:
				destination.setRow(getRow()-1);
				destination.setColumn(getColumn());
			break;
			case 9:
				destination.setRow(getRow()-1);
				destination.setColumn(getColumn()+1);
			break;
			case 10:
				destination.setRow(getRow()+range);
				destination.setColumn(getColumn()-range);
			break;
			case 11:
				destination.setRow(getRow()+range);
				destination.setColumn(getColumn());
			break;
			case 12:
				destination.setRow(getRow()+range);
				destination.setColumn(getColumn()+range);
			break;
			case 13:
				destination.setRow(getRow());
				destination.setColumn(getColumn()-range);
			break;
			case 14:
				destination.setRow(getRow());
				destination.setColumn(getColumn()+range);
			break;
			case 15:
				destination.setRow(getRow()-range);
				destination.setColumn(getColumn()-range);
			break;
			case 16:
				destination.setRow(getRow()-range);
				destination.setColumn(getColumn());
			break;
			case 17:
				destination.setRow(getRow()-range);
				destination.setColumn(getColumn()+range);
			break;
			case 18:
				destination.setRow(getRow()+range);
				destination.setColumn(getColumn()-1);
			break;
			case 19:
				destination.setRow(getRow()+range);
				destination.setColumn(getColumn()+1);
			break;
			case 20:
				destination.setRow(getRow()+1);
				destination.setColumn(getColumn()-range);
			break;
			case 21:
				destination.setRow(getRow()+1);
				destination.setColumn(getColumn()+range);
			break;
			case 22:
				destination.setRow(getRow()-1);
				destination.setColumn(getColumn()-range);
			break;
			case 23:
				destination.setRow(getRow()-1);
				destination.setColumn(getColumn()+range);
			break;
			case 24:
				destination.setRow(getRow()-range);
				destination.setColumn(getColumn()-1);
			break;
			case 25:
				destination.setRow(getRow()-range);
				destination.setColumn(getColumn()+1);
			break;
		}
		return destination;
    }
}