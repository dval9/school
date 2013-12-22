/**
 *Assignment 1 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */
 
 public class IntegerOverflowException extends RuntimeException{

    /**
     *Calling super class
     */
    public IntegerOverflowException(){
        super();
    }

    /**
     *Calling super class if a message is passed
     *@param message The message of exception
     */
    public IntegerOverflowException(String str){
        super(str);
    }
}
