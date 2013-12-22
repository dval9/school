/**
 *Tom Crowfoot
 *10037477
 *CPSC 219 Assignment 5
 *Version 0.5
 */
 import java.util.Random;
//subclass of goodguy, computer controlled
//constants for HIDE_TERRAIN MAX_HIT_POINTS EAST SOUTH moves east until edge, then south
//attack method, method that determines if hobbits get hit
public class Hobbit extends GoodGuy{

    public static final char DEFAULT_APPEARANCE='h';
	public final int MAX_HIT_POINTS=10;
	public final int EAST=6;
	public final int SOUTH=2;
	public final int MAX_DAMAGE=6;
	public final int MIN_DAMAGE=1;
	private Random aRandom;
	private int damage;

    public Hobbit (char anAppearance, int aRow, int aColumn){
		super(anAppearance,aRow,aColumn);
		setHitPoints(MAX_HIT_POINTS);
		aRandom=new Random();
    }
	
	//returns hobbit damage
	public int attack(){
		damage=aRandom.nextInt(MAX_DAMAGE)+MIN_DAMAGE;
		if(Mode.debug)
			System.out.println(">>>hobbit attacks "+damage+" damage");
		return damage;
	}
	
	//subtracts hobbit health if hit, 50/50 chance to dodge
	public void deductDamage(int damage){
		if(aRandom.nextBoolean()){
			if(Mode.debug)
				System.out.println(">>>rolled true so hobbit takes no damage");
			System.out.println("Hobbit is hidden, and takes no damage.");
		}
		else{
			if(Mode.debug)
				System.out.println(">>>hp before "+hitPoints);
			System.out.println("Hobbit suffers "+damage+" points of damage!");
			hitPoints=hitPoints-damage;
			if(Mode.debug)
				System.out.println(">>>hp after "+hitPoints);
		}
	}
}