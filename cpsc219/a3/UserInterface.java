/**
 *Tom Crowfoot
 *10037477
 *Version 0.5
 */

//userinterface handles the input/output, starts the loop for the program, invokes manager class to change data.
 
import java.util.Scanner;

public class UserInterface
{
	Manager aManager;
    private Scanner in;
	private boolean done;
	private char choice;
	
	//assigning default values
    public UserInterface()
    {
		in=new Scanner(System.in);
		done=false;
		aManager=new Manager();
    }
    
	//function called by driver, main loop of program
	public void start()
    {
		intro();
		while(done==false)
		{
			menu();
			getInput();
			selection(choice);
		}
    }
    
	public void intro()
	{
		System.out.println("This program can store information about a movie collection.");
		System.out.println("When adding movies to the collection, please use a raiting between 1 to 5.");
		System.out.println("Also, the possible choices for genre are action, drama, sci fi, comedy, horror, and martial arts.");
	}
	
	//display menu for user 
	public void menu()
    {
		System.out.println(" ");
		System.out.println("Please make a selection: ");
		System.out.println("\t(A)dd a movie to the collection.");
		System.out.println("\t(D)isplay the collection.");
		System.out.println("\t(S)earch for a movie.");
		System.out.println("\t(R)emove a movie from the collection.");
		System.out.println("\t(O)pposite order display.");
		System.out.println("\t(Q)uit.");
		System.out.print("Selection: ");
    }
    
	//gets the users input
	public void getInput()
	{
		final int EMPTY=0;
		String temp=in.nextLine();
		if(temp.length()==EMPTY)
		{
			System.out.println("That is not a valid input.");
			System.out.print("Selection: ");
			getInput();
		}
		else
			choice=temp.charAt(EMPTY);
	}
	
	//branch for what the user selects, calls methods from manager
	public void selection(char select)
	{
		switch(select)
		{
			case 'a':
			case 'A':
				aManager.add();
				break;
			case 'd':
			case 'D':
				aManager.display();
				break;
			case 's':
			case 'S':
				aManager.search();
				break;
			case 'r':
			case 'R':
				aManager.remove();
				break;
			case 'o':
			case 'O':
				aManager.reverse();
				break;
			case 'q':
			case 'Q':
				System.out.println("\n\tQuitting the program.");
				done=true;
				break;
			default:
				System.out.println("That is not a valid input.");
		}
	}
}