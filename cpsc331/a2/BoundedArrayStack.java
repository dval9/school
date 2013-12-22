/**
 *Assignment 2 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */

/**
 *Class to implement BoundedStack interface, uses an array to hold data.
 *Constructor should accept one integer, to be used as capacity of the stack.
 *Contains methods: size(), capacity(), isFull(), isEmpty(), push(x), top(), pop()
 *Uses java.util package for EmptyStackException, can also throw FullStackException.
 */

import java.util.EmptyStackException;

public class BoundedArrayStack<T> implements BoundedStack<T>{

    private int capacity;
    private int top;
    private T[] stack;

    /**
     *Constructor for BoundedArrayStack class.
     *Takes an integer argument to define the capacity of the stack.
     *@param cap as the capacity of the stack.
     *Suppressing the unchecked data type warning when creating the array of type T[].
     */
    @SuppressWarnings("unchecked")
    BoundedArrayStack(int cap){
	capacity=cap;
	top=-1;
	stack=(T[])new Object[cap];
    }

    /**
     *Implementation of size method in BoundedStack interface.
     *Returns the number of elements in the stack.
     *@return The number of elements in the stack.
     */
    public int size(){
	return top+1;
    }

    /**
     *Implementation of capacity method in BoundedStack interface.
     *Returns the maximum number of elements the stack can store.
     *@return The maximum number of elements the stack can store.
     */
    public int capacity(){
	return capacity;
    }

    /**
     *Implementation of the isFull method in BoundedStack interface.
     *Returns True if the stack is full, false otherwise.
     *@return True if number of elements==capacity, false otherwise.
     */
    public boolean isFull(){
	return (top+1==capacity);
    }
 
    /**
     *Implementation of the isEmpty method in StackInt interface.
     *Returns true if the stack is empty, false otherwise.
     *@return true if stack is empty, false otherwise.
     */
    public boolean isEmpty(){
	return (top==-1);
    }

    /**
     *Implementation of the puch method in BoundedStack interface.
     *Adds an object onto the top of the stack.
     *<p>
     *  <strong>Precondition:</strong>
     *           Throws FullStackException if it is called when stack is full.
     * </p>
     *@param x object to be pushed onto the stack.
     *@throws FullStackException if the stack is full.
     */
    public void push(T x) throws FullStackException{
	if(isFull())
	    throw new FullStackException();
	top++;
	stack[top]=x;
    }

    /**
     *Implementation of the top method in StackInt interface.
     *Returns the element at the top of the stack, leaving the stack unchanged.
     *<p>
     *  <strong>Postcondition:</strong>
     *           The stack remains unchanged.
     * </p>
     *@return reference to the item at the top of the stack.
     *@throws EmptyStackException if the stack is empty.
     */
    public T top() throws EmptyStackException{
	if(isEmpty())
	    throw new EmptyStackException();
	return stack[top];
    }

    /**
     *Implementation of the pop method in StackInt interface.
     *Returns the element at the top of the stack, and removes it from the stack.
     *<p>
     *  <strong>Postcondition:</strong>
     *           The stack is one item smaller.
     * </p>
     *@return reference to the item at the top of the stack.
     *@throws EmptyStackException if the stack is empty.
     */
    public T pop() throws EmptyStackException{
	if(isEmpty())
	    throw new EmptyStackException();
	T x=stack[top];
	stack[top]=null;
	top--;
	return x;
    }
}
