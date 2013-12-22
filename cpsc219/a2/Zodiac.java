/**
 *Assignment 2 CPSC 219
 *Tom Crowfoot
 *10037477
 *Version 1.0(final)
 */

import java.util.Scanner;

public class Zodiac
{
    //initializing variables
	
    public final int JANUARY=1;
    public final int FEBRUARY=2;
    public final int MARCH=3;
    public final int APRIL=4;
    public final int MAY=5;
    public final int JUNE=6;
    public final int JULY=7;
    public final int AUGUST=8;
    public final int SEPTEMBER=9;
    public final int OCTOBER=10;
    public final int NOVEMBER=11;
    public final int DECEMBER=12;
    public final int MIN_DAY=1;
    public final int MAX_DAY_LONG=31;
    public final int MAX_DAY_SHORT=30;
    public final int MAX_DAY_FEB=28;
    public final int MIN_MONTH=1;
    public final int MAX_MONTH=12;
    public final int DEFAULT=-1;
    private int month;
    private int day;
    private Scanner in;
	
    public Zodiac ()
    {
		//main
		month=DEFAULT;
		day=DEFAULT;
		in=new Scanner(System.in);
    }
    public void startZodiac ()
    {
		//start of run

		introduction();
		promptMonth();
		while(!isMonthValid())
			promptMonth();
		promptDay();
		while(!isDayValid())
			promptDay();
		determineSign();
    }
    public void introduction ()
    {
		//user instructions

		System.out.println("This program will calculate your Zodiac sign.");
		System.out.println("Please use intergers for months.\ni.e. January=1...");
		System.out.println("If you are born on February 29th, use the 28th instead.");
    }
    public void determineSign ()
    {
		//takes input and outputs zodiac sign
		
		if((month==DECEMBER&&(day>=22&&day<=MAX_DAY_LONG))
			||(month==JANUARY&&(day>=MIN_DAY&&day<=19)))
				System.out.println("Your astrological sign is Capricorn.");
		else if((month==JANUARY&&(day>=20&&day<=MAX_DAY_LONG))
			||(month==FEBRUARY&&(day>=MIN_DAY&&day<=18)))
				System.out.println("Your astrological sign is Aquarius.");
		else if((month==FEBRUARY&&(day>=19&&day<=MAX_DAY_FEB))
			||(month==MARCH&&(day>=MIN_DAY&&day<=20)))
				System.out.println("Your astrological sign is Pisces.");
		else if((month==MARCH&&(day>=21&&day<=MAX_DAY_LONG))
			||(month==APRIL&&(day>=MIN_DAY&&day<=19)))
				System.out.println("Your astrological sign is Aries.");
		else if((month==APRIL&&(day>=20&&day<=MAX_DAY_SHORT))
			||(month==MAY&&(day>=MIN_DAY&&day<=20)))
				System.out.println("Your astrological sign is Taurus.");
		else if((month==MAY&&(day>=21&&day<=MAX_DAY_LONG))
			||(month==JUNE&&(day>=MIN_DAY&&day<=21)))
				System.out.println("Your astrological sign is Gemini.");
		else if((month==JUNE&&(day>=22&&day<=MAX_DAY_SHORT))
			||(month==JULY&&(day>=MIN_DAY&&day<=22)))
				System.out.println("Your astrological sign is Cancer.");
		else if((month==JULY&&(day>=23&&day<=MAX_DAY_LONG))
			||(month==AUGUST&&(day>=MIN_DAY&&day<=22)))
				System.out.println("Your astrological sign is Leo.");
		else if((month==AUGUST&&(day>=23&&day<=MAX_DAY_LONG))
			||(month==SEPTEMBER&&(day>=MIN_DAY&&day<=22)))
				System.out.println("Your astrological sign is Virgo.");
		else if((month==SEPTEMBER&&(day>=23&&day<=MAX_DAY_SHORT))
			||(month==OCTOBER&&(day>=MIN_DAY&&day<=22)))
				System.out.println("Your astrological sign is Libra.");
		else if((month==OCTOBER&&(day>=23&&day<=MAX_DAY_LONG))
			||(month==NOVEMBER&&(day>=MIN_DAY&&day<=21)))
				System.out.println("Your astrological sign is Scorpio.");
		else if((month==NOVEMBER&&(day>=22&&day<=MAX_DAY_SHORT))
			||(month==DECEMBER&&(day>=MIN_DAY&&day<=21)))
				System.out.println("Your astrological sign is Sagittarius.");
	}
	public boolean isDayValid ()
	{
		//checks if day is sensible

		if((month==JANUARY||month==MARCH||month==MAY||month==JULY||month==AUGUST||month==OCTOBER||month==DECEMBER)
			&&(day>=MIN_DAY&&day<=MAX_DAY_LONG))
				return true;
		else if((month==APRIL||month==JUNE||month==SEPTEMBER||month==NOVEMBER)
			&&(day>=MIN_DAY&&day<=MAX_DAY_SHORT))
				return true;
		else if((month==FEBRUARY)
			&&(day>=MIN_DAY&&day<=MAX_DAY_FEB))
				return true;
		else
				System.out.println("That is not a valid day for that month.");
				return false;
	}
    public boolean isMonthValid ()
    {
		//checks if month is sensible
		
		if((month>=MIN_MONTH)&&(month<=MAX_MONTH))
			return true;
		else
			System.out.println("That is not a valid month.");
			return false;
    }
    public boolean isMonthValid (int test)
    {
		//testing loop
		
		if((test>=MIN_MONTH)&&(test<=MAX_MONTH))
		    {
		    System.out.println("valid entry"+test);
		    return true;
		    }
		else
		    {
		    System.out.println("not valid"+test);
	            return false;
		    }
    }
    public void promptDay ()
    {
		//revieves day input
	
		System.out.print("Please enter the day you were born: ");
		day=in.nextInt();
    }
    public void promptMonth ()
    {
		//recieves month input

		System.out.print("Please enter the month you were born: ");
		month=in.nextInt();
    }	
}