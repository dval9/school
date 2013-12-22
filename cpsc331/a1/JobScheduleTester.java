/**
 *Assignment 1 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *Testing class for JobSchedule program
 *Test cases
 *Base case empty (input={} - output 0)
 *Base case min (input={5} - output 5)
 *Small case one (input={1,2} - output 2)
 *Small case two (input={2,1} - output 2)
 *Medium case (input={5,6,8,6,2,4} - output 17)
 *Large case (input={1,2,3,...,20} - output 110)
 *NullArrayException case (input=null - output NullArrayException)
 *InvalidInputException case (input={1,-2} - output InvalidInputException)
 *Maximum case (input={Integer.MAX_VALUE} - output Integer.MAX_VALUE)
 *IntegerOverflowException case (input={Integer.MAX_VALUE,3,3} - output IntegerOverflowException)
 */

public class JobScheduleTester{

    private JobSchedule job;

    @Before
    public void setUp(){
	job=new JobSchedule();
    }

    @After
    public void tearDown(){
	job=null;
    }

    /**
     *Testing Base case empty (input={} - output 0)
     */
    @Test
    public void testBaseCaseEmptyRecursive(){
	System.out.println("Testing BaseCaseEmptyRecursive");
	job.setNOJ(0);
	int[] array=new int[0];
        int expOutput=0;
        int actOutput=job.recursiveJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Base case min (input={5} - output 5)
     */
    @Test
    public void testBaseCaseMinRecursive(){
	System.out.println("Testing BaseCaseMinRecursive");
	job.setNOJ(1);
	int[] array=new int[]{5};
        int expOutput=5;
        int actOutput=job.recursiveJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Small case one (input={1,2} - output 2)
     */
    @Test
    public void testSmallOneRecursive(){
	System.out.println("Testing SmallOneRecursive");
	job.setNOJ(2);
	int[] array=new int[]{1,2};
        int expOutput=2;
        int actOutput=job.recursiveJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Small case two (input={2,1} - output 2)
     */
    @Test
    public void testSmallTwoRecursive(){
	System.out.println("Testing SmallTwoRecursive");
	job.setNOJ(2);
	int[] array=new int[]{2,1};
        int expOutput=2;
        int actOutput=job.recursiveJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Medium case (input={5,6,8,6,2,4} - output 17)
     */
    @Test
    public void testMediumRecursive(){
	System.out.println("Testing MediumRecursive");
	job.setNOJ(6);
	int[] array=new int[]{5,6,8,6,2,4};
        int expOutput=17;
        int actOutput=job.recursiveJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Large case (input={1,2,3,...,20} - output 110)
     */
    @Test
    public void testLargeRecursive(){
	System.out.println("Testing LargeRecursive");
	job.setNOJ(20);
	int[] array=new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        int expOutput=110;
        int actOutput=job.recursiveJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing NullArrayException case (input=null - output NullArrayException)
     */
    @Test(expected=NullArrayException.class)
    public void testNullArrayExceptionRecursive(){
	System.out.println("Testing NullArrayExceptionRecursive");
	job.setNOJ(0);
	int[] array=null;
        job.recursiveJobSchedule(array);	
    }

    /**
     *Testing InvalidInputException case (input={1,-2} - output InvalidInputException)
     */
    @Test(expected=InvalidInputException.class)
    public void testInvalidInputExceptionRecursive(){
	System.out.println("Testing InvalidInputExceptionRecursive");
	job.setNOJ(2);
	int[] array=new int[]{1,-2};
        job.recursiveJobSchedule(array);	
    }

    /**
     *Testing Maximum case (input={Integer.MAX_VALUE} - output Integer.MAX_VALUE)
     */
    @Test
    public void testMaximumRecursive(){
	System.out.println("Testing MaximumRecursive");
	job.setNOJ(1);
	int[] array=new int[]{Integer.MAX_VALUE};
        int expOutput=Integer.MAX_VALUE;
        int actOutput=job.recursiveJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing IntegerOverflowException case (input={Integer.MAX_VALUE,3,3} - output IntegerOverflowException)
     */
    @Test(expected=IntegerOverflowException.class)
    public void testIntegerOverflowExceptionRecursive(){
	System.out.println("Testing IntegerOverflowExceptionRecursive");
	job.setNOJ(3);
	int[] array=new int[]{Integer.MAX_VALUE,3,3};
        job.recursiveJobSchedule(array);	
    }

    /**
     *Testing Base case empty (input={} - output 0)
     */
    @Test
    public void testBaseCaseEmptyIterative(){
	System.out.println("Testing BaseCaseEmptyIterative");
	job.setNOJ(0);
	int[] array=new int[0];
        int expOutput=0;
        int actOutput=job.iterativeJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Base case min (input={5} - output 5)
     */
    @Test
    public void testBaseCaseMinIterative(){
	System.out.println("Testing BaseCaseMinIterative");
	job.setNOJ(1);
	int[] array=new int[]{5};
        int expOutput=5;
        int actOutput=job.iterativeJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Small case one (input={1,2} - output 2)
     */
    @Test
    public void testSmallOneIterative(){
	System.out.println("Testing SmallOneIterative");
	job.setNOJ(2);
	int[] array=new int[]{1,2};
        int expOutput=2;
        int actOutput=job.iterativeJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Small case two (input={2,1} - output 2)
     */
    @Test
    public void testSmallTwoIterative(){
	System.out.println("Testing SmallTwoIterative");
	job.setNOJ(2);
	int[] array=new int[]{2,1};
        int expOutput=2;
        int actOutput=job.iterativeJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Medium case (input={5,6,8,6,2,4} - output 17)
     */
    @Test
    public void testMediumIterative(){
	System.out.println("Testing MediumIterative");
	job.setNOJ(6);
	int[] array=new int[]{5,6,8,6,2,4};
        int expOutput=17;
        int actOutput=job.iterativeJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing Large case (input={1,2,3,...,20} - output 110)
     */
    @Test
    public void testLargeIterative(){
	System.out.println("Testing LargeIterative");
	job.setNOJ(20);
	int[] array=new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        int expOutput=110;
        int actOutput=job.iterativeJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing NullArrayException case (input=null - output NullArrayException)
     */
    @Test(expected=NullArrayException.class)
    public void testNullArrayExceptionIterative(){
	System.out.println("Testing NullArrayExceptionIterative");
	job.setNOJ(0);
	int[] array=null;
        job.iterativeJobSchedule(array);	
    }

    /**
     *Testing InvalidInputException case (input={1,-2} - output InvalidInputException)
     */
    @Test(expected=InvalidInputException.class)
    public void testInvalidInputExceptionIterative(){
	System.out.println("Testing InvalidInputExceptionIterative");
	job.setNOJ(2);
	int[] array=new int[]{1,-2};
        job.iterativeJobSchedule(array);	
    }

    /**
     *Testing Maximum case (input={Integer.MAX_VALUE} - output Integer.MAX_VALUE)
     */
    @Test
    public void testMaximumIterative(){
	System.out.println("Testing MaximumIterative");
	job.setNOJ(1);
	int[] array=new int[]{Integer.MAX_VALUE};
        int expOutput=Integer.MAX_VALUE;
        int actOutput=job.iterativeJobSchedule(array);
        assertEquals(expOutput, actOutput);	
    }

    /**
     *Testing IntegerOverflowException case (input={Integer.MAX_VALUE,3,3} - output IntegerOverflowException)
     */
    @Test(expected=IntegerOverflowException.class)
    public void testIntegerOverflowExceptionIterative(){
	System.out.println("Testing IntegerOverflowExceptionIterative");
	job.setNOJ(3);
	int[] array=new int[]{Integer.MAX_VALUE,3,3};
        job.iterativeJobSchedule(array);	
    }
}