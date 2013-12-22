/**
 * Tom Crowfoot
 * 10037477
 * Compute frequency of characters in a file, writes results to a text file
 */

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Arrays;

public class compute_freq{
    private static String input_file;
    private static String output_file;
    private static int totalChar;
    private static ArrayList<charNum> charList;	
    private class charNum{//struct to hold a character, its frequency, and its encoded string
		private int name;
		private int count;
		private double freq;
		private charNum(int name){
			this.name=name;
			count=1;
		}
		private void incCount(){
			count++;
		}
    }	
   
    compute_freq(){
		totalChar=0;
		charList=new ArrayList<charNum>();
    }
    
    //computes the frequency of each character in the file
    private void computeFreq(){
		try{
			FileInputStream is=new FileInputStream(input_file);
			int read;
			boolean add=true;
			do{
				do{
					read=is.read();
				}while(read==10||read==13||read==32);//skip whitespace
				totalChar++;
				for(charNum n:charList){
					if(n.name==read){
						n.incCount();
						add=false;
						break;
					}
				}
				if(add)
					charList.add(new charNum(read));
				add=true;
			}while(read!=-1);
			totalChar--;
			charList.remove(charList.size()-1);//remove end of file character
			for(charNum n:charList)
				n.freq=(double)n.count/totalChar;	      
			is.close();
		}catch(Exception e){
			System.out.println("\n\n***error reading file***\n\n");
			System.exit(1);
		}
    }

    //write the compressed file
    private void writeFile(){
		try{
			PrintWriter out = new PrintWriter(output_file);
			for(charNum n:charList)
				out.println((char)n.name+" has a count of "+n.count+"/"+totalChar+" which is a frequency of: "+n.freq);
				//System.out.println((char)n.name+" has a count of "+n.count+"/"+totalChar+" which is a frequency of: "+n.freq);
			System.out.println(totalChar);
			out.close();
		}catch(Exception e){
			System.out.println("\n\n***error writing file***\n\n");
			System.exit(1);
		}
    }

    //arg0=file_dictionary arg1=file_spellcheck
    public static void main(String[] args){
		try{
			input_file=args[0];
			if(args.length==2)
				output_file=args[1];
			else
				output_file="output.txt";
		}catch(Exception e){
			System.out.println("incorrect usage use\n-> java as4 input_file output_file //output_file name defaults to output.txt");
			System.exit(1);
		}
		compute_freq a=new compute_freq();
		a.computeFreq();
		a.writeFile();
	}
}


