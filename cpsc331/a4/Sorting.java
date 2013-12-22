/**
 * CPSC 331 Assignment 4 T01
 * @author Tom Crowfoot 10037477
 */

import java.util.Arrays;

/**
 * Class that will sort given input arrays in a variety of methods.
 * Available sorting methods are:
 * Default java method, call with javaSort(array).
 * Insertion sort, call with insertionSort(array).
 * Heap sort, call with heapSort(array).
 * Quick sort, call with quickSort(array).
 * Improved version of quick sort, call with quickSortImproved(array).
 * Another version of quick sort, call with quickSortBonus(array).
 */
public class Sorting{
    
    private static int heapSize;

    /**
     * Sorts an input array in-place using the default Java sorting method as
     * defined in the Arrays.sort() function.
     *
     * @param A The array to be sorted
     */
    public static void javaSort(int[] A){
		Arrays.sort(A);
    }

    /**
     * Sorts an input array using an insertion sort implementation.
     *
     * @param A The array to be sorted
     */
    public static void insertionSort(int[] A){
		for(int i=1;i<A.length;i++){
			int j=i;
			while(j>0&&A[j]<A[j-1]){
				int temp=A[j];
				A[j]=A[j-1];
				A[j-1]=temp;
				j--;
			}
		}
    }
    
    /**
     * Sorts an input array a heap sort implementation.
     *
     * @param A The array to be sorted
     */
    public static void heapSort(int[] A){
		heapSize=1;
		int i=1;
		while(i<A.length){
			heapInsert(A,A[i]);
			i++;
		}
		i=A.length-1;
		while(i>0){
			int largest=heapDelete(A);
			A[i]=largest;
			i--;
		}
    }
    
    /**
     * Helper function for heapSort.
     * Used in creating the heap.
     *
     * @param A The heap to add the value to.
     * @param key The value to add to the heap.
     */
    private static void heapInsert(int[] A,int key){
		A[heapSize]=key;
		heapSize++;
		int j=heapSize-1;
		while(j>0&&A[j]>A[(j-1)/2]){
			int temp=A[j];
			A[j]=A[(j-1)/2];
			A[(j-1)/2]=temp;
			j=(j-1)/2;
		}
    }

    /**
     * Helper function for heapSort.
     * Used to remove largest value from heap.
     *
     * @param A The heap to remove max value from.
     * @return int Value removed from heap.
     */
    private static int heapDelete(int[] A){
		int max=A[0];
		A[0]=A[heapSize-1];
		heapSize--;
		int j=0;
		while(j<heapSize){
			int l=2*j+1;
			int r=2*j+2;
			int largest=j;
			if(l<heapSize&&A[l]>A[largest])
				largest=l;
			if(r<heapSize&&A[r]>A[largest])
				largest=r;
			if(largest!=j){
				int temp=A[j];
				A[j]=A[largest];
				A[largest]=temp;
				j=largest;
			}
			else
			j=heapSize;
		}
		return max;
    }
    
    /**
     * Sorts an input array a quick sort implementation.
     *
     * @param A The array to be sorted
     */
    public static void quickSort(int[] A){
		quickSort(A,0,A.length-1);
    }

    /**
     * Helper function for quickSort, called recursively.
     * 
     * @param A Array to sort.
     * @param low First position of the array.
     * @param high Last position of the array.
     */
    private static void quickSort(int[] A,int low,int high){
		if(low<high){
			int q=DPartition(A,low,high);
			quickSort(A,low,q-1);
			quickSort(A,q+1,high);
		}
    }

    /**
     * Helper function for quickSort, puts values smaller than pivot to the left of it, and values larger than to the right.
     *
     * @param A Array to do work on.
     * @param low First position of the array.
     * @param high Last position of the array.
     * @return int Pivot value used for sorting.
     */
    private static int DPartition(int[] A,int low,int high){
		int p=A[high];
		int i=low;
		int j=high-1;
		while(i<=j){
			while(i<=j&&A[i]<=p){
				i++;
			}
			while(j>=i&&A[j]>=p){
				j--;
			}
			if(i<j){
				int temp=A[j];
				A[j]=A[i];
				A[i]=temp;
			}
		}
		int temp=A[i];
		A[i]=A[high];
		A[high]=temp;
		return i;
    }
    
    /**
     * Sorts an input array an improved quick sort implementation.
     * It is improved by choosing the pivot to be the median of the first, middle and last values of the array.
     * It also stops recursion and uses insertion sort for arrays of small length.
     *
     * @param A The array to be sorted
     */
    public static void quickSortImproved(int[] A){
		quickSortImproved(A,0,A.length-1);
    }

    /**
     * Helper function for quickSortImproved, called recursively.
     * 
     * @param A Array to sort.
     * @param low First position of the array.
     * @param high Last position of the array.
     */
    private static void quickSortImproved(int[] A,int low,int high){
		if(low<high){
			if(high-low<1024)
				insertionSort(A);
			else{
				int q=DPartitionImproved(A,low,high);
				quickSortImproved(A,low,q-1);
				quickSortImproved(A,q+1,high);
			}
		}
    }

    /**
     * Helper function for quickSort, puts values smaller than pivot to the left of it, and values larger than to the right.
     *
     * @param A Array to do work on.
     * @param low First position of the array.
     * @param high Last position of the array.
     * @return int Pivot value used for sorting.
     */
    private static int DPartitionImproved(int[] A,int low,int high){
		int[] tempArray={low,high,(low+high)/2};
		insertionSort(tempArray);
		if(tempArray[0]==A[high]){
			int temp=A[low];
			A[low]=A[high];
			A[high]=temp;
		}
		else if(tempArray[0]==A[(low+high)/2]){
			int temp=A[low];
			A[low]=A[(low+high)/2];
			A[(low+high)/2]=temp;
		}
		if(tempArray[2]==A[(low+high)/2]){
			int temp=A[high];
			A[high]=A[(low+high)/2];
			A[(low+high)/2]=temp;
		}
		int temp=A[high-1];
		A[high-1]=A[(low+high)/2];
		A[(low+high)/2]=temp;
		return DPartition(A,low+1,high-1);
    }

    /**
     * Sorts an input array a quick sort implementation.
     * Has worst case run time of O(n*log(n)).
     *
     * @param A The array to be sorted
     */
    public static void quickSortBonus(int[] A){
		quickSortBonus(A,0,A.length-1);
    }

    /**
     * Helper function for quickSortImproved, called recursively.
     * 
     * @param A Array to sort.
     * @param low First position of the array.
     * @param high Last position of the array.
     */
    private static void quickSortBonus(int[] A,int low,int high){
		if(low<high){
			int q=RPartitionBonus(A,low,high);
			quickSortBonus(A,low,q-1);
			quickSortBonus(A,q+1,high);
		}
    }

    /**
     * Helper function for quickSort, puts values smaller than pivot to the left of it, and values larger than to the right.
     *
     * @param A Array to do work on.
     * @param low First position of the array.
     * @param high Last position of the array.
     * @return int Pivot value used for sorting.
     */
    private static int RPartitionBonus(int[] A,int low,int high){
		int rand=(int)(Math.rint(Math.random()*100))%(high-low)+low;
		int temp=A[rand];
		A[rand]=A[high];
		A[high]=temp;
		return DPartition(A,low,high);
    }
    
    /**
     * Utility function to return a duplicate of an array.
     *
     * @param A The array to be copied
     * @return A copy of the array
     */
    public static int[] duplicate(int[] A){
		int[] B=new int[A.length];
		System.arraycopy(A,0,B,0,A.length);
		return B;
    }
    
    /**
     * Utility function to determine if an array is sorted. Returns true if it
     * is sorted and false otherwise.
     *
     * @param A The array to test if it's sorted
     * @return true if the array is sorted; false otherwise
     */
    public static boolean isSorted(int[] A){
		for(int i=1;i<A.length;i++){
			if(A[i-1]>A[i]){
				return false;
			}
		}
		return true;
    }
    
    /**
     * Utility function to output array to String
     *
     * @param A The array to make into a String
     * @return The String form of the array A
     */
    public static String arrayToString(int[] A){
        String s=("[");
        for(int i=0;i<A.length;i++){
            s+=(A[i]);
            if(i!=A.length-1){
                s+=(", ");
            }
        }
        s+="]";
        return s;
    }
    
    /**
     * Utility function to determine if two arrays are equal
     *
     * @param A The array to test if equal to array B
     * @param B The array to test if equal to array A
     * @return true if the arrays are equal, false otherwise
     */
    public static boolean arrayEquals(int[] A,int[] B){
        if(A==B){
            return true;
        }
        if(A.length!=B.length){
            return false;
        }
        for(int i=0;i<A.length;i++){
            if(A[i]!=B[i]){
                return false;
            }
        }
        return true;
    }
}