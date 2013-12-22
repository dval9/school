/**
 * Tom Crowfoot
 * 10037477
 * CPSC 335 Assignment 3 Trie spellcheck program
 * http://www.cs.duke.edu/~ola/courses/cps108/fall96/joggle/trie/Trie.java was used to help form code
 * both bonus implemented, that is only necessary pointers are stored, and the dictionary can handle any character
 * will have to slightly modify code to create space in arrays depending on which special characters are wanted to be used however, but trivial work
 */

import java.io.BufferedReader;
import java.io.FileReader;

public class as3{
    private static String file_dictionary;//name of dictionary file
    private static String file_spellcheck;//name of file to spellcheck
    private static int numKeys;//number of keys in dictionary
    private static int wordsSearched;//number of words checked
    private static long timeExist;//total time to compute when key exists
    private static long timeNotExist;//total time to compute when the key does not exist
    private static int depthExist;//total depth needed to search for keys that exist
    private static int height;//height of dictionary

    //trie data structure, can add and search
    public static class Trie{
    
	private static final int SIZE=36;//a..z+0..9
	private Trie[] links;//contains next depth
	private boolean isAWord;//boolean for if word was found or not

	public Trie(){
	    links=new Trie[SIZE];
	    isAWord=false;
	}

	//add the string to trie
	public void insert(String s){
	    Trie t=this;//current depth
	    for(int k=0;k<s.length();k++){
		if(Character.isLetter(s.charAt(k))){//letters stored 0..25
		    if(t.links[Character.toLowerCase(s.charAt(k))-'a']==null)
			t.links[Character.toLowerCase(s.charAt(k))-'a']=new Trie();
		    t=t.links[Character.toLowerCase(s.charAt(k))-'a'];
		}else if(Character.isDigit(s.charAt(k))){//digits stored 26..35
		    if(t.links[Character.toLowerCase(s.charAt(k))-'0'+26]==null)
			t.links[Character.toLowerCase(s.charAt(k))-'0'+26]=new Trie();
		    t=t.links[Character.toLowerCase(s.charAt(k))-'0'+26];
		}//add another else if for special characters if wanted, where instead of digit or characters can find other things and do same
	    }
	    height=(s.length()>height)?s.length():height;//height
	    t.isAWord=true;
	}

	//search for string in trie
	public boolean find(String s){
	    Trie t=this;
	    long x=System.currentTimeMillis();
	    for(int k=0;k<s.length();k++){
		if(Character.isLetter(s.charAt(k))){
		    if(t.links[Character.toLowerCase(s.charAt(k))-'a']==null)
			return false;
		    t=t.links[Character.toLowerCase(s.charAt(k))-'a'];
		}else if(Character.isDigit(s.charAt(k))){
		    if(t.links[Character.toLowerCase(s.charAt(k))-'0'+26]==null)
			return false;
		    t=t.links[Character.toLowerCase(s.charAt(k))-'0'+26];
		}//add another else if for special characters if wanted, where instead of digit or characters can find other things and do same
	    }
	    long y=System.currentTimeMillis();
	    if(t.isAWord){
		timeExist+=(y-x);
		depthExist+=s.length();
	    }
	    else
		timeNotExist+=(y-x);
	    return t.isAWord;
	}
    }

    //setting the statistics to initial values
    as3(){
	numKeys=wordsSearched=depthExist=height=0;
	timeExist=timeNotExist=0;
    }

    //reads files, stores words into array for use
    private static void fillDictionary(Trie t){
	try{
	    int i=0;
	    BufferedReader br=new BufferedReader(new FileReader(file_dictionary));
	    while(br.readLine()!=null)
	    	i++;
	    br=new BufferedReader(new FileReader(file_dictionary));
	    for(int j=0;j<i;j++){
		t.insert(br.readLine());
		numKeys++;
	    }
	    br.close();
	}catch(Exception e){
	    System.out.println("error creating dictionary");
	    System.exit(1);
	}
    }

    //checks the spelling file against the dictionary
    private static void checkFile(Trie t){
	try{
	    int i=0;
	    BufferedReader br=new BufferedReader(new FileReader(file_spellcheck));
	    while(br.readLine()!=null)
	    	i++;
	    br=new BufferedReader(new FileReader(file_spellcheck));
	    for(int j=0;j<i;j++){
		String[] s=br.readLine().split("\\s|\\n|;|,|!|\\?|\\.|\\[|\\]|--|\"|-|'|\\:|\\(|\\)");
		for(int x=0;x<s.length;x++)
		    if(!s[x].equals("")){
			wordsSearched++;
			if(t.find(s[x]))
			    System.out.println(s[x]+" FOUND");
			if(!t.find(s[x]))
			    System.out.println(s[x]+" NOT FOUND");
		    }
	    }
	    br.close();
	}catch(Exception e){
	    System.out.println("error checking file");
	    System.exit(1);
	}
    }

    //arg0=file_dictionary arg1=file_spellcheck
    public static void main(String[] args){
	try{
	    file_dictionary=args[0];
	    file_spellcheck=args[1];
	}catch(Exception e){
	    System.out.println("incorrect usage use -> java as3 file_dictionary file_spellcheck");
	    System.exit(1);
	}
	Trie dictionary=new Trie();
	fillDictionary(dictionary);
	checkFile(dictionary);
	System.out.println("Number of keys in the dictionary: "+numKeys+"\nNumber words searched: "+wordsSearched+"\nAverage time(ms) to look up key that exists: "+timeExist/wordsSearched+"\nAverage time(ms) to look up key that does not exists: "+timeNotExist/wordsSearched+"\nAverage depth required to search for a word that exists in dictionary: "+depthExist/wordsSearched+"\nMaximum height of the Trie is: "+height);
    }
}
