/*Assignment 1 CPSC 219
 *Version 1.0
 *Runs on java 1.7.0_02
 *Tom Crowfoot
 *10037477
 *Purpose of this program is to tell the user their astrological sign
 *Does not take into account leap years, so february is set to
 *a maximum of 28 days
 *Will only accept intergers for birth month/day of birth
 *Anything else will error and quit*/


//calling scanner to take user imput
import java.util.Scanner;

public class AstrologicalSigns
{
	public static void main (String [] args)
	{
		//setting variables
		int day = 00;
		int month = 00;
		int checkmonth = 0;
		int checkday = 0;
		Scanner in = new Scanner(System.in);
		
		//month
		while (checkmonth == 0)
		{
			//receiving month from user
			System.out.print("Enter the number corresponding to the month you were born: ");
			month = in.nextInt ();
			//checking to see if month makes sense
			if (month >= 1 && month <= 12)
				checkmonth = 1;
			else
				System.out.println("The month you entered does not exist, try again.");
		}
		
		//day
		while (checkday == 0)
		{
			//receiving day from user
			System.out.print("Enter the day of the month you were born: ");
			day = in.nextInt ();
			//checking to see if day makes sense
			if ((day >= 1 && day <= 31) && month == 1)
				checkday = 1;
			else if ((day >= 1 && day <= 28) && month == 2)
				checkday = 1;
			else if ((day >= 1 && day <= 31) && month == 3)
				checkday = 1;
			else if ((day >= 1 && day <= 30) && month == 4)
				checkday = 1;
			else if ((day >= 1 && day <= 31) && month == 5)
				checkday = 1;				
			else if ((day >= 1 && day <= 30) && month == 6)
				checkday = 1;				
			else if ((day >= 1 && day <= 31) && month == 7)
				checkday = 1;				
			else if ((day >= 1 && day <= 31) && month == 8)
				checkday = 1;				
			else if ((day >= 1 && day <= 30) && month == 9)
				checkday = 1;				
			else if ((day >= 1 && day <= 31) && month == 10)
				checkday = 1;				
			else if ((day >= 1 && day <= 30) && month == 11)
				checkday = 1;				
			else if ((day >= 1 && day <= 31) && month == 12)
				checkday = 1;				
			else
				System.out.println("That day does not exist for that month, try again.");
		}
		
		//assigning user a sign
		if (((month == 12) && (day >=22 && day <=31)) || ((month == 1) && (day >= 1 && day <= 19)))
			System.out.println("Your astrological sign is Capricorn.");
		else if (((month == 1) && (day >=20 && day <=31)) || ((month == 2) && (day >= 1 && day <= 18)))
			System.out.println("Your astrological sign is Aquarius.");
		else if (((month == 2) && (day >=19 && day <=28)) || ((month == 3) && (day >= 1 && day <= 20)))
			System.out.println("Your astrological sign is Pisces.");
		else if (((month == 3) && (day >=21 && day <=31)) || ((month == 4) && (day >= 1 && day <= 19)))
			System.out.println("Your astrological sign is Aries.");
		else if (((month == 4) && (day >=20 && day <=30)) || ((month == 5) && (day >= 1 && day <= 20)))
			System.out.println("Your astrological sign is Taurus.");
		else if (((month == 5) && (day >=21 && day <=31)) || ((month == 6) && (day >= 1 && day <= 21)))
			System.out.println("Your astrological sign is Gemini.");
		else if (((month == 6) && (day >=22 && day <=30)) || ((month == 7) && (day >= 1 && day <= 22)))
			System.out.println("Your astrological sign is Cancer.");
		else if (((month == 7) && (day >=23 && day <=31)) || ((month == 8) && (day >= 1 && day <= 22)))
			System.out.println("Your astrological sign is Leo.");
		else if (((month == 8) && (day >=23 && day <=31)) || ((month == 9) && (day >= 1 && day <= 22)))
			System.out.println("Your astrological sign is Virgo.");
		else if (((month == 9) && (day >=23 && day <=30)) || ((month == 10) && (day >= 1 && day <= 22)))
			System.out.println("Your astrological sign is Libra.");
		else if (((month == 10) && (day >=23 && day <=31)) || ((month == 11) && (day >= 1 && day <= 21)))
			System.out.println("Your astrological sign is Scorpio.");
		else if (((month == 11) && (day >=22 && day <=30)) || ((month == 12) && (day >= 1 && day <= 21)))
			System.out.println("Your astrological sign is Sagittarius.");
	}
}