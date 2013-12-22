/**
 *Assignment 2 CPSC 219
 *Tom Crowfoot
 *10037477
 *Version 1.0(final)
 *This program will prompt user for birth month/day of birth and output their zodiac sign
 *Will only accept intergers
 */


public class Drivertest1
{ 
    //test driver 1 for branch
    //tests isMonthValid branch to see possible outcomes
    public static void main (String [] args)
    {
	Zodiac bZodiac=new Zodiac();
	bZodiac.isMonthValid(0);
	bZodiac.isMonthValid(13);
	bZodiac.isMonthValid(-5);
	bZodiac.isMonthValid(7);
    }    
}