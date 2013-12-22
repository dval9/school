/**
 *Tom Crowfoot
 *10037477
 *CPSC 219 Assignment 5
 *Version 0.5
 */
 import java.util.Random;
//tracks info about game world
//reference to 'grid' a 10x10 array of references to MEObject, constants for the array ie MIN MAX SIZE, empty elements should be null, occupied elements reference MEObject
//display world with numbered grid, moving the elements of grid, constructor determines starting positions, which are constant 
public class MEWorld
{
    private MEObject[][] grid;
    private Human player;
    private Hobbit aHobbit;
	private Goblin gob1;
	private Goblin gob2;
	private Goblin gob3;
	private UrukHai uruk1;
	private UrukHai uruk2;
	private static boolean playerAlive=true;
    public static final int MIN=0;
    public static final int MAX=9;
    public static final int SIZE=10;
    public static final char EXIT='X';
    public static final String LINE="  - - - - - - - - - -";
    public static final String COUNT="  0 1 2 3 4 5 6 7 8 9 ";	

    public MEWorld(){
		setAllToEmpty();
		hardCodedInitialize();
		player=(Human)grid[0][0];
		aHobbit=(Hobbit)grid[0][1];
		gob1=(Goblin)grid[4][6];
		gob2=(Goblin)grid[6][1];
		gob3=(Goblin)grid[6][9];
		uruk1=(UrukHai)grid[2][3];
		uruk2=(UrukHai)grid[8][4];
    }
	
	//returns if player is alive, to skip their turn if dead
	public static boolean getPlayerStatus(){
		return playerAlive;
	}

	// sets the starting positions for things on the map
    public void hardCodedInitialize(){
      grid[0][0]=new Human(Human.DEFAULT_APPEARANCE,0,0);
      grid[0][1]=new Hobbit(Hobbit.DEFAULT_APPEARANCE,0,1);
      grid[2][3]=new UrukHai(UrukHai.DEFAULT_APPEARANCE,2,3);
      grid[8][4]=new UrukHai(UrukHai.DEFAULT_APPEARANCE,8,4);
      grid[4][6]=new Goblin(Goblin.DEFAULT_APPEARANCE,4,6); 
      grid[6][1]=new Goblin(Goblin.DEFAULT_APPEARANCE,6,1);
      grid[6][9]=new Goblin(Goblin.DEFAULT_APPEARANCE,6,9);
      grid[MAX][MAX]=new MEObject(EXIT);
    }

	// sets the map to everything empty, called before setting the npcs/player
    public void setAllToEmpty(){
       grid=new MEObject[SIZE][SIZE];
       for (int r=0;r<SIZE;r++)
          for (int c=0;c<SIZE;c++)
             grid[r][c]=null;
    }

	//moving hobbit east then south
    public void aHobbitMove(){
		if(aHobbit.getColumn()<MAX&&grid[aHobbit.getRow()][aHobbit.getColumn()+1]==null){
			aHobbit.updateLocation(aHobbit.positionToLocation(6).getRow(),aHobbit.positionToLocation(6).getColumn());
			grid[aHobbit.getRow()][aHobbit.getColumn()]=grid[aHobbit.getRow()][aHobbit.getColumn()-1];
			grid[aHobbit.getRow()][aHobbit.getColumn()-1]=null;
		}
		else if(aHobbit.getRow()<MAX&&aHobbit.getColumn()<MAX&&grid[aHobbit.getRow()+1][aHobbit.getColumn()+1]==null){
			aHobbit.updateLocation(aHobbit.positionToLocation(3).getRow(),aHobbit.positionToLocation(3).getColumn());
			grid[aHobbit.getRow()][aHobbit.getColumn()]=grid[aHobbit.getRow()-1][aHobbit.getColumn()-1];
			grid[aHobbit.getRow()-1][aHobbit.getColumn()-1]=null;
		}
		else if(aHobbit.getRow()<MAX&&grid[aHobbit.getRow()+1][aHobbit.getColumn()]==null){
			aHobbit.updateLocation(aHobbit.positionToLocation(2).getRow(),aHobbit.positionToLocation(2).getColumn());
			grid[aHobbit.getRow()][aHobbit.getColumn()]=grid[aHobbit.getRow()-1][aHobbit.getColumn()];
			grid[aHobbit.getRow()-1][aHobbit.getColumn()]=null;
		}
		else if(grid[aHobbit.getRow()+1][aHobbit.getColumn()]==grid[MAX][MAX]){
			if(Mode.debug)
				System.out.println(">>>Setting game mode to win.");
			Mode.setGameStatus(Mode.WON);
		}
    }
		
	//checks to see if anything dies, displays message if they do and removes them from game
	public void checkDeaths(){
		if(uruk1!=null&&uruk1.getHitPoints()<=MEObject.DEAD){
			if(Mode.debug)
				System.out.println(">>>Killing off urukhai at "+uruk1.getRow()+","+uruk1.getColumn());
			System.out.println("An enemy has been vanquished!");
			grid[uruk1.getRow()][uruk1.getColumn()]=null;
			uruk1=null;
		}
		if(uruk2!=null&&uruk2.getHitPoints()<=MEObject.DEAD){
			if(Mode.debug)
				System.out.println(">>>Killing off urukhai at "+uruk2.getRow()+","+uruk2.getColumn());
			System.out.println("An enemy has been vanquished!");
			grid[uruk2.getRow()][uruk2.getColumn()]=null;
			uruk2=null;
		}
		if(gob1!=null&&gob1.getHitPoints()<=MEObject.DEAD){
			if(Mode.debug)
				System.out.println(">>>Killing off goblin at "+gob1.getRow()+","+gob1.getColumn());
			System.out.println("An enemy has been vanquished!");
			grid[gob1.getRow()][gob1.getColumn()]=null;
			gob1=null;
		}
		if(gob2!=null&&gob2.getHitPoints()<=MEObject.DEAD){
			if(Mode.debug)
				System.out.println(">>>Killing off goblin at "+gob2.getRow()+","+gob2.getColumn());
			System.out.println("An enemy has been vanquished!");
			grid[gob2.getRow()][gob2.getColumn()]=null;
			gob2=null;
		}
		if(gob3!=null&&gob3.getHitPoints()<=MEObject.DEAD){
			if(Mode.debug)
				System.out.println(">>>Killing off goblin at "+gob3.getRow()+","+gob3.getColumn());
			System.out.println("An enemy has been vanquished!");
			grid[gob3.getRow()][gob3.getColumn()]=null;
			gob3=null;
		}
		if(aHobbit.getHitPoints()<=MEObject.DEAD){
			if(Mode.debug){
				System.out.println(">>>Setting game mode to lost.");
				System.out.println(">>>Killing off hobbit at "+aHobbit.getRow()+","+aHobbit.getColumn());
			}
			Mode.setGameStatus(Mode.LOST);
			aHobbit.setHitPoints(MEObject.DEAD);
			grid[aHobbit.getRow()][aHobbit.getColumn()]=null;
		}
		if(player!=null&&player.getHitPoints()<=MEObject.DEAD){
			if(playerAlive){
				if(Mode.debug)
					System.out.println(">>>Killing off player at "+player.getRow()+","+player.getColumn());
				System.out.println("You have been slain!");
				playerAlive=false;
				player.setHitPoints(MEObject.DEAD);
				grid[player.getRow()][player.getColumn()]=null;
			}
		}
	}
	
	//enemies attempt to attack player or hobbit in +-row/column, range is 2 for urukhai so uses slightly different method
	public void enemiesAttack(){
		boolean attacked=false;
		int attempt=0;
		while(!attacked&&gob1!=null&&attempt<MAX*500){
			int row=gob1.move().getRow();
			int column=gob1.move().getColumn();
			attempt++;
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]!=null&&(grid[row][column]==player||grid[row][column]==aHobbit)){
				attacked=true;
				grid[row][column].deductDamage(gob1.attack());
				if(Mode.debug)
					System.out.println(">>>gob at "+gob1.getRow()+","+gob1.getColumn()+"attacks "+grid[row][column].getAppearance());
			}
		}
		attempt=0;
		attacked=false;
		while(!attacked&&gob2!=null&&attempt<MAX*500){
			int row=gob2.move().getRow();
			int column=gob2.move().getColumn();
			attempt++;
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]!=null&&(grid[row][column]==player||grid[row][column]==aHobbit)){
				attacked=true;
				grid[row][column].deductDamage(gob2.attack());
				if(Mode.debug)
					System.out.println(">>>gob at "+gob2.getRow()+","+gob2.getColumn()+"attacks "+grid[row][column].getAppearance());
			}
		}
		attempt=0;
		attacked=false;
		while(!attacked&&gob3!=null&&attempt<MAX*500){
			int row=gob3.move().getRow();
			int column=gob3.move().getColumn();
			attempt++;
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]!=null&&(grid[row][column]==player||grid[row][column]==aHobbit)){
				attacked=true;
				grid[row][column].deductDamage(gob3.attack());
				if(Mode.debug)
					System.out.println(">>>gob at "+gob3.getRow()+","+gob3.getColumn()+"attacks "+grid[row][column].getAppearance());
			}
		}
		attempt=0;
		attacked=false;
		while(!attacked&&uruk1!=null&&attempt<MAX*500){
			int row=uruk1.attackAt().getRow();
			int column=uruk1.attackAt().getColumn();
			attempt++;
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]!=null&&(grid[row][column]==player||grid[row][column]==aHobbit)){
				attacked=true;
				grid[row][column].deductDamage(uruk1.attack());
				if(Mode.debug)
					System.out.println(">>>uruk at "+uruk1.getRow()+","+uruk1.getColumn()+"attacks "+grid[row][column].getAppearance());
			}
		}
		attempt=0;
		attacked=false;
		while(!attacked&&uruk2!=null&&attempt<MAX*500){
			int row=uruk2.attackAt().getRow();
			int column=uruk2.attackAt().getColumn();
			attempt++;
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]!=null&&(grid[row][column]==player||grid[row][column]==aHobbit)){
				attacked=true;
				grid[row][column].deductDamage(uruk2.attack());
				if(Mode.debug)
					System.out.println(">>>uruk at "+uruk2.getRow()+","+uruk2.getColumn()+"attacks "+grid[row][column].getAppearance());
			}
		}
	}
	
	//hobbit attacks if something is in melee direction
	public void aHobbitAttack(){
		boolean attacked=false;
		int attempt=0;
		while(!attacked&&aHobbit!=null&&attempt<MAX*500){
			int row=aHobbit.move().getRow();
			int column=aHobbit.move().getColumn();
			attempt++;
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]!=null&&grid[row][column]!=player&&grid[row][column]!=aHobbit){
				attacked=true;
				grid[row][column].deductDamage(aHobbit.attack());
				if(Mode.debug)
					System.out.println(">>>hobbit at "+aHobbit.getRow()+","+aHobbit.getColumn()+"attacks "+grid[row][column].getAppearance());
			}
		}
	}
	
	//checks player choice to attack, then deducts hitpoints from target if they can attack that
    public boolean playerAttack(int choice){
		if(choice==1&&player.getRow()<MAX&&player.getColumn()>MIN&&grid[player.getRow()+1][player.getColumn()-1]!=null&&grid[player.getRow()+1][player.getColumn()-1]!=aHobbit){
			grid[player.getRow()+1][player.getColumn()-1].deductDamage(player.attack());
			System.out.println("You attack the "+grid[player.getRow()+1][player.getColumn()-1].getAppearance()+".");
			return false;
		}
		else if(choice==2&&player.getRow()<MAX&&grid[player.getRow()+1][player.getColumn()]!=null&&grid[player.getRow()+1][player.getColumn()]!=aHobbit){
			grid[player.getRow()+1][player.getColumn()].deductDamage(player.attack());
			System.out.println("You attack the "+grid[player.getRow()+1][player.getColumn()].getAppearance()+".");
			return false;
		}
		else if(choice==3&&player.getRow()<MAX&&player.getColumn()<MAX&&grid[player.getRow()+1][player.getColumn()+1]!=null&&grid[player.getRow()+1][player.getColumn()+1]!=aHobbit){
			grid[player.getRow()+1][player.getColumn()+1].deductDamage(player.attack());
			System.out.println("You attack the "+grid[player.getRow()+1][player.getColumn()+1].getAppearance()+".");
			return false;
		}
		else if(choice==4&&player.getColumn()>MIN&&grid[player.getRow()][player.getColumn()-1]!=null&&grid[player.getRow()][player.getColumn()-1]!=aHobbit){
			grid[player.getRow()][player.getColumn()-1].deductDamage(player.attack());
			System.out.println("You attack the "+grid[player.getRow()][player.getColumn()-1].getAppearance()+".");
			return false;
		}
		else if(choice==5){
			System.out.println("You pass on your attack.");
			return false;
		}
		else if(choice==6&&player.getColumn()<MAX&&grid[player.getRow()][player.getColumn()+1]!=null&&grid[player.getRow()][player.getColumn()+1]!=aHobbit){
			grid[player.getRow()][player.getColumn()+1].deductDamage(player.attack());
			System.out.println("You attack the "+grid[player.getRow()][player.getColumn()+1].getAppearance()+".");
			return false;
		}
		else if(choice==7&&player.getRow()>MIN&&player.getColumn()>MIN&&grid[player.getRow()-1][player.getColumn()-1]!=null&&grid[player.getRow()-1][player.getColumn()-1]!=aHobbit){
			grid[player.getRow()-1][player.getColumn()-1].deductDamage(player.attack());
			System.out.println("You attack the "+grid[player.getRow()-1][player.getColumn()-1].getAppearance()+".");
			return false;
		}
		else if(choice==8&&player.getRow()>MIN&&grid[player.getRow()-1][player.getColumn()]!=null&&grid[player.getRow()-1][player.getColumn()]!=aHobbit){
			grid[player.getRow()-1][player.getColumn()].deductDamage(player.attack());
			System.out.println("You attack the "+grid[player.getRow()-1][player.getColumn()].getAppearance()+".");
			return false;
		}
		else if(choice==9&&player.getRow()>MIN&&player.getColumn()<MAX&&grid[player.getRow()-1][player.getColumn()+1]!=null&&grid[player.getRow()-1][player.getColumn()+1]!=aHobbit){
			grid[player.getRow()-1][player.getColumn()+1].deductDamage(player.attack());
			System.out.println("You attack the "+grid[player.getRow()-1][player.getColumn()+1].getAppearance()+".");
			return false;
		}
		else{
			System.out.println("\nYou cannot attack that.\n");
			return true;
		}
    }
    
	//moves enemies in random direction if spot is empty
	public void enemiesMove(){
		boolean moved=false;
		while(!moved&&gob1!=null){
			int row=gob1.move().getRow();
			int column=gob1.move().getColumn();
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]==null){
				moved=true;
				grid[gob1.getRow()][gob1.getColumn()]=null;
				gob1.updateLocation(row,column);
				grid[gob1.getRow()][gob1.getColumn()]=gob1;
			}
		}
		moved=false;
		while(!moved&&gob2!=null){
			int row=gob2.move().getRow();
			int column=gob2.move().getColumn();
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]==null){
				moved=true;
				grid[gob2.getRow()][gob2.getColumn()]=null;
				gob2.updateLocation(row,column);
				grid[gob2.getRow()][gob2.getColumn()]=gob2;
			}
		}
		moved=false;
		while(!moved&&gob3!=null){
			int row=gob3.move().getRow();
			int column=gob3.move().getColumn();
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]==null){
				moved=true;
				grid[gob3.getRow()][gob3.getColumn()]=null;
				gob3.updateLocation(row,column);
				grid[gob3.getRow()][gob3.getColumn()]=gob3;
			}
		}
		moved=false;
		while(!moved&&uruk1!=null){
			int row=uruk1.move().getRow();
			int column=uruk1.move().getColumn();
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]==null){
				moved=true;
				grid[uruk1.getRow()][uruk1.getColumn()]=null;
				uruk1.updateLocation(row,column);
				grid[uruk1.getRow()][uruk1.getColumn()]=uruk1;
			}
		}
		moved=false;
		while(!moved&&uruk2!=null){
			int row=uruk2.move().getRow();
			int column=uruk2.move().getColumn();
			if(row>=MIN&&row<=MAX&&column>=MIN&&column<=MAX&&grid[row][column]==null){
				moved=true;
				grid[uruk2.getRow()][uruk2.getColumn()]=null;
				uruk2.updateLocation(row,column);
				grid[uruk2.getRow()][uruk2.getColumn()]=uruk2;
			}
		}
	}
	
	//moves player in selected direction if possible
	public boolean playerMove(int choice){
		if(choice==1&&player.getRow()<MAX&&player.getColumn()>MIN&&grid[player.getRow()+1][player.getColumn()-1]==null){
			player.updateLocation(player.positionToLocation(choice).getRow(),player.positionToLocation(choice).getColumn());
			grid[player.getRow()][player.getColumn()]=grid[player.getRow()-1][player.getColumn()+1];
			grid[player.getRow()-1][player.getColumn()+1]=null;
			return false;
		}
		else if(choice==2&&player.getRow()<MAX&&grid[player.getRow()+1][player.getColumn()]==null){
			player.updateLocation(player.positionToLocation(choice).getRow(),player.positionToLocation(choice).getColumn());
			grid[player.getRow()][player.getColumn()]=grid[player.getRow()-1][player.getColumn()];
			grid[player.getRow()-1][player.getColumn()]=null;
			return false;
		}
		else if(choice==3&&player.getRow()<MAX&&player.getColumn()<MAX&&grid[player.getRow()+1][player.getColumn()+1]==null){
			player.updateLocation(player.positionToLocation(choice).getRow(),player.positionToLocation(choice).getColumn());
			grid[player.getRow()][player.getColumn()]=grid[player.getRow()-1][player.getColumn()-1];
			grid[player.getRow()-1][player.getColumn()-1]=null;
			return false;
		}
		else if(choice==4&&player.getColumn()>MIN&&grid[player.getRow()][player.getColumn()-1]==null){
			player.updateLocation(player.positionToLocation(choice).getRow(),player.positionToLocation(choice).getColumn());
			grid[player.getRow()][player.getColumn()]=grid[player.getRow()][player.getColumn()+1];
			grid[player.getRow()][player.getColumn()+1]=null;
			return false;
		}
		else if(choice==5){
			System.out.println("You do not move.");
			return false;
		}
		else if(choice==6&&player.getColumn()<MAX&&grid[player.getRow()][player.getColumn()+1]==null){
			player.updateLocation(player.positionToLocation(choice).getRow(),player.positionToLocation(choice).getColumn());
			grid[player.getRow()][player.getColumn()]=grid[player.getRow()][player.getColumn()-1];
			grid[player.getRow()][player.getColumn()-1]=null;
			return false;
		}
		else if(choice==7&&player.getRow()>MIN&&player.getColumn()>MIN&&grid[player.getRow()-1][player.getColumn()-1]==null){
			player.updateLocation(player.positionToLocation(choice).getRow(),player.positionToLocation(choice).getColumn());
			grid[player.getRow()][player.getColumn()]=grid[player.getRow()+1][player.getColumn()+1];
			grid[player.getRow()+1][player.getColumn()+1]=null;
			return false;
		}
		else if(choice==8&&player.getRow()>MIN&&grid[player.getRow()-1][player.getColumn()]==null){
			player.updateLocation(player.positionToLocation(choice).getRow(),player.positionToLocation(choice).getColumn());
			grid[player.getRow()][player.getColumn()]=grid[player.getRow()+1][player.getColumn()];
			grid[player.getRow()+1][player.getColumn()]=null;
			return false;
		}
		else if(choice==9&&player.getRow()>MIN&&player.getColumn()<MAX&&grid[player.getRow()-1][player.getColumn()+1]==null){
			player.updateLocation(player.positionToLocation(choice).getRow(),player.positionToLocation(choice).getColumn());
			grid[player.getRow()][player.getColumn()]=grid[player.getRow()+1][player.getColumn()-1];
			grid[player.getRow()+1][player.getColumn()-1]=null;
			return false;
		}
		else{
			System.out.println("\nYou cannot move there.\n");
			return true;
		}
	}
	
	//displays game field
    public void display(){
		System.out.println("Boromir statistics");
		System.out.println("-------------------");
		System.out.println("Hit points: "+player.getHitPoints());
		System.out.println("\nHobbit statistics");
		System.out.println("------------------");
		System.out.println("Hit points: "+aHobbit.getHitPoints()+"\n");
		System.out.println(COUNT);
		System.out.println(LINE);
		for(int r=MIN;r<=MAX;r++){
			System.out.print(r+"|");
			for(int c=MIN;c<=MAX;c++){
				if (grid[r][c]!=null)
					System.out.print(grid[r][c].getAppearance()+"|");
				else
					System.out.print(" "+"|");
			}
			System.out.println("\n"+LINE);
		}
		System.out.println();
	}
}
