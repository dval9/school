/**
 *Assignment 2 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */
 
 public class InvalidExpressionException extends RuntimeException{
    
     /**
     *Calling super class
     */
    public InvalidExpressionException(){
        super();
    }

    /**
     *Calling super class if a message is passed
     *@param message The message of exception
     */
    public InvalidExpressionException(String str){
        super(str);
    }
}
