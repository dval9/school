/**
 *Assignment 1 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *Testing class for LispEval program
 *Test cases
 *Input="+" - output InvalidExpressionException - Checking a non valid expression because it has no brackets
 *Input="()" - output InvalidExpressionException - Checking a non valid expression because it has no operands
 *Input="(" - output InvalidExpressionException - Checking a non valid expression because it does not have a closed term
 *Input="(5)" - output InvalidExpressionException - Checking a non valid expression because it has no brackets
 *Input="(/)" - output InvalidExpressionException - Checking a non valid expression because the divisor operator needs arguments
 *Input="(*)" - output 1.0 - Checking a non valid expression because it has no brackets
 *Input="(- 1)" - output -1.0 - Checking a valid simple expression
 *Input="(* (5 6))" - output InvalidExpressionException - Checking a non valid expression because the second term does not have an operator
 *Input="(+ (* (+ (- 3))))" - output -3.0 - Checking a valid more complex expression
 *Input="(+ 3 a)" - output InvalidExpressionException - Checking a non valid expression because it contains an invalid character
 */

public class LispTester{

    private LispEval lisp;

    @Before
    public void setUp(){
		lisp=new LispEval();
    }

    @After
    public void tearDown(){
		lisp=null;
    }

    /**
     *Testing case 1 (input="+" - output InvalidExpressionException)
     */
	@SuppressWarnings("unchecked")
    @Test(expected=InvalidExpressionException.class)
    public void testCase1(){
		System.out.println("Testing case 1");
		String expr="+";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		lisp.evaluate(stack,expr);
    }
	
	/**
     *Testing case 2 (input="()" - output InvalidExpressionException)
     */
	@SuppressWarnings("unchecked")
    @Test(expected=InvalidExpressionException.class)
    public void testCase2(){
		System.out.println("Testing case 2");
		String expr="()";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		lisp.evaluate(stack,expr);
    }
	
	/**
     *Testing case 3 (input="(" - output InvalidExpressionException)
     */
	@SuppressWarnings("unchecked")
    @Test(expected=InvalidExpressionException.class)
    public void testCase3(){
		System.out.println("Testing case 3");
		String expr="(";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		lisp.evaluate(stack,expr);
    }
	
	/**
     *Testing case 4 (input="(5)" - output InvalidExpressionException)
     */
	@SuppressWarnings("unchecked")
    @Test(expected=InvalidExpressionException.class)
    public void testCase4(){
		System.out.println("Testing case 4");
		String expr="(5)";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		lisp.evaluate(stack,expr);
    }
	
	/**
     *Testing case 5 (input="(/)" - output InvalidExpressionException)
     */
	@SuppressWarnings("unchecked")
    @Test(expected=InvalidExpressionException.class)
    public void testCase5(){
		System.out.println("Testing case 5");
		String expr="(/)";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		lisp.evaluate(stack,expr);
    }
	
	/**
     *Testing case 6 (input="(*)" - output 1.0)
     */
    @SuppressWarnings({"unchecked","deprecation"})
	@Test
    public void testCase6(){
		System.out.println("Testing case 6");
		String expr="(*)";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		double expOutput=1.0;
		double actOutput=lisp.evaluate(stack,expr);
		assertEquals(expOutput, actOutput,0);	
    }
	
	/**
     *Testing case 7 (input="(- 1)" - output 1.0)
     */
    @SuppressWarnings({"unchecked","deprecation"})
	@Test
    public void testCase7(){
		System.out.println("Testing case 7");
		String expr="(- 1)";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		double expOutput=-1.0;
		double actOutput=lisp.evaluate(stack,expr);
		assertEquals(expOutput, actOutput,0);	
    }
	
	/**
     *Testing case 8 (input="(* (5 6))" - output InvalidExpressionException)
     */
	@SuppressWarnings("unchecked")
    @Test(expected=InvalidExpressionException.class)
    public void testCase8(){
		System.out.println("Testing case 8");
		String expr="(* (5 6))";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		lisp.evaluate(stack,expr);
    }
	
	/**
     *Testing case 9 (input="(+ (* (+ (- 3))))" - output 1.0)
     */
	@SuppressWarnings({"unchecked","deprecation"})
    @Test
    public void testCase9(){
		System.out.println("Testing case 9");
		String expr="(+ (* (+ (- 3))))";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		double expOutput=-3.0;
		double actOutput=lisp.evaluate(stack,expr);
		assertEquals(expOutput, actOutput,0);	
    }
	
	/**
     *Testing case 10 (input="(+ 3 a)" - output InvalidExpressionException)
     */
	@SuppressWarnings("unchecked")
    @Test(expected=InvalidExpressionException.class)
		public void testCase10(){
		System.out.println("Testing case 10");
		String expr="(+ 3 a)";
		BoundedArrayStack stack=new BoundedArrayStack<String>(expr.length());
		lisp.evaluate(stack,expr);
    }
}