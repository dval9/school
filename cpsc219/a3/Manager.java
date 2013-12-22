/**
 *Tom Crowfoot
 *10037477
 *Version 0.5
 */

//handles all list operations such as adding, removing, displaying, searching, displaying reverse
//interacts with the movienode,movie classes to edit data
//methods called by the userinterface class

import java.util.Scanner;

public class Manager
{  
    private MovieNode head;
    //these two variables are used to check if add method should cancel
	private String newGenre;
	private String newRaiting;
	
	//assigning list to null to start
    public Manager () 
    {
		head = null;
    }
	
	//adds a movie to the collection in alphabetical order
    public void add ()
    {
		Boolean cancel=false;
		while(cancel==false)
		{
			Scanner in=new Scanner(System.in);
			System.out.print("Enter a title for the movie: ");
			String newName=in.nextLine();
			System.out.print("Enter the name of the first actor: ");
			String newCastA=in.nextLine();
			System.out.print("Enter the name of the second actor: ");
			String newCastB=in.nextLine();
			System.out.print("Enter the name of the third actor: ");
			String newCastC=in.nextLine();
			checkGenre();
			if(newGenre=="")
			{
				cancel=true;
				break;
			}
			checkRaiting();
			if(newRaiting=="0")
			{
				cancel=true;
				break;
			}
			Movie aMovie=new Movie(newName,newCastA,newCastB,newCastC,newGenre,newRaiting);
			MovieNode aMovieNode=new MovieNode(aMovie,null);
			if(head==null)
				head=aMovieNode;
			else
			{
				if(aMovieNode.getData().getName().compareToIgnoreCase(head.getData().getName())<0)
				{
					aMovieNode.setNext(head);
					head=aMovieNode;
				}
				else
				{
					MovieNode current=head;
					MovieNode previous=null;
					while(current!=null)
					{
						previous=current;
						current=current.getNext();
						if(current!=null&&(aMovieNode.getData().getName().compareToIgnoreCase(current.getData().getName())<0))
						{
							aMovieNode.setNext(current);
							previous.setNext(aMovieNode);
							break;
						}
					}
					if(current==null)
							previous.setNext(aMovieNode);
				} 
			}
			System.out.println("The movie has been added to the collection.");
			cancel=true;
		}
    }
	
	//checks to see if the raiting is between 1 and 5, 0 cancels the add
	public void checkRaiting()
	{
		Scanner in=new Scanner(System.in);
		Boolean validRaiting=false;
		while(validRaiting==false)
		{
			System.out.print("Enter the raiting of the movie, enter 0 to cancel adding movie: ");
			String temp=in.nextLine();
			if(temp.equals("0")){
				System.out.println("Canceling adding movie to collection.");
				newRaiting="0";
				validRaiting=true;
			}
			else if(temp.equals("1")){
				newRaiting="1";
				validRaiting=true;
			}
			else if(temp.equals("2")){
				newRaiting="2";
				validRaiting=true;
			}
			else if(temp.equals("3")){
				newRaiting="3";
				validRaiting=true;
			}
			else if(temp.equals("4")){
				newRaiting="4";
				validRaiting=true;
			}
			else if(temp.equals("5")){
				newRaiting="5";
				validRaiting=true;
			}
			else
				System.out.println("That is not a valid raiting, please enter a value from 1 to 5.");
			/*switch(temp)
			{
				case "0":
					System.out.println("Canceling adding movie to collection.");
					newRaiting="0";
					validRaiting=true;
					break;
				case "1":
					newRaiting="1";
					validRaiting=true;
					break;
				case "2":
					newRaiting="2";
					validRaiting=true;
					break;
				case "3":
					newRaiting="3";
					validRaiting=true;
					break;
				case "4":
					newRaiting="4";
					validRaiting=true;
					break;
				case "5":
					newRaiting="5";
					validRaiting=true;
					break;
				default:
					System.out.println("That is not a valid raiting, please enter a value from 1 to 5.");
			}*/
		}
	}
	
	//checks to see if one of the accepted genres, or cancels adding movie if blank input is entered
	public void	checkGenre()
	{
		Scanner in=new Scanner(System.in);
		Boolean validGenre=false;
		while(validGenre==false)
		{
			System.out.print("Enter the genre of the movie, enter nothing to cancel adding movie: ");
			String temp=in.nextLine();
			if(temp.equals("")){
				System.out.println("Canceling adding movie to collection.");
				newGenre="";
				validGenre=true;
			}
			else if(temp.equals("action")||temp.equals("Action")){
				newGenre="Action";
				validGenre=true;
			}
			else if(temp.equals("drama")||temp.equals("Drama")){
				newGenre="Drama";
				validGenre=true;
			}
			else if(temp.equals("science fiction")||temp.equals("Science Fiction")){
				newGenre="Science Fiction";
				validGenre=true;
			}
			else if(temp.equals("comedy")||temp.equals("Comedy")){
				newGenre="Comedy";
				validGenre=true;
			}
			else if(temp.equals("horror")||temp.equals("Horror")){
				newGenre="Horror";
				validGenre=true;
			}
			else if(temp.equals("martial arts")||temp.equals("Martial Arts")){
				newGenre="Martial Arts";
				validGenre=true;
			}
			else
				System.out.println("That is not an accepted genre. Valid genres are:\n\tAction\n\tDrama\n\tScience Fiction\n\tComedy\n\tHorror\n\tMartial Arts");
			/*switch(temp)
			{
				case "":
					System.out.println("Canceling adding movie to collection.");
					newGenre="";
					validGenre=true;
					break;
				case "action":
				case "Action":
					newGenre="Action";
					validGenre=true;
					break;
				case "drama":
				case "Drama":
					newGenre="Drama";
					validGenre=true;
					break;
				case "science fiction":
				case "Science Fiction":
					newGenre="Science Fiction";
					validGenre=true;
					break;
				case "comedy":
				case "Comedy":
					newGenre="Comedy";
					validGenre=true;
					break;
				case "horror":
				case "Horror":
					newGenre="Horror";
					validGenre=true;
					break;
				case "martial arts":
				case "Martial Arts":
					newGenre="Martial Arts";
					validGenre=true;
					break;
				default:
					System.out.println("That is not an accepted genre. Valid genres are:\n\tAction\n\tDrama\n\tScience Fiction\n\tComedy\n\tHorror\n\tMartial Arts");
			}*/
		}
	}
	
	//displays the collection, 4 at a time until user presses enter
    public void display()
    {
		MovieNode temp=head;
		System.out.println("Displaying the collection, use enter to continue.");
		System.out.println(" ");
		if(head==null)
			System.out.println("\tThe collection is empty.");
		while(temp!=null)
		{
			int count=0;
			final int MAX=3;
			do
			{
				String[] castString=temp.getData().getCast();
				System.out.println(temp.getData().getName());
				System.out.println(castString[0]);
				System.out.println(castString[1]);
				System.out.println(castString[2]);
				System.out.println(temp.getData().getGenre());
				System.out.println(temp.getData().getRaiting());
				System.out.println("************");
				temp=temp.getNext();
				count++;
			}
			while(count<=MAX&&temp!=null);
			Scanner in=new Scanner(System.in);
			in.nextLine();
		}
    }

	//displays searched movie 
	public void display(MovieNode name)
	{
		MovieNode temp=name;
		String[] castString=temp.getData().getCast();
		System.out.println("");
		System.out.println(temp.getData().getName());
		System.out.println(castString[0]);
		System.out.println(castString[1]);
		System.out.println(castString[2]);
		System.out.println(temp.getData().getGenre());
		System.out.println(temp.getData().getRaiting());
    }
	
	//searches movie that user enters
	public void search()
	{
		System.out.println("");
		if(head==null)
			System.out.println("\tThe collection is empty.");
		else
		{
			MovieNode previous=null;
			MovieNode current=head;
			String searchName=null;
			boolean isFound=false;
			String currentName;
			Scanner in=new Scanner(System.in);
			System.out.print("Enter name of movie to display: ");
			searchName=in.nextLine();      
			while((current!=null)&&(isFound==false))
			{
				currentName=current.getData().getName();
				final int SAME=0;
				if(searchName.compareToIgnoreCase(currentName)==SAME)
					isFound=true;
				else
				{
					previous=current;
					current=current.getNext();
				}
			}
			if(isFound==true)
				display(current);
			else
				System.out.println("There is no such movie in the collection.");
		}
	}
	
	//removes specified movie from list
	public void remove()
    {
		if(head==null)
		{
			System.out.println("");
			System.out.println("\tThe collection is empty.");
		}
		else
		{
			MovieNode previous=null;
			MovieNode current=head;
			String searchName=null;
			boolean isFound=false;
			String currentName;
			Scanner in=new Scanner(System.in);
			System.out.println("");
			System.out.print("Enter name of the movie to remove: ");
			searchName=in.nextLine();      
			while((current!=null)&&(isFound==false))
			{
				currentName=current.getData().getName();
				final int SAME=0;
				if(searchName.compareToIgnoreCase(currentName)==SAME)
					isFound=true;
				else
				{
					previous=current;
					current=current.getNext();
				}
			}
			if(isFound==true)
			{
				System.out.println("Removing the movie from the collection.");
				if(previous==null)
					head=head.getNext();
				else
					previous.setNext(current.getNext());
			}
			else
				System.out.println("There is no such movie in the collection.");
		}
    }
	
	//displays the list in reverse order using recursion
	public void reverse()
    {
		System.out.println("Displaying the collection in reverse.");
		System.out.println(" ");
		if(head==null)
		{
			System.out.println("\tThe collection is empty.");
			return;
		}
		else
			System.out.println("");
			doReverse(head);
	}
	
	//the recursion part of reverse function
	public void doReverse(MovieNode temp)
	{
		if(temp==null)
			return;
		if(temp!=null)
		{
			doReverse(temp.getNext());
		}
		String[] castString=temp.getData().getCast();
		System.out.println(temp.getData().getName());
		System.out.println(castString[0]);
		System.out.println(castString[1]);
		System.out.println(castString[2]);
		System.out.println(temp.getData().getGenre());
		System.out.println(temp.getData().getRaiting());
		System.out.println("************");
		return;
    }
}