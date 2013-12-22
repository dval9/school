/**
 *Assignment 2 CPSC 219
 *Tom Crowfoot
 *10037477
 *Version 1.0(final)
 *This program will prompt user for birth month/day of birth and output their zodiac sign
 *Will only accept intergers
 */


public class Drivertest2
{
      //test driver 2 for loop
      //tests month valid loop in startZodiac,cut off after a few trys
    public static void main (String [] args)
    {
	int count1=0;
	int count2=0;
	int count3=0;
	int count4=0;
	Zodiac cZodiac=new Zodiac();
	while(!cZodiac.isMonthValid(0)&&count1<=5)
	    {
	    System.out.println("not valid loop #"+count1);
	    count1++;
	    }
	while(!cZodiac.isMonthValid(13)&&count2<=5)
	    {
	    System.out.println("not valid loop #"+count2);
	    count2++;
	    }
	while(!cZodiac.isMonthValid(-5)&&count3<=5)
	    {
	    System.out.println("not valid loop #"+count3);
	    count3++;
	    }
	while(!cZodiac.isMonthValid(7)&&count4<=5)
	    {
	    System.out.println("not valid loop #"+count4);
	    count4++;
	    }
    }
}