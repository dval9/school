/*
  Author: James Tam
  Version: March 8, 2011

  The code for this entire class was written by James Tam.
*/


public class GoodGuy extends Humanoid
{
    public static final int DEFAULT_VIRTUE = 1;
    public static final char DEFAULT_APPEARANCE = 'G';;
    private int virtue;

    public GoodGuy (char anAppearance, int aRow, int aColumn)
    {
	super(anAppearance,aRow,aColumn);
    }
    
    public int getVirtue () { return virtue; }
}