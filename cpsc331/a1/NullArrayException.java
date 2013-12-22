/**
 *Assignment 1 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */
 
 public class NullArrayException extends RuntimeException{
    
	/**
     *Calling super class
     */
    public NullArrayException(){
        super();
    }

    /**
     *Calling super class if a message is passed
     *@param message The message of exception
     */
    public NullArrayException(String str){
        super(str);
    }
}
