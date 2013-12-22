/**
 *Assignment 2 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */
 
 public class FullStackException extends RuntimeException{
    
     /**
     *Calling super class
     */
    public FullStackException(){
        super();
    }

    /**
     *Calling super class if a message is passed
     *@param message The message of exception
     */
    public FullStackException(String str){
        super(str);
    }
}
