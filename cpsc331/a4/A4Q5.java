/**
 * CPSC 331 Assignment 4 T01
 * @author Tom Crowfoot 10037477
 */
 
 /**
  * Class to test sorting algorithms in Sorting.java.
  * Generates 100 random arrays of various lengths, and runs each sorting algorithm on them.
  */
 public class A4Q5{
	
	public static void main(String[] args){
	    //testArray(128);
	    //testArray(1024);
	    //testArray(16384);
	    //testArray(65536);
	}
	
	/**
	 * Tests sorting algorithms.
	 *
	 * @param n Size of arrays to test.
	 */
	public static void testArray(int n){
	    for(int i=0;i<100;i++){
		testArrayJavaSort(n);
	    }
	    for(int i=0;i<100;i++){
		testArrayInsertionSort(n);
	    }
	    for(int i=0;i<100;i++){
		testArrayHeapSort(n);
	    }
	    for(int i=0;i<100;i++){
		testArrayQuickSort(n);
	    }
	    for(int i=0;i<100;i++){
		testArrayQuickSortImproved(n);
	    }
	}
	
	/**
	 * Tests javaSort sorting algorithm.
	 *
	 * @param n Size of arrays to test.
	 */
	public static void testArrayJavaSort(int n){
		int[] a=new int[n];
		for(int i=0;i<a.length;i++){
			a[i]=(int)(Math.rint(Math.random()*100));
		}
		Sorting.javaSort(a);
	}
	
	/**
	 * Tests insertionSort sorting algorithm.
	 *
	 * @param n Size of arrays to test.
	 */
	public static void testArrayInsertionSort(int n){
		int[] a=new int[n];
		for(int i=0;i<a.length;i++){
			a[i]=(int)(Math.rint(Math.random()*100));
		}
		Sorting.insertionSort(a);
	}
	
	/**
	 * Tests heapSort sorting algorithm.
	 *
	 * @param n Size of arrays to test.
	 */
	public static void testArrayHeapSort(int n){
		int[] a=new int[n];
		for(int i=0;i<a.length;i++){
			a[i]=(int)(Math.rint(Math.random()*100));
		}
		Sorting.heapSort(a);
	}
	
	/**
	 * Tests quickSort sorting algorithm.
	 *
	 * @param n Size of arrays to test.
	 */
	public static void testArrayQuickSort(int n){
		int[] a=new int[n];
		for(int i=0;i<a.length;i++){
			a[i]=(int)(Math.rint(Math.random()*100));
		}
		Sorting.quickSort(a);
	}
	
	/**
	 * Tests quickSortImproved sorting algorithm.
	 *
	 * @param n Size of arrays to test.
	 */
	public static void testArrayQuickSortImproved(int n){
		int[] a=new int[n];
		for(int i=0;i<a.length;i++){
			a[i]=(int)(Math.rint(Math.random()*100));
		}
		Sorting.quickSortImproved(a);
	}
 }