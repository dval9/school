/** Tom Crowfoot
*	10037477
*	Version 1.0
*	This class will list the menu, take the users input, and create a biosphere of the users choosing.
*       Debug mode can be turned on by entering 'd'.
*/
import java.util.Scanner;
public class UserInterface{
   
    private String choice;
	private boolean valid;
	private boolean run;
	private Biosphere aBiosphere;
	
	public UserInterface(){
		run=true;
		aBiosphere=new Biosphere();
	}

	// starts program loop
    public void start(){
		intro();
		while(run){
			valid=false;
			getInput();
			quit();
			toggleDebug();
			runBiosphere();
			if(!valid)
				System.out.println("That is not a valid input, try again.");
		}
	}
	
	// displays intro menu for user
	public void intro(){
		System.out.println("Pick the starting Biosphere:");
		System.out.println("\t1: All empty.");
		System.out.println("\t2: One critter.");
		System.out.println("\t3: Single birth.");
		System.out.println("\t4: Simple pattern in middle.");
		System.out.println("\t5: Pattern near edges.");
		System.out.println("\t6: Fertile critters.");
		System.out.println("\t7: Complex pattern 1: no fertile critters.");
		System.out.println("\t8: Complex pattern 2: fertile critters.");
		System.out.println("\tQ: Quit program.");
    }
	
	// gets input in form of a string
	public void getInput(){
		System.out.print("Enter your selection: ");
		Scanner in=new Scanner(System.in);
        choice=in.nextLine();
    }
	// toggles debug mode	
	public void toggleDebug(){
		if(choice.equalsIgnoreCase("D")){
			if(Mode.debug==false){
				System.out.println("Debug mode is now on.");
				Mode.debug=true;
				valid=true;
			}
			else if(Mode.debug==true){
				System.out.println("Debug mode is now off.");
				Mode.debug=false;
				valid=true;
			}
		}
	}
	
	// quits program
	public void quit(){
		if(choice.equalsIgnoreCase("Q")){
			System.out.println("Program is quitting.");
			run=false;
			valid=true;	
		}
	}
		
	// creates the chosen biosphere
	public void runBiosphere(){
		if(choice.equals("1")){
			valid=true;
			aBiosphere.initialize(1);
			aBiosphere.runTurn();
		}
		else if(choice.equals("2")){
			valid=true;
			aBiosphere.initialize(2);
			aBiosphere.runTurn();
		}
		else if(choice.equals("3")){
			valid=true;
			aBiosphere.initialize(3);
			aBiosphere.runTurn();
		}
		else if(choice.equals("4")){
			valid=true;
			aBiosphere.initialize(4);
			aBiosphere.runTurn();
		}
		else if(choice.equals("5")){
			valid=true;
			aBiosphere.initialize(5);
			aBiosphere.runTurn();
		}
		else if(choice.equals("6")){
			valid=true;
			aBiosphere.initialize(6);
			aBiosphere.runTurn();
		}
		else if(choice.equals("7")){
			valid=true;
			aBiosphere.initialize(7);
			aBiosphere.runTurn();
		}
		else if(choice.equals("8")){
			valid=true;
			aBiosphere.initialize(8);
			aBiosphere.runTurn();
		}
	}
}