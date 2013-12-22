/**
 *Tom Crowfoot
 *10037477
 *CPSC 219 Assignment 3
 *Version 0.5
 *The program will make a list that can have movies added, removed, displayed, searched for, displayed in reverse order
 *The program will not list anything other than movies in the specified form, that is name, 3 actors, a single genre, and raiting
 *When removing movies, the first case found will be removed, and movies can only be added to the end
 *When searching movies, the first case will be displayed only
 */

 //driver class, contains main to call start method in userinterface
public class Driver
{   	
    public static void main (String[] args)
    {
		UserInterface aUserInterface=new UserInterface();
		aUserInterface.start();
    }
}