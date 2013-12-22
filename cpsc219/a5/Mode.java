/*
  Author: James Tam
  Version: March 8, 2011

  The code for this entire class was written by James Tam.
*/

public class Mode
{
    public static final int NEUTRAL = 0;
    public static final int WON = 1;
    public static final int LOST = 2;

    public static boolean debug = false;
    public static boolean cheat = false;
    private static int gameStatus = NEUTRAL;

    public static int checkGameStatus () { return gameStatus; }
   
    public static void setGameStatus (int aStatus)
    {
	if ((aStatus == NEUTRAL) ||
	    (aStatus == WON) ||
	    (aStatus == LOST))
	    gameStatus = aStatus;
	else
	    System.out.println(">>Error: Mode.setGameStatus() Invalid status");

    }

}
