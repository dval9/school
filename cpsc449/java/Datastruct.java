/*CPSC 449 Project- Java 
 *October 10, 2013
 *Group: Mac Haffey, Tom Crowfoot, Christian Daniel, Sukhdeep Bratch, Subhodeep Ray-Chaudhuri
 */

//Data structure class

import java.util.ArrayList;

public class Datastruct{
    static ArrayList<mach_task_tuple> forced_partial_assignment = new ArrayList<mach_task_tuple>(); // hard constraint 1
    static ArrayList<mach_task_tuple> forbidden_machine = new ArrayList<mach_task_tuple>(); // hard constraint 2
    static ArrayList<task_task_tuple> too_near_tasks = new ArrayList<task_task_tuple>(); // hard constraint 3
    static int[][] machine_penalties = new int[8][8]; // soft constraint 1
    static ArrayList<mach_task_tuple> scheduled_tasks= new ArrayList<mach_task_tuple>(); //Final list for storing solution
    static ArrayList<task_task_tuple> too_near_penalties = new ArrayList<task_task_tuple>(); // soft constraint 2
    static mach_task_tuple[] solution = new mach_task_tuple[8];
    static mach_task_tuple[] temp_solution = new mach_task_tuple[8];
    static int best_pen = Integer.MAX_VALUE;
    static int current_pen = 0;

    class mach_task_tuple{ // struct to hold machine-task hard constraint pairings
		private int mach;
		private int task;
		mach_task_tuple(int mach, int task){
			this.mach=mach;
			this.task=task;
		}
		int getMach(){
			return mach;
		}
		int getTask(){
			return task;
		}
	}
    class task_task_tuple{ // struct to hold task-task related constraints
		private int task1;
		private int task2;
		private int penalty; // only useful for the soft constraint 2
		task_task_tuple(int task1, int task2){ // hard constraint constructor
			this.task1=task1;
			this.task2=task2;
		}
		task_task_tuple(int task1, int task2, int penalty){ // soft constraint constructor
			this.task1=task1;
			this.task2=task2;
			this.penalty=penalty;
		}
		int getTask1(){
			return task1;
		}
		int getTask2(){
			return task2;
		}
		int getPenalty(){
			return penalty;
		}
    }
	
	//make a new mach_task_tuple, and return it
	mach_task_tuple make_mtt(int mach, int task){
		mach_task_tuple temp = new mach_task_tuple(mach, task);
		return temp;
	}
	
	//make a new task_task_tuple with no penalty, and return it
	task_task_tuple make_ttt(int mach, int task){
		task_task_tuple temp = new task_task_tuple(mach, task);
		return temp;
	}
	
	//make a new task_task_tuple with a penalty, and return it
	task_task_tuple make_ttt(int mach, int task, int penalty){
		task_task_tuple temp = new task_task_tuple(mach, task, penalty);
		return temp;
	}	
}
















