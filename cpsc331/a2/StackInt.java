/**
 * The StackInt interface represents the Stack ADT as described
 * in the CPSC 331 lectures.
 *
 */
public interface StackInt<T> {

    /**
     * Tests whether the stack is empty. Return true if the stack is empty
	 * otherwise; returns false
     *
     * @return true if the stack is empty, false otherwise
     */
    public boolean isEmpty();

    /**
     * Pushes the object x onto the top of the stack.
     *
     * @param x object to be pushed onto the stack.
     */
    public void push(T x);

    /**
     * Returns the object at the top of the stack without removing it.
	 * post: the stack remains unchanged
     *
     * @return reference to the item at the top of the stack
     * @throws EmptyStackException if the stack is empty
     */
    public T top();

    /**
     * Returns the object at the top of the stack and removes it
	 * post: the stack is one item smaller
     *
     * @return reference to the item at the top of the stack
     * @throws EmptyStackException if the stack is empty
     */
    public T pop();
}
