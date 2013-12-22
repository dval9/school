/**
 *Tom Crowfoot
 *10037477
 *CPSC 219 Assignment 5
 *Version 0.5
 */
 import java.util.Random;
//subclass of humanoid, computer controlled, weak enemy
//attack method, randomly attack
public class Goblin extends Humanoid{

    public static final char DEFAULT_APPEARANCE='g';
	public final int MAX_DAMAGE=8;
	public final int MIN_DAMAGE=1;
	private Random aRandom;
	private int damage;
   
    public Goblin(char anAppearance,int aRow,int aColumn){
        super(anAppearance,aRow,aColumn);
		aRandom=new Random();
    }
	
	//returns gob attack damage
	public int attack(){
		damage=aRandom.nextInt(MAX_DAMAGE)+MIN_DAMAGE;
		if(Mode.debug)
			System.out.println(">>>goblin at "+getRow()+","+getColumn()+" attacks for "+damage);
		return damage;
	}
}