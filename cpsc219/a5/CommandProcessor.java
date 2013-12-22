/**
 *Tom Crowfoot
 *10037477
 *CPSC 219 Assignment 5
 *Version 0.5
 */
//user interface class
//stores user input, contains instance of class MEWorld
//displays menu, gets user selection, determine selection, start/end game
import java.util.Scanner;
public class CommandProcessor{
    
    private String input;
    private boolean run;
    private Scanner in;
    private MEWorld aMEWorld;
	private int turnNumber;
    
    public  CommandProcessor(){
		aMEWorld=new MEWorld();
		run=true;
		in=new Scanner(System.in);
		turnNumber=1;
		input="";
    }

	//main loop of program
    public void run(){
		displayIntro();
		in.nextLine();
		while(run){
			System.out.println("Turn: "+turnNumber++);
			doPlayer();
			if(input=="q")
				return;
			doHobbit();
			if(Mode.checkGameStatus()==Mode.WON)
				return;
			doEnemies();
			if(Mode.checkGameStatus()==Mode.LOST)
				return;
		}
    }
	
	//checks to see if input only consists of numbers, converts the string to int if it is
	//to prevent erroring with bad input
	public boolean checkInput(String str){
		if(str.length()==0)
            return false;
		if(!Character.isDigit(str.charAt(0))&&!(str.charAt(0)=='-'))
			return false;
        for(int i=1;i<str.length();i++){
            if(!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
	}
	
	//moving the player, can also access cheat menu from here or quit
	public void doPlayerMove(){
		int move=0;
		boolean check=true;
		moveMenu();
		getInput();
		while(check){
			if(checkInput(input)){
				move=new Integer(Integer.parseInt(input));
				check=false;
			}
			else{
				System.out.print("Not valid entry, enter choice: ");
				getInput();
			}
		}
		if(move==0){
			cheatMenu();
			doPlayerMove();
		}
		else if(move<0){
			System.out.println("Quitting program, thanks for playing.");
			run=false;
			input="q";
			return;
		}
		else if(move>0&&move<10){
			if(aMEWorld.playerMove(move))
				doPlayerMove();
		}
		else{
			System.out.println("You cannot move that direction!");
			doPlayerMove();
		}
	}
	
	//doing the players attack, can get to cheat menu or quit
	public void doPlayerAttack(){
		int attack=0;
		boolean check=true;
		attackMenu();
		getInput();
		while(check){
			if(checkInput(input)){
				attack=new Integer(Integer.parseInt(input));
				check=false;
			}
			else{
				System.out.print("Not valid entry, enter choice: ");
				getInput();
			}
		}
		if(attack==0){
			cheatMenu();
			doPlayerAttack();
		}
		else if(attack<0){
			System.out.println("Quitting program, thanks for playing.");
			run=false;
			input="q";
			return;
		}
		else if(attack>0&&attack<10){
			if(aMEWorld.playerAttack(attack))
				doPlayerAttack();
		}
		else{
			System.out.println("You cannot attack that!");
			doPlayerAttack();
		}
	}
	
	//hobbits move, go east then south
	public void doHobbitsMove(){
	    aMEWorld.aHobbitMove();
	}
	
	//hobbit attack, random choice
	public void doHobbitsAttack(){
		aMEWorld.aHobbitAttack();
	}
	
	//enemies move random direction
	public void doEnemiesMove(){
		aMEWorld.enemiesMove();
	}
	
	//enemies attack random target
	public void doEnemiesAttack(){
		aMEWorld.enemiesAttack();
	}
	
	//cheat menu can toggle invincible on player, debug mode
	public void cheatMenu(){
		System.out.println("\nCHEAT OPTIONS: ");
		System.out.println("(C)heat mode on.");
		System.out.println("(D)ebug mode on.");
		System.out.println("(Q)uit cheat menu.");
		while(!input.equalsIgnoreCase("q")){
			System.out.print("Enter choice: ");
			getInput();
			if(input.equalsIgnoreCase("c")){
				if(Mode.cheat==false){
					Mode.cheat=true;
					System.out.println("Cheat mode enabled.");
				}
				else if(Mode.cheat==true){
					Mode.cheat=false;
					System.out.println("Cheat mode disabled.");
				}
			}
			else if(input.equalsIgnoreCase("d")){
				if(Mode.debug==false){
					Mode.debug=true;
					System.out.println("Debug mode enabled.");
				}
				else if(Mode.debug==true){
					Mode.debug=false;
					System.out.println("Debug mode disabled.");
				}
			}
		}
		System.out.println("");
	}
	
	//running players turn, does move then attack and checks if anything dies
	public void doPlayer(){
		if(!MEWorld.getPlayerStatus()){
			System.out.println("\nYou are dead, so skipping your turn.");
			return;
		}
		aMEWorld.display();
		System.out.println("************************");
		System.out.println("* Players turn to move *");
		System.out.println("************************");
		doPlayerMove();
		if(input=="q")
			return;
		System.out.println("");
		aMEWorld.display();
		System.out.println("**************************");
		System.out.println("* Players turn to attack *");
		System.out.println("**************************");
		doPlayerAttack();
		aMEWorld.checkDeaths();
		if(input=="q")
			return;
	}
	
	//running hobbits turn, moves and checks if game is won, then attacks and checks if anything died
	public void doHobbit(){
		System.out.println("");
		aMEWorld.display();
		System.out.println("**********************");
		System.out.println("* Hobbits are moving *");
		System.out.println("**********************");
		doHobbitsMove();
		if(Mode.checkGameStatus()==Mode.WON){
			System.out.println("Congratulations, you have won the game!");
			return;
		}
		System.out.println("[Press enter to continue]");
		in.nextLine();
		aMEWorld.display();
		System.out.println("*************************");
		System.out.println("* Hobbits are attacking *");
		System.out.println("*************************");
		doHobbitsAttack();
		aMEWorld.checkDeaths();
		System.out.println("[Press enter to continue]");
		in.nextLine();
	}
	
	//running enemies turn, moves them and attacks and checks if they kill anything, ending game if hobbit dies
	public void doEnemies(){
		aMEWorld.display();
		System.out.println("**********************");
		System.out.println("* Enemies are moving *");
		System.out.println("**********************");
		doEnemiesMove();
		System.out.println("[Press enter to continue]");
		in.nextLine();
		aMEWorld.display();
		System.out.println("*************************");
		System.out.println("* Enemies are attacking *");
		System.out.println("*************************");
		doEnemiesAttack();
		aMEWorld.checkDeaths();
		System.out.println("[Press enter to continue]");
		in.nextLine();
		aMEWorld.display();
		if(Mode.checkGameStatus()==Mode.LOST){
			System.out.println("The Hobbit has been slain! The game is lost...");
			return;
		}
		System.out.println("[End of turn, press enter to continue]");
		in.nextLine();
	}
	
	//getting the player input
    public void getInput(){
		input=in.nextLine();
    }
    
	//display player attack menu
	public void attackMenu(){
		System.out.println("ATTACK OPTIONS");
		System.out.println("----------------");
		System.out.println(" 7 8 9 ");
		System.out.println(" 4 5 6 ");
		System.out.println(" 1 2 3 ");
		System.out.println("Enter 5 to pass on attack.");
		System.out.println("Enter a negative value to quit the game.");
		System.out.print("Enter choice: ");
	}
	
	//display player move menu
    public void moveMenu(){
		System.out.println("MOVEMENT OPTIONS");
		System.out.println("----------------");
		System.out.println(" 7 8 9 ");
		System.out.println(" 4 5 6 ");
		System.out.println(" 1 2 3 ");
		System.out.println("Enter 5 to pass on movement.");
		System.out.println("Enter a negative value to quit the game.");
		System.out.print("Enter choice: ");
    }

	//display intro
    public void displayIntro(){
		System.out.println("**************************************************************************");
		System.out.println("* Welcome to Middle Earth!                                               *");
		System.out.println("* The objective of this game is to safely escort the hobbit to the exit. *");
		System.out.println("* You play the Gondorian hero Boromir, who must protect the hobbit from  *");
		System.out.println("* the evil goblins and Uruk-Hai. The hobbit will attempt to make for the *");
		System.out.println("* exit on its own, moving east then south. All characters can attack     *");
		System.out.println("* if they enter into melee range of an enemy. The Uruk-Hai can also fire *");
		System.out.println("* arrows for a range of two squares. If the hobbit succedes in reaching  *");
		System.out.println("* the end, the game is won. If the hobbit is slain, the game is lost.    *");
		System.out.println("* All control in the game is done with the number pad, good luck.        *");
		System.out.println("**************************************************************************");
		System.out.println("[Press enter to start the game!]");
    }
}