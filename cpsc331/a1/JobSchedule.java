/**
 *Assignment 1 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */

import java.io.*;	//library used for reading files

public class JobSchedule{
   
	private static int numberOfJobs;	//the first integer in the file, how many jobs are listed
    private static int[] jobPayAmounts;	//an array containing the amounts each job pays in the order the file is, total size of numberOfJobs

	/**
	*Main function for JobSchedule program
	*@param args Program arguements -> java JobSchedule filename r/i
	*/
    public static void main(String[]args){
		    if(args.length!=2||!args[1].equals("r")&&!args[1].equals("i")){
				System.out.println("Improper args. Correct usage -> java JobSchedule filename r/i");
				System.exit(1);
			}
			readFile(args[0]);
			try{
				if(args[1].equals("r"))
				System.out.println("Recursive function solution: "+recursiveJobSchedule(jobPayAmounts));
			else if(args[1].equals("i"))
				System.out.println("Iterative function solution: "+iterativeJobSchedule(jobPayAmounts));
			}catch(Exception e){
			    System.out.println(e.getMessage());
			}
    }

    /**
     *Testing function to set numberOfJobs
     *@param n Integer to set numberOfJobs to
     */
    public static void setNOJ(int n){
	numberOfJobs=n;
    }

	/**
	*Reads the input file, saves the first line into numberOfJobs, and the rest of the lines into jobPayAmounts
	*@param fileName Name of the file to read
	*/
    private static void readFile(String fileName){
       	try{
			FileReader fr=new FileReader(fileName);
			BufferedReader br=new BufferedReader(fr);
			numberOfJobs=Integer.parseInt(br.readLine());
			jobPayAmounts=new int[numberOfJobs];
			for(int i=0;i<numberOfJobs;i++){
				jobPayAmounts[i]=Integer.parseInt(br.readLine());
			}
			fr.close();
		}catch(Exception e){
			System.out.println("Error reading file.");
			System.exit(1);
		}
    }
	
	/**
	*
	*<p>
	*  <strong>Precondition:</strong>
	*           <code>A</code> is a non-null array of positive integers.<br />
	*  <strong>Postcondition:</strong>
	*           <code>A</code> is unmodified.
	* </p>
	*
	*Computes the maximum value for the given input file, as determined by the recursive algorithm
	*F(n)=>	F(0)=0
	*	 =>	F(1)=A[0]
	*	 =>	F(n>=2)=max(F(n-1),A[n-1]+F(n-2))
	*@param A An array of type integer
	*@return An integer, the solution to the algorithm
	*@throw NullArrayException If the array passed is null
	*@throw InvalidInputException If the array has any values <= 0
	*@throw IntegerOverflowException If the computed value is too large for a Java integer data type
	*/
    public static int recursiveJobSchedule(int[] A) throws NullArrayException, InvalidInputException, IntegerOverflowException{
		if(A==null)
			throw new NullArrayException("Null array passed.");
		if(A!=null){
			for(int i=0;i<numberOfJobs;i++){
				if(A[i]<=0)
					throw new InvalidInputException("The value "+A[i]+" is not allowed.");
			}
		}
		return recursiveJobSchedule(A,A.length);
	}	

	/**
	*Helper recursive function for recursiveJobSchedule(int[] A)
	*@param A An array of integers
	*@param n The length of array A as an integer
	*@return An integer used to calculate the maximum amount of money
	*/
    private static int recursiveJobSchedule(int[] A, int n){
	    if(n==0)
		return 0;
	    else if(n==1)
		return A[0];
	    else{
		if(Math.max((long)recursiveJobSchedule(A,n-1),(long)A[n-1]+(long)recursiveJobSchedule(A,n-2))>Integer.MAX_VALUE)
		    throw new IntegerOverflowException("The computed value is too large to be displayed by the Java Integer data type.");
		else
		    return Math.max(recursiveJobSchedule(A,n-1),A[n-1]+recursiveJobSchedule(A,n-2));
	    }
    }

    
	/**
	*
	*<p>
	*  <strong>Precondition:</strong>
	*           <code>A</code> is a non-null array of positive integers.<br />
	*  <strong>Postcondition:</strong>
	*           <code>A</code> is unmodified.
	* </p>
	*
	*Does the same job as the method recursiveJobSchedule
	*Is iterative instead of recursive, however the algorithm is pretty much the same
	*@param A An array of type integer
	*@return An integer, the solution to the algorithm
	*@throw NullArrayException If the array passed is null
	*@throw InvalidInputException If the array has any values <= 0
	*@throw IntegerOverflowException If the computed value is too large for a Java integer data type
	*/
    public static int iterativeJobSchedule(int[] A) throws NullArrayException, InvalidInputException, IntegerOverflowException{
		if(A==null)
			throw new NullArrayException("Null array passed.");
		if(A!=null){
			for(int i=0;i<numberOfJobs;i++){
			if(A[i]<=0)
				throw new InvalidInputException("The value "+A[i]+" is not allowed.");
			}
		}
		if(A.length==0)
		    return 0;
		int[] arrayF=new int[A.length+1];
		arrayF[0]=0;
		arrayF[1]=A[0];
		int i=0;
		while(i<arrayF.length){
		    if(i>=2){
			if(Math.max((long)arrayF[i-1],(long)A[i-1]+(long)arrayF[i-2])>Integer.MAX_VALUE)
			    throw new IntegerOverflowException("The computed value is too large to be displayed by the Java Integer data type.");
			else
			    arrayF[i]=Math.max(arrayF[i-1],A[i-1]+arrayF[i-2]);
		    }
		    i++;
		}
		return arrayF[arrayF.length-1];
    }
}
