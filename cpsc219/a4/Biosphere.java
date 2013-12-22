/** Tom Crowfoot
*	10037477
*	Version 1.0
*	This class contains the array of critters, and does all the opperations on the array per the rules of the game.
*	Also displays the array after the opperations are complete.
*/
import java.util.Scanner;
class Biosphere{
    public static final int ROWS=10;
    public static final int COLUMNS=10;
    public static final int MIN_ROW=0;
    public static final int MAX_ROW=9;
    public static final int MIN_COLUMN=0;
    public static final int MAX_COLUMN=9;
    public static final String HORIZONTAL_LINE="  - - - - - - - - - -";
    public static final String HORIZONTAL_COUNT="  0 1 2 3 4 5 6 7 8 9 ";
    private Critter[][] previous;
    private Critter[][] current; 
    private int generation;

    public Biosphere(){
		int r;
		int c;
		generation=0;
		previous=new Critter[ROWS][COLUMNS];
		current=new Critter[ROWS][COLUMNS]; 
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++){
				previous[r][c]=new Critter(Critter.EMPTY);
				current[r][c]=new Critter(Critter.EMPTY);
			}
		}
    }

	// creating initial biosphere based on user choice
    public void initialize(int sphereType){
		if(sphereType==1)
			 initializeCase1();
		if(sphereType==2)
			initializeCase2();
		if(sphereType==3)
			initializeCase3();
		if(sphereType==4)
			initializeCase4();
		if(sphereType==5)
			initializeCase5();
		if(sphereType==6)
			initializeCase6();
		if(sphereType==7)
			initializeCase7();
		if(sphereType==7)
			initializeCase7();
		if(sphereType==8)
			initializeCase8();
   }
    
    // Completely empty
    private void initializeCase1(){
		int r,c;
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++){
				previous[r][c]=new Critter(Critter.EMPTY);
				current[r][c]=new Critter(Critter.EMPTY);
			}
		}
    }

    // Single critter
    private void initializeCase2(){
		int r,c;
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++){
				previous[r][c] = new Critter(Critter.EMPTY);
				current[r][c] = new Critter(Critter.EMPTY);
			}
		}
		previous[MIN_ROW][MIN_COLUMN]=new Critter(Critter.REGULAR);
		current[MIN_ROW][MIN_COLUMN]=new Critter(Critter.REGULAR);	
    }

    // Single birth (3 critters)
    private void initializeCase3(){
		int r,c;
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++){
				previous[r][c]=new Critter(' ');
				current[r][c]=new Critter(' ');
			}
		}
		current[1][1]=new Critter('*');
		previous[1][1]=new Critter('*');
		current[2][3]=new Critter('*');
		previous[2][3]=new Critter('*');
		current[3][1]=new Critter('*');
		previous[3][1]=new Critter('*');
    }
    
    // Simple pattern in middle
    private void initializeCase4(){
		int r,c;
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++){
				previous[r][c]=new Critter(Critter.EMPTY);
				current[r][c]=new Critter(Critter.EMPTY);
			}
		}
		previous[1][1]=new Critter(Critter.REGULAR);
		current[1][1]=new Critter(Critter.REGULAR);	
		previous[2][3]=new Critter(Critter.REGULAR);
		current[2][3]=new Critter(Critter.REGULAR);
		previous[2][2]=new Critter(Critter.REGULAR);
		current[2][2]=new Critter(Critter.REGULAR);			
		previous[3][1]=new Critter(Critter.REGULAR);
		current[3][1]=new Critter(Critter.REGULAR);	
		previous[3][3]=new Critter(Critter.REGULAR);
		current[3][3]=new Critter(Critter.REGULAR);
    }

    // Pattern near edges
    private void initializeCase5(){
		int r,c;
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++){
				previous[r][c]=new Critter(Critter.EMPTY);
				current[r][c]=new Critter(Critter.EMPTY);
			}
		}
		previous[MIN_ROW][MIN_COLUMN]=new Critter(Critter.REGULAR);
		current[MIN_ROW][MIN_COLUMN]=new Critter(Critter.REGULAR);
		previous[MIN_ROW][MAX_COLUMN]=new Critter(Critter.REGULAR);
		current[MIN_ROW][MAX_COLUMN]=new Critter(Critter.REGULAR);	
		previous[MAX_ROW][MIN_COLUMN]=new Critter(Critter.REGULAR);
		current[MAX_ROW][MIN_COLUMN]=new Critter(Critter.REGULAR);	
		previous[MAX_ROW][MAX_COLUMN]=new Critter(Critter.REGULAR);
		current[MAX_ROW][MAX_COLUMN]=new Critter(Critter.REGULAR);	
		previous[0][2]=new Critter(Critter.REGULAR);
		current[0][2]=new Critter(Critter.REGULAR);	
		previous[2][1]=new Critter(Critter.REGULAR);
		current[2][1]=new Critter(Critter.REGULAR);	
    }

	// fertile critters
    private void initializeCase6(){
		int r,c;
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++){
				previous[r][c]=new Critter(Critter.EMPTY);
				current[r][c]=new Critter(Critter.EMPTY);
			}
		}
		previous[2][MIN_COLUMN]=new Critter(Critter.FERTILE);
		current[2][MIN_COLUMN]=new Critter(Critter.FERTILE);	    
		previous[1][1]=new Critter(Critter.REGULAR);
		current[1][1]=new Critter(Critter.REGULAR);	    
		previous[4][1]=new Critter(Critter.FERTILE);
		current[4][1]=new Critter(Critter.FERTILE);	    
    }

    // Complex pattern, no fertile critters
    private void initializeCase7(){
		int r,c;
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++){
				previous[r][c]=new Critter(Critter.EMPTY);
				current[r][c]=new Critter(Critter.EMPTY);
			}
		}
		current[1][4]=new Critter(Critter.REGULAR);
		previous[1][4]=new Critter(Critter.REGULAR);
		current[2][5]=new Critter(Critter.REGULAR);
		previous[2][5]=new Critter(Critter.REGULAR);
		current[3][3]=new Critter(Critter.REGULAR);
		previous[3][3]=new Critter(Critter.REGULAR);
		current[3][4]=new Critter(Critter.REGULAR);
		previous[3][4]=new Critter(Critter.REGULAR);
		current[3][5]=new Critter(Critter.REGULAR);
		previous[3][5]=new Critter(Critter.REGULAR);
    }

    // Complex pattern, with fertile critters
    private void initializeCase8(){
		int r,c;
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++){
				previous[r][c]=new Critter(Critter.EMPTY);
				current[r][c]=new Critter(Critter.EMPTY);
			}
		}
		current[2][2]=new Critter(Critter.FERTILE);
		previous[2][2]=new Critter(Critter.FERTILE);
		current[2][3]=new Critter(Critter.REGULAR);
		previous[2][3]=new Critter(Critter.REGULAR);
		current[2][4]=new Critter(Critter.FERTILE);
		previous[2][4]=new Critter(Critter.FERTILE);
		current[4][1]=new Critter(Critter.REGULAR);
		previous[4][1]=new Critter(Critter.REGULAR);
    }

	// adds a new critter if conditions are met
	private void doBirth(int row,int column,int neighbours){
		if(previous[row][column].getAppearance()==Critter.EMPTY
			&&neighbours==3){
			if(Mode.debug==true)
				System.out.println("Birthing a new critter at ("+row+","+column+") because neighbour count was:"+neighbours);
			current[row][column]=new Critter(Critter.REGULAR);
		}
	}
	
	// kills off critter if conditions are met
	private void doDeath(int row,int column,int neighbours){
		if(((previous[row][column].getAppearance()==Critter.REGULAR)
			||(previous[row][column].getAppearance()==Critter.FERTILE))
			&&(neighbours<=1||neighbours>=4)){
			if(Mode.debug==true)
				System.out.println("Killing a critter at ("+row+","+column+") because neighbour count was:"+neighbours);
			current[row][column]=new Critter(Critter.EMPTY);
		}
	}
	
	// counts how many critters neighbour a given cell, returns the integer amount
	private int countNeighbour(int row,int column){
		int neighbour=0;
		if(row-1>=0&&column-1>=0){
			if(previous[row-1][column-1].getAppearance()!=Critter.EMPTY)
				neighbour++;
		}
		if(row-1>=0){
			if(previous[row-1][column].getAppearance()!=Critter.EMPTY)
				neighbour++;
		}
		if(row-1>=0&&column+1<=9){
			if(previous[row-1][column+1].getAppearance()!=Critter.EMPTY)
				neighbour++;
		}
		if(column-1>=0){
			if(previous[row][column-1].getAppearance()!=Critter.EMPTY)
				neighbour++;
		}
		if(column+1<=9){
			if(previous[row][column+1].getAppearance()!=Critter.EMPTY)
				neighbour++;
		}
		if(row+1<=9&&column-1>=0){
			if(previous[row+1][column-1].getAppearance()!=Critter.EMPTY)
				neighbour++;
		}
		if(row+1<=9){
			if(previous[row+1][column].getAppearance()!=Critter.EMPTY)
				neighbour++;
		}
		if(row+1<=9&&column+1<=9){
			if(previous[row+1][column+1].getAppearance()!=Critter.EMPTY)
				neighbour++;
		}
		return neighbour;
	}
	
	// counts how many critters neighbour a given cell(for births), returns the integer amount
	private int countNeighbourBirth(int row,int column){
		int neighbour=0;
		if(row-1>=0&&column-1>=0){
			if(previous[row-1][column-1].getAppearance()!=Critter.EMPTY){
				if(previous[row-1][column-1].getAppearance()==Critter.FERTILE)
					neighbour++;
				neighbour++;
			}
		}
		if(row-1>=0){
			if(previous[row-1][column].getAppearance()!=Critter.EMPTY){
				if(previous[row-1][column].getAppearance()==Critter.FERTILE)
					neighbour++;
				neighbour++;
			}
		}
		if(row-1>=0&&column+1<=9){
			if(previous[row-1][column+1].getAppearance()!=Critter.EMPTY){
				if(previous[row-1][column+1].getAppearance()==Critter.FERTILE)
					neighbour++;
				neighbour++;
			}
		}
		if(column-1>=0){
			if(previous[row][column-1].getAppearance()!=Critter.EMPTY){
				if(previous[row][column-1].getAppearance()==Critter.FERTILE)
					neighbour++;
				neighbour++;
			}
		}
		if(column+1<=9){
			if(previous[row][column+1].getAppearance()!=Critter.EMPTY){
				if(previous[row][column+1].getAppearance()==Critter.FERTILE)
					neighbour++;
				neighbour++;
			}
		}
		if(row+1<=9&&column-1>=0){
			if(previous[row+1][column-1].getAppearance()!=Critter.EMPTY){
				if(previous[row+1][column-1].getAppearance()==Critter.FERTILE)
					neighbour++;
				neighbour++;
			}
		}
		if(row+1<=9){
			if(previous[row+1][column].getAppearance()!=Critter.EMPTY){
				if(previous[row+1][column].getAppearance()==Critter.FERTILE)
					neighbour++;
				neighbour++;
			}
		}
		if(row+1<=9&&column+1<=9){
			if(previous[row+1][column+1].getAppearance()!=Critter.EMPTY){
				if(previous[row+1][column+1].getAppearance()==Critter.FERTILE)
					neighbour++;
				neighbour++;
			}
		}
		return neighbour;
	}
	
	// checking the corners of array 
	private void checkCorners(){
		if(Mode.debug==true)
			System.out.println("Checking corners");
		doBirth(MIN_ROW,MIN_COLUMN,countNeighbourBirth(MIN_ROW,MIN_COLUMN));
		doDeath(MIN_ROW,MIN_COLUMN,countNeighbour(MIN_ROW,MIN_COLUMN));
		doBirth(MIN_ROW,MAX_COLUMN,countNeighbourBirth(MIN_ROW,MAX_COLUMN));
		doDeath(MIN_ROW,MAX_COLUMN,countNeighbour(MIN_ROW,MAX_COLUMN));
		doBirth(MAX_ROW,MIN_COLUMN,countNeighbourBirth(MAX_ROW,MIN_COLUMN));
		doDeath(MAX_ROW,MIN_COLUMN,countNeighbour(MAX_ROW,MIN_COLUMN));
		doBirth(MAX_ROW,MAX_COLUMN,countNeighbourBirth(MAX_ROW,MAX_COLUMN));
		doDeath(MAX_ROW,MAX_COLUMN,countNeighbour(MAX_ROW,MAX_COLUMN));
	}
	
	// checking top row of the array
	private void checkTop(){
		if(Mode.debug==true)
			System.out.println("Checking top");
		int r=MIN_ROW;
		int c;
		for(c=MIN_COLUMN+1;c<COLUMNS-1;c++){
			doBirth(r,c,countNeighbourBirth(r,c));
			doDeath(r,c,countNeighbour(r,c));
		}
	}
	
	// checking bottom row of array
	private void checkBottom(){
		if(Mode.debug==true)
			System.out.println("Checking bottom");
		int r=MAX_ROW;
		int c;
		for(c=MIN_COLUMN+1;c<COLUMNS-1;c++){
			doBirth(r,c,countNeighbourBirth(r,c));
			doDeath(r,c,countNeighbour(r,c));
		}
	}
	
	// checking left column of array
	private void checkLeft(){
		if(Mode.debug==true)
			System.out.println("Checking left");
		int r;
		int c=MIN_COLUMN;
		for(r=MIN_ROW+1;r<ROWS-1;r++){
			doBirth(r,c,countNeighbourBirth(r,c));
			doDeath(r,c,countNeighbour(r,c));
		}
	}
	
	// checking right column of array
	private void checkRight(){
		if(Mode.debug==true)
			System.out.println("Checking right");
		int r;
		int c=MAX_COLUMN;
		for(r=MIN_ROW+1;r<ROWS-1;r++){
			doBirth(r,c,countNeighbourBirth(r,c));
			doDeath(r,c,countNeighbour(r,c));
		}
	}
	
	// checking everything else ie 8x8 center grid
	private void checkCenter(){
		if(Mode.debug==true)
			System.out.println("Checking center");
		int r,c;
		for(r=MIN_ROW+1;r<MAX_ROW;r++){
			for(c=MIN_COLUMN+1;c<MAX_COLUMN;c++){
			doBirth(r,c,countNeighbourBirth(r,c));
			doDeath(r,c,countNeighbour(r,c));
			}
		}
	}
	
	// decides births and deaths for next generation
	private void updateCritters(){
		checkCorners();
		checkTop();
		checkBottom();
		checkLeft();
		checkRight();
		checkCenter();
	}
	
	// displays the previous and current generations
    private void display (){
		int r,c;
		System.out.println("\t\tGeneration: "+generation);
		System.out.println("  PREVIOUS GENERATION"+"\t   CURRENT GENERATION");
		System.out.println(HORIZONTAL_COUNT+" \t "+HORIZONTAL_COUNT);
		for(r=0;r<ROWS;r++){
			System.out.println(HORIZONTAL_LINE+" \t "+HORIZONTAL_LINE);
			System.out.print(r+"|");
			for(c=0;c<COLUMNS;c++){
				previous[r][c].display();
				System.out.print('|');
			}
			System.out.print("\t"+r);
			System.out.print(" |");
			for(c=0;c<COLUMNS;c++){
				current[r][c].display();
				System.out.print('|');
			}
			System.out.println();
		}
		System.out.println(HORIZONTAL_LINE+" \t "+HORIZONTAL_LINE);
    }

	// setting previous generation equal to current generation
	private void changeGen(){
		int r,c;
		for(r=0;r<ROWS;r++){
			for(c=0;c<COLUMNS;c++)
				previous[r][c]=current[r][c];
		}
		generation++;
	}
	
	// called by userinterface to run the choosen case
	// displays the initial array, then runs the check to see what changes, then displays next generation
    public void runTurn(){
		updateCritters();
		display();
		changeGen();
		System.out.println("Press enter to go to next generation, or Q to end simulation.");
		Scanner in=new Scanner(System.in);
        String choice=in.nextLine();
		if(choice.equalsIgnoreCase("Q")){
			generation=0;
			return;
		}
		runTurn();
    }
}