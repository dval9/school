/**
 *Tom Crowfoot
 *10037477
 *CPSC 219 Assignment 5
 *Version 0.5
 */
 import java.util.Random;
//subclass of goodguy, controlled by player
//constants MAX_HIT_POINTS 
//attack method
public class Human extends GoodGuy{
   
	public static final char DEFAULT_APPEARANCE='B';
	public final int MAX_HIT_POINTS=20;
	public final int MAX_DAMAGE=13;
	public final int MIN_DAMAGE=4;
	private Random aRandom;
	private int damage;
	
    public Human(char anAppearance,int aRow,int aColumn){
		super(anAppearance,aRow,aColumn);
		setHitPoints(MAX_HIT_POINTS);
		aRandom=new Random();
    }
	
	//returns human damage
	public int attack(){
		damage=aRandom.nextInt(MAX_DAMAGE-MIN_DAMAGE+1)+MIN_DAMAGE;
		if(Mode.debug)
			System.out.println(">>>player attacks "+damage+" damage");
		return damage;
	}
	
	//subtracts humans health, does not subtract damage if cheat mode is on
	public void deductDamage(int damage){
		if(Mode.cheat){
			if(Mode.debug)
				System.out.println(">>>Cheat mode is enabled, so you take no damage.");
		}
		else{
			if(Mode.debug)
				System.out.println(">>>hp before "+hitPoints);
			System.out.println("You suffer "+damage+" points of damage!");
			hitPoints=hitPoints-damage;
			if(Mode.debug)
				System.out.println(">>>hp after "+hitPoints);
		}
	}
}