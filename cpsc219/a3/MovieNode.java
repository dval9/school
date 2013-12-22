/**
 *Tom Crowfoot
 *10037477
 *Version 0.5
 */

//links the movie nodes together
 
public class MovieNode
{
    private Movie data;
    private MovieNode next;

	//making initial node null	
    public MovieNode() 
    { 
	data = null;
	next = null;
    }

	//creates the object in the list
    public MovieNode(Movie data,MovieNode next)
    {
	setData(data);
	setNext(next);
    }

	//sets data from the movie class
    public void setData(Movie data)  
    {   
	this.data = data;  
    }

	//returns data
    public Movie getData()  
    {  
	return data;  
    }

	//sets the pointer
    public void setNext(MovieNode next)  
    {   
	this.next = next;  
    }

	//returns the pointer
    public MovieNode getNext()  
    {   
	return next;   
    }
}
