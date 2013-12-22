/*CPSC 449 Project- Java 
 *October 10, 2013
 *Group: Mac Haffey, Tom Crowfoot, Christian Daniel, Sukhdeep Bratch, Subhodeep Ray-Chaudhuri
 */

//bb class

import java.util.*;
public class bb 
{
	int stack_pointer = 0;
 	Stack<BBMap.Node> stack = new Stack<BBMap.Node>();
	Datastruct data = null;
	char[] taskArray = {'R', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N'}; //so it worst case it would be [R,A,B,C,D,E,F,G,H] 
	static ArrayList <Character> alreadyUsedTask = new ArrayList<Character>();
	static ArrayList <Character> alreadyUsedMach = new ArrayList<Character>();
	
	//intitalizes all info about hard constraits
	public bb (Datastruct input)
	{
		data = input;
		int counter = 0;
		//splites the tasks and machines up so the only tasks avaliable will be those not allready assigned, and the only machines that
		// will be explored are ones that have not already been assigned because of hard constraint #1
		while(counter < data.scheduled_tasks.size())
		{
			alreadyUsedTask.add((char)data.scheduled_tasks.get(counter).getTask());
			alreadyUsedMach.add((char)data.scheduled_tasks.get(counter).getMach());
			counter = counter + 1;
		}
		initTaskArray();
		counter = 0;
		while(counter < 9 && taskArray[counter]!= 'N')
		{
			//System.out.println(taskArray[counter]);
			counter = counter + 1;
		}
	}
	
	// initalizes task to be used based on which ones have not yet been assigned
	private void initTaskArray ()
	{
		int size = 1;
		if (!(alreadyUsedTask.contains('A')))
		{
			taskArray[size] = 'A';
			size =size + 1;
		}
		if (!(alreadyUsedTask.contains('B')))
		{
			taskArray[size] = 'B';
			size = size + 1;
		}
		if (!(alreadyUsedTask.contains('C')))
		{
			taskArray[size] = 'C';
			size = size + 1;
		}
		if (!(alreadyUsedTask.contains('D')))
		{
			taskArray[size] = 'D';
			size = size + 1;
		}
		if (!(alreadyUsedTask.contains('E')))
		{
			taskArray[size] = 'E';
			size = size + 1;
		}
		if (!(alreadyUsedTask.contains('F')))
		{
			taskArray[size] = 'F';
			size = size + 1;
		}
		if (!(alreadyUsedTask.contains('G')))
		{
			taskArray[size] = 'G';
			size = size + 1;
		}
		if (!(alreadyUsedTask.contains('H')))
		{
			taskArray[size] = 'H';
			size = size + 1;
		}
	}


	// itterates through the list to check if the machine and task are forbidden
	private boolean forbbiden (char mach, char task)
	{
		boolean forb = false;
		char curTask = 'N';
		char  curMach = 'N';
		int counter = 0;
		while (counter < data.forbidden_machine.size())
		{
			curMach = (char)data.forbidden_machine.get(counter).getMach();
			curTask = (char)data.forbidden_machine.get(counter).getTask();
			if ((curMach == mach) && (curTask == task))
			{
				forb = true;
			}
			counter = counter + 1;
		}		
		return forb;
	}


	// This method looks at the first too_near pair then gose looking through
	// the parts off the tree that is already made to see if makeing
	// Node n would cause that pair to exist. If yes return true
	// if no return false
	private boolean tooNear(char task, char mach)
	{
		boolean near = false;	
		int counter = 0;
        int inCount = 0;
        char task1 = 'N';
        char task2 = 'N';
        while (counter < data.too_near_tasks.size())
        {
			task1 = (char)data.too_near_tasks.get(counter).getTask1();
			task2 = (char)data.too_near_tasks.get(counter).getTask2();
			inCount = 0;
			while ( (inCount < 8) && data.temp_solution[inCount] != null){
				if ((data.temp_solution[inCount].getMach() + 1) == (int)mach){
					if ((task1 == (char)data.temp_solution[inCount].getTask()) && (task2 == task)){
						near = true;
					}
				}
				if ((data.temp_solution[inCount].getMach() - 1) == (int)mach){
					if ((task1 == task) && (task2 == (char)data.temp_solution[inCount].getTask())){
						near = true;
					}
				}
				if (((char)data.temp_solution[inCount].getMach() == '1') && (mach == '8')){
					if ((task1 == task) && (task2 == (char)data.temp_solution[inCount].getTask())){
					near = true;
					}
				}
				if (((char)data.temp_solution[inCount].getMach() == '8') && (mach == '1')){
					if ((task1 == (char)data.temp_solution[inCount].getTask()) && (task2 == task)){
						near = true;
					}
				}
				inCount = inCount + 1;
			}
			counter = counter + 1;                  
		}
		return near;
	}
	
	// calculate too near penaltys
	// called once when we have a full solution
	// iterates through all too-near penalties
	// on each iteration, checks for the task in our temp solution
	// it then remembers the machine assigned to task1 and task2
	// then it checks if mach1 and mach2 are next to eachother, if they are add the penalty
	int tooNearPens(){
		int temp = 0;
		for(int i=0;i<data.too_near_penalties.size();i++){
			int mach1 = '0';
			int mach2 = '0';
			for(int j=0;j<stack_pointer;j++){
				if(data.temp_solution[j].getTask() == data.too_near_penalties.get(i).getTask1())
					mach1 = data.temp_solution[j].getMach();
				if(data.temp_solution[j].getTask() == data.too_near_penalties.get(i).getTask2())
					mach2 = data.temp_solution[j].getMach();
			}
			if((mach1+1 == mach2) || (mach1 == '8' && mach2 == '1'))
				temp+=data.too_near_penalties.get(i).getPenalty();
		}
		return temp;
	}
	
	void calc_current_pen(){
		data.current_pen = 0;
		for(int i=0;i<8;i++){
			data.current_pen+=data.machine_penalties[data.temp_solution[i].getMach()-'1'][data.temp_solution[i].getTask()-'A'];
		}
	}
	
	
	/* This function basically makes the tree in a depth-first fashion
	   First the 'block' function is called to make and machine that
	   are already in use unexpandable. So if machine 1 is apart of the
	   solution path then you won't generte another machine 1 in the same path.
	   then it ensures we're not indexing out of the taskArray not sure is we
	   need this bit) then it will recursivley expand its self.*/

	public void makeTree(BBMap.Node n, BBMap searchTree)
	{
		try{
		for (Datastruct.mach_task_tuple m : data.forced_partial_assignment)
		{
			data.temp_solution[stack_pointer] = m;
			data.solution[stack_pointer++] = m;
		}
		int size = 0;
		stack.push(searchTree.root);
		while(!(stack.empty()))
		    {///*COMMENT*/for(int ff=0;ff<stack_pointer;ff++){System.out.print("("+(char)data.temp_solution[ff].getMach()+(char)data.temp_solution[ff].getTask()+")");}
						//System.out.print(stack.size()+"\n");
		    if (stack_pointer == 8)
			{
				int too_near_pens = tooNearPens();
				calc_current_pen();///*COMMENT*/System.out.println(" pens for ^ curr/best:"+(data.current_pen + too_near_pens)+"/"+data.best_pen);
			    if (data.current_pen + too_near_pens < data.best_pen)
				{
				    System.arraycopy(data.temp_solution, 0, data.solution, 0, 8);
				    data.best_pen = data.current_pen + too_near_pens;
				}
			}
		    

		   //System.out.print("M: " + n.mach + ", T: " + n.task);
			if (n.task != 'R')
			{
				//System.out.println(", P: " + n.parent.mach);
			}
			// prevents already made machines from being made again
			searchTree.block(n); // becomes useless work while backing up but not a lot to worry about
	  		char Ptask = n.task;
			int taskPos = -1;
			int counter = 0;
			// what was the parents task
			while ((counter <= 8) && (taskArray[counter] != Ptask))
			{
				counter = counter + 1;
			}
			// childs task is one after the parents class
			taskPos = counter + 1;
			// if we are down to past the last task, then we're at a leaf node so get out, NOTE: will have to log the solution somewhere
			if((taskPos > 8) || (taskArray[taskPos] == 'N'))
			{ 
			    stack.pop();
				stack_pointer--;
				if(!stack.empty())
				n = stack.peek();// get runtime error here, empty stack exception
			}
  			else if ((n.oneChild == null) && (!(alreadyUsedMach.contains('1'))) && (forbbiden('1', taskArray[taskPos]) == false) && (tooNear(taskArray[taskPos], '1') == false))
			{
				char task = taskArray[taskPos];
				searchTree.addOneChild(n, task);
  				stack.push(n.oneChild);
   				n.oneChild.visited = true;
   				//Do something with soft and hard constraints here?
   				//Now search with node on top of stack
   				n = n.oneChild;
				data.temp_solution[stack_pointer++] = data.make_mtt(n.mach, n.task);
  			}
			else if((n.twoChild == null) && (!(alreadyUsedMach.contains('2'))) && (forbbiden('2', taskArray[taskPos]) == false) && (tooNear(taskArray[taskPos], '2') == false))
			{
				char task = taskArray[taskPos];
				searchTree.addTwoChild(n, task);
  				stack.push(n.twoChild);
   				n.twoChild.visited = true;
   				//Do something with soft and hard constraints here?
   				//Now search with node on top of stack
   				n = n.twoChild;
				data.temp_solution[stack_pointer++] = data.make_mtt(n.mach, n.task);			
  			}

			else if((n.threeChild == null) && (!(alreadyUsedMach.contains('3'))) && (forbbiden('3', taskArray[taskPos]) == false) && (tooNear(taskArray[taskPos], '3') == false))
			{
				char task = taskArray[taskPos];
				searchTree.addThreeChild(n, task);
  				stack.push(n.threeChild);
   				n.threeChild.visited = true;
   				//Do something with soft and hard constraints here?
   				//Now search with node on top of stack
   				n = n.threeChild;
				data.temp_solution[stack_pointer++] = data.make_mtt(n.mach, n.task);
  			}

			else if((n.fourChild == null) && (!(alreadyUsedMach.contains('4'))) && (forbbiden('4', taskArray[taskPos]) == false) && (tooNear(taskArray[taskPos], '4') == false))
			{
				char task = taskArray[taskPos];
				searchTree.addFourChild(n, task);
  				stack.push(n.fourChild);
   				n.fourChild.visited = true;
   				//Do something with soft and hard constraints here?
   				//Now search with node on top of stack
   				n = n.fourChild;
				data.temp_solution[stack_pointer++] = data.make_mtt(n.mach, n.task);
  			}

			else if((n.fiveChild == null) && (!(alreadyUsedMach.contains('5'))) && (forbbiden('5', taskArray[taskPos]) == false) && (tooNear(taskArray[taskPos], '5') == false))
			{
				char task = taskArray[taskPos];
				searchTree.addFiveChild(n, task);
  				stack.push(n.fiveChild);
   				n.fiveChild.visited = true;
   				//Do something with soft and hard constraints here?
   				//Now search with node on top of stack
   				n = n.fiveChild;
				data.temp_solution[stack_pointer++] = data.make_mtt(n.mach, n.task);
  			}

			else if((n.sixChild == null) && (!(alreadyUsedMach.contains('6'))) && (forbbiden('6', taskArray[taskPos]) == false) && (tooNear(taskArray[taskPos], '6') == false))
			{
				char task = taskArray[taskPos];
				searchTree.addSixChild(n, task);
  				stack.push(n.sixChild);
   				n.sixChild.visited = true;
   				//Do something with soft and hard constraints here?
   				//Now search with node on top of stack
   				n = n.sixChild;
				data.temp_solution[stack_pointer++] = data.make_mtt(n.mach, n.task);
  			}

			else if((n.sevenChild == null) && (!(alreadyUsedMach.contains('7'))) && (forbbiden('7', taskArray[taskPos]) == false) && (tooNear(taskArray[taskPos], '7') == false))
			{
				char task = taskArray[taskPos];
				searchTree.addSevenChild(n, task);
  				stack.push(n.sevenChild);
   				n.sevenChild.visited = true;
   				//Do something with soft and hard constraints here?
   				//Now search with node on top of stack
   				n = n.sevenChild;
				data.temp_solution[stack_pointer++] = data.make_mtt(n.mach, n.task);
  			}

			else if((n.eightChild == null) && (!(alreadyUsedMach.contains('8'))) && (forbbiden('8', taskArray[taskPos]) == false) && (tooNear(taskArray[taskPos], '8') == false))
			{
				char task = taskArray[taskPos];
				searchTree.addEightChild(n, task);
  				stack.push(n.eightChild);
   				n.eightChild.visited = true;
   				//Do something with soft and hard constraints here?
   				//Now search with node on top of stack
   				n = n.eightChild;
				data.temp_solution[stack_pointer++] = data.make_mtt(n.mach, n.task);
  			}  			
			// might not need
  			else
			{
  				 //this is when the node n has no children anymore
  				 //remove from stack
				stack.pop();
				stack_pointer--;
				if (!(stack.isEmpty()))
				{
					n = stack.peek();
				}
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
 	}
}
