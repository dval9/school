/**
 * Tom Crowfoot
 * 10037477
 * CPSC 335 Assignment 4
 * http://algs4.cs.princeton.edu/55compression/Huffman.java.html was used to generate the huffman tree
 * http://introcs.cs.princeton.edu/java/stdlib/BinaryOut.java.html was used to print bits
 */

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Arrays;

public class as4{
    private static String input_file;
    private static String output_file;
    private static int totalChar;
    private static ArrayList<charNum> charList;
    //struct to hold a character, its frequency, and its encoded string
    private class charNum implements Comparable<charNum>{
	private int name;
	private String codeName;
	private int count;
	private double freq;
	private charNum(int name){
	    this.name=name;
	    count=1;
	}
	private void incCount(){
	    count++;
	}
	public int compareTo(charNum n){
	    return this.name-n.name;
	}
    }
    //node structure for building huffman tree
    private class Node implements Comparable<Node>{
	private int name;
	private double freq;
	private Node l, r;//left & right nodes
	Node(int name, double freq, Node l, Node r){
	    this.name=name;
	    this.freq=freq;
	    this.l=l;
	    this.r=r;
	}
	//check if leaf
	private boolean isLeaf(){
	    return(l==null&&r==null);
	}
	//compare, based on frequency
	public int compareTo(Node n){
	    return (int)((this.freq-n.freq)*100000000);
	}
    }

    as4(){
	totalChar=0;
	charList=new ArrayList<charNum>();
    }
    
    //build the tree
    private Node buildTree(){
	PriorityQueue<Node> queue=new PriorityQueue<Node>();
	for(charNum n:charList)//add all values as single trees
	    queue.add(new Node(n.name,n.freq,null,null));
	while(queue.size()>1){//merge two smallest trees
	    Node l=queue.poll();
	    Node r=queue.poll();
	    Node p=new Node(0,l.freq+r.freq,l,r);
	    queue.add(p);
	}
	return queue.poll();//return root of the queue
    }

    //find the encoded strings for each character in the file
    private void buildCodes(Node root,String str){
	if(!root.isLeaf()){
	    buildCodes(root.l,str+"0");
	    buildCodes(root.r,str+"1");
	}
	else{
	    for(charNum n:charList){
		if(root.name==n.name){
		    n.codeName=str;
		    break;
		}
	    }
	}
    }

    //computes the frequency of each character in the file
    private void computeFreq(){
	try{
	    FileInputStream is=new FileInputStream(input_file);
	    int read;
	    boolean add=true;
	    do{
		read=is.read();
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
    private void writeFile(Object[] chars){
	try{
	    FileInputStream is=new FileInputStream(input_file);
	    BinaryOut bo=new BinaryOut(output_file);
	    int read;
	    int dataSize=0;
	    String code="";
	    //write header in little endian
	    bo.writeByte(0x43);//ID C
	    bo.writeByte(0x33);//3
	    bo.writeByte(0x33);//3
	    bo.writeByte(0x35);//5
	    bo.writeByte(charList.size());//nSymbols
	    bo.writeByte(0);
	    //count nBits
	    do{		
		read=is.read();
		for(int i=0;i<chars.length;i++)
		    if(((charNum)chars[i]).name==read)
			dataSize+=((charNum)chars[i]).codeName.length();
	    }while(read!=-1);
	    bo.writeByte(dataSize&0x000000ff);//nDataBits
	    bo.writeByte(dataSize>>8&0x000000ff);
	    bo.writeByte(dataSize>>16&0x000000ff);
	    bo.writeByte(dataSize>>24&0x000000ff);
	    //write data
	    for(Object n:chars){
		bo.writeByte(((charNum)n).name);
		bo.writeByte(((charNum)n).codeName.length());
		int j=0;
		for(int i=0;i<((charNum)n).codeName.length();i++){
		    if(((charNum)n).codeName.charAt(i)=='1')
			bo.writeBit(true);
		    else
			bo.writeBit(false);
		    j++;
		}
		while(j%8!=0){
		    bo.writeBit(false);
		    j++;
		}
	    }
	    //write the compressed text
	    is=new FileInputStream(input_file);
	    do{		
		read=is.read();
		for(int i=0;i<chars.length;i++)
		    if(((charNum)chars[i]).name==read)
			code=((charNum)chars[i]).codeName;
		for(int i=0;i<code.length();i++){
		    if(code.charAt(i)=='1')
			bo.writeBit(true);
		    else
			bo.writeBit(false);
		}   
		}while(read!=-1);
	    //close streams
	    is.close();
	    bo.close();
	}catch(Exception e){
	    System.out.println("\n\n***error writing file***\n\n");
	    System.exit(1);
	}
    }

    //arg0=file_dictionary arg1=file_spellcheck
    public static void main(String[] args){
	long time=java.lang.System.currentTimeMillis();
	try{
	    input_file=args[0];
	    output_file=args[1];
	}catch(Exception e){
	    System.out.println("incorrect usage use\n-> java as4 input_file output_file");
	    System.exit(1);
	}
	as4 a=new as4();
	a.computeFreq();
	Node root=a.buildTree();
	a.buildCodes(root,"");
	Object[] chars=charList.toArray();
	Arrays.sort(chars);
	a.writeFile(chars);
	int averageHeight=0;
	for(charNum n:charList){
	    averageHeight+=((charNum)n).codeName.length();
	}
	System.out.println("Runtime in milliseconds: "+(java.lang.System.currentTimeMillis()-time));
	System.out.println("Memory cost of the program is: "+((java.lang.Runtime.getRuntime().totalMemory()-java.lang.Runtime.getRuntime().freeMemory())/1024)+"kB");
	System.out.println("Average height of tree is: "+averageHeight/charList.size());
    }

    //class to print things in binary, can print bits with writeBit, and print bytes with writeByte
    //taken from link at top of file slightly edited for use
    public final class BinaryOut{
	private BufferedOutputStream out;  //the output stream
	private int buffer;                //8-bit buffer of bits to write out
	private int N;                     //number of bits remaining in buffer

	/**
	 * Create a binary output stream from a filename.
	 */
	public BinaryOut(String s){
	    try{out=new BufferedOutputStream(new FileOutputStream(s));}
	    catch(IOException e){e.printStackTrace();}
	}

	/**
	 * Write the specified bit to the binary output stream.
	 */
	public void writeBit(boolean bit) {
	    //add bit to buffer
	    buffer<<=1;
	    if(bit)buffer|=1;
	    //if buffer is full (8 bits), write out as a single byte
	    N++;
	    if(N==8)clearBuffer();
	}

	/**
	 * Write the 8-bit byte to the binary output stream.
	 */
	public void writeByte(int x){
	    for(int i=0;i<8;i++){
		boolean bit=((x>>>(8-i-1))&1)==1;
		writeBit(bit);
	    }
	}

	/**
	 * Write out any remaining bits in buffer to the binary output stream, padding with 0s
	 */
	private void clearBuffer(){
	    if(N==0)return;
	    if(N>0)buffer<<=(8-N);
	    try{out.write(buffer);}
	    catch(IOException e){e.printStackTrace();}
	    N=0;
	    buffer=0;
	}

	/**
	 * Flush the binary output stream, padding 0s if number of bits written so far is not a multiple of 8.
	 */
	public void flush(){
	    clearBuffer();
	    try{out.flush();}
	    catch(IOException e){e.printStackTrace();}
	}

	/**
	 * Close and flush the binary output stream. Once it is closed, you can no longer write bits.
	 */
	public void close(){
	    flush();
	    try{out.close();}
	    catch(IOException e){e.printStackTrace();}
	}
    }
}


