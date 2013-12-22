/**
 *Assignment 1 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */
 
 public class InvalidInputException extends RuntimeException{

    /**
     *Calling super class
     */
    public InvalidInputException(){
        super();
    }

    /**
     *Calling super class if a message is passed
     *@param message The message of exception
     */
    public InvalidInputException(String str){
        super(str);
    }
}
