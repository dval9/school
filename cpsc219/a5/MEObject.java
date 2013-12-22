/*
  Author: James Tam
  Version: March 8, 2011

  The code for this entire class was written by James Tam.
*/

public class MEObject
{
    private char appearance;
    protected int hitPoints;
    public static final char DEFAULT_APPEARANCE = ' ';
    public static final char DEFAULT_HIT_POINTS= 6;
    public static final int DEAD = 0;

    public MEObject ()
    {
	setAppearance(DEFAULT_APPEARANCE);
	setHitPoints(DEFAULT_HIT_POINTS);
    }

    public MEObject (char anAppearance)
    {
	setHitPoints(DEFAULT_HIT_POINTS);
	setAppearance(anAppearance);
    }
    public void deductDamage (int damage) { hitPoints = hitPoints - damage; }
    public void setHitPoints (int hitPoints) { this.hitPoints = hitPoints; }
    public void setAppearance (char anAppearance) { appearance = anAppearance; }
    public char getAppearance () { return appearance; }
    public int getHitPoints () { return hitPoints; }
}
