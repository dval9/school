/*
  Author: James Tam
  Version: March 8, 2011

  The code for this entire class was written by James Tam.
*/

public class Coordinate
{
    private int row;
    private int column;
    public static final int INVALID = -1;

    public Coordinate ()
    {
	row = INVALID;
	column = INVALID;
    }

    public Coordinate (int aRow, int aColumn)
    {
	this();
	row = aRow;
	column = aColumn;
    }

    public int getRow () 
    { 
	return row; 
    }
    
    public int getColumn () 
    { 
	return column; 
    }
    
    public void setRow (int aRow) 
    { 
	row = aRow; 
    }

    public void setColumn (int aColumn) 
    { 
	column = aColumn; 
    }
}
