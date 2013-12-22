/**
 *Assignment 2 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */

/**
 *Class to implement BoundedStack interface, uses a linked list to hold data.
 *Constructor should accept one integer, to be used as capacity of the stack.
 *Contains methods: size(), capacity(), isFull(), isEmpty(), push(x), top(), pop()
 *Uses java.util package for EmptyStackException, can also throw FullStackException.
 */

import java.util.EmptyStackException;

public class BoundedLinkedListStack<T> implements BoundedStack<T>{
    
    /**
     *Class to inpliment a linked list.
     */
    private class StackNode<T>{
	private T value;
	private StackNode<T> next;

	/**
	 *Constructor creates a new node, and links it into the list.
	 *@param x as the value for the node.
	 *@param n as the next node in the list.
	 */
	private StackNode(T x, StackNode<T> n){
	    value=x;
	    next=n;
	}
    }

    private int capacity;
    private int size;
    private StackNode<T> top;

    /**
     *Constructor for BoundedLinkedListStack class.
     *Takes an integer argument to define the capacity of the stack.
     *@param cap as the capacity of the stack.
     */
    BoundedLinkedListStack(int cap){
	capacity=cap;
	size=0;
	top=(StackNode<T>)null;
    }

    /**
     *Implementation of size method in BoundedStack interface.
     *Returns the number of elements in the stack.
     *@return The number of elements in the stack.
     */
    public int size(){
	return size;
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
	return (size==capacity);
    }
 
    /**
     *Implementation of the isEmpty method in StackInt interface.
     *Returns true if the stack is empty, false otherwise.
     *@return true if stack is empty, false otherwise.
     */
    public boolean isEmpty(){
	return (size==0);
    }

    /**
     *Implementation of the puch method in BoundedStack interface.
     *Adds an object onto the top of the stack.
     .*<p>
     *  <strong>Precondition:</strong>
     *           Throws FullStackException if it is called when stack is full.
     * </p>
     *@param x object to be pushed onto the stack.
     *@throws FullStackException if the stack is full.
     */
    public void push(T x) throws FullStackException{
	if(isFull())
	    throw new FullStackException();
	size++;
	top=new StackNode<T>(x,top);
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
	return top.value;
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
	T x=top.value;
	top=top.next;
	size--;
	return x;
    }
}