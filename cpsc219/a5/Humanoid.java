import java.util.Random;

/*
  Author: James Tam
  Version: March 8, 2011

  The code for this entire class was written by James Tam.
*/


public class Humanoid extends MEObject
{
    private Random aGenerator;
    private int row;
    private int column;
    private boolean hasMoved;
    protected int range;
    

    public static final char DEFAULT_APPEARANCE = 'U';
    public static final int ATTACK_DAMAGE = 2;
    public static final char DEAD = 'X';

    public Humanoid (char anAppearance, int aRow, int aColumn)
    {
	super(anAppearance);
	setRow(aRow);
	setColumn(aColumn);
	setRange(1);
	setHasMoved(false);
	aGenerator = new Random ();
    }

    public int attack ()
    {
	return ATTACK_DAMAGE;
    }

    public Coordinate move ()
    {
	int direction;
	Coordinate aCoordinate;
	direction = aGenerator.nextInt(9)+1;
	aCoordinate = positionToLocation(direction);
	return aCoordinate;
    }


    public Coordinate positionToLocation (int position)
    {
	Coordinate destination = new Coordinate ();
	switch (position)
        {
	    // Movement: Southwest
	    case 1:
		destination.setRow(row+1);
		destination.setColumn(column-1);
		break;
	   // Movement: South
	    case 2:
		destination.setRow(row+1);
		destination.setColumn(column);
		break;
	    // Movement: Southeast
	    case 3:
		destination.setRow(row+1);
		destination.setColumn(column+1);
		break;
	    // Movement: West
	    case 4:
		destination.setRow(row);
		destination.setColumn(column-1);
		break;
	    // Movement: no move
	    case 5:
		destination.setRow(row);
		destination.setColumn(column);
		break;
	    // Movement: East
	    case 6:
		destination.setRow(row);
		destination.setColumn(column+1);
		break;
	    // Movement Northwest
	    case 7:
		destination.setRow(row-1);
		destination.setColumn(column-1);
		break;
		// Movement: North
	    case 8:
		destination.setRow(row-1);
		destination.setColumn(column);
		break;
		// Movement: Northeast
	    case 9:
		destination.setRow(row-1);
		destination.setColumn(column+1);
		break;
	}
	return destination;
    }
    
    public void updateLocation (int dRow, int dColumn)
    {
	row = dRow;
	column = dColumn;
	if (Mode.debug == true)
	{
	    System.out.println(">>Humanoid.updateLocation()");
	    System.out.println("app (r/c)" + getAppearance() + " " + row + " " + column);
	}
    }

    public void setRow (int aRow) { row = aRow; }
    public void setColumn (int aColumn) { column = aColumn; }
    public void setHasMoved (boolean moved) { hasMoved = moved; }
    public void setRange (int aRange) { range = aRange; }
    public int getRow () { return row; }
    public int getColumn () { return column; }
    public boolean getHasMoved () { return hasMoved; }
    public int getRange () { return range; }
}