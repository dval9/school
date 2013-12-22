/**
 *Tom Crowfoot
 *10037477
 *Version 0.5
 */ 

//Stores information about movies; name, 3 cast members, genre, raiting
 
public class Movie
{
    private String name;
    private String[] cast;
    private String genre;
    private String raiting;
	public final int LENGTH=3;

	//creating the object in list
	public Movie(String aName,String aCast,String bCast,String cCast,String aGenre,String aRaiting)
    {
		setName(aName);
		setCast(aCast,bCast,cCast);
		setGenre(aGenre);
		setRaiting(aRaiting);
    }

	//sets name
    public void setName(String aName)
    {
		name=aName;
    }

	//returns a string for name
    public String getName() 
    {
		return name;
    }
    
	//sets the three cast members into an array
    public void setCast(String aCast,String bCast,String cCast)
    {
		cast=new String[LENGTH];
		cast[0]=aCast;
		cast[1]=bCast;
		cast[2]=cCast;
    }

	//returns cast array
    public String[] getCast()
    {
		return cast;
    }

	//sets the genre of the movie
    public void setGenre(String aGenre)
    {
		genre=aGenre;
    }

	//returns the string genre of the movie
    public String getGenre()
    {
		return genre;
    }

	//sets the raiting of the movie
    public void setRaiting(String aRaiting)
    {
		raiting=aRaiting;
    }

	//returns the string raiting
    public String getRaiting()
    {
		return raiting;
	}
}