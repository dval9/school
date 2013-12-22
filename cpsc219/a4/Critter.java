/** Tom Crowfoot
*	10037477
*	Version 1.0
*	This class stores data on what kind of critter is at a location.
*/
class Critter{
    public static final char REGULAR='*';
    public static final char EMPTY=' ';
    public static final char FERTILE='!';
    private char appearance;

    public Critter(){
		appearance=REGULAR;
    }

    public Critter(char ch){
		appearance=ch;
    }

    public char getAppearance(){
		return appearance;
    } 

    public void setAppearance(char newAppearance){
		if((newAppearance==EMPTY)||(newAppearance==REGULAR)||(newAppearance==FERTILE))
			appearance=newAppearance;
		else
			System.out.println("Critter's appearance must be either a star, space or exclaimation");
    } 

    public void display(){
		System.out.print(appearance);
    }
}