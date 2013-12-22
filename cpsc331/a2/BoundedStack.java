/**
 *Assignment 2 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */

/**
 *BoundedStack interface is an extension of the StackInt interface provided for the assignment.
 *Contains extra methods to check size, capacity, and if the stack is full.
 *Contains a version of the push method from StackInt, which additionally throws an error when the stack is full.
 */

public interface BoundedStack<T> extends StackInt<T>{

    /**
     *Returns the number of elements currently on the stack.
     *@return The number of elements in the stack.
     */
    public int size();

    /**
     *Returns maximum elements the stack can store.
     *@return The maximum number of elements the stack can store.
     */
    public int capacity();

    /**
     *Checks if the stack is full of elements.
     *@return True if number of elements==capacity, false otherwise.
     */
    public boolean isFull();

    /**
     *Pushes the object x onto the top of the stack.
     *<p>
     *  <strong>Precondition:</strong>
     *           Throws FullStackException if it is called when stack is full.
     * </p>
     *@param x object to be pushed onto the stack.
     *@throws FullStackException if the stack is full.
     */
    public void push(T x) throws FullStackException;
}