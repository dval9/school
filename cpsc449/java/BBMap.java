/*CPSC 449 Project- Java 
 *October 10, 2013
 *Group: Mac Haffey, Tom Crowfoot, Christian Daniel, Sukhdeep Bratch, Subhodeep Ray-Chaudhuri
 */

//BBMap Class

public class BBMap 
{
	Node root;
	//Each node will represent a machine "mach" that is assigned a task "task".
	//When making a new node all children are null and upon a method call to do so
	// it will be assigned some children nodes.
	public class Node
    	{
		//public for testing, should be private
        	public char mach;
		public char task;
		public int pathPenalty;
		public Node parent;
		public Node oneChild;
	    	public Node twoChild;
	    	public Node threeChild;
	    	public Node fourChild;
	    	public Node fiveChild;
	    	public Node sixChild;
	    	public Node sevenChild;
	    	public Node eightChild;
		public boolean visited;
           
        public Node (char m, char t, Node p)
		{
	            mach = m;
	            task = t;
				parent = p;
				//pathPenalty = p.pathPenalty; // penalty keeps track of the total penalties on a path through the tree, not sure if this is the right place for it
		    	oneChild = null;
		    	twoChild = null;
		    	threeChild = null;
		    	fourChild = null;
		    	fiveChild = null;
		    	sixChild = null;
		    	sevenChild = null;
		    	eightChild = null;
			visited = false;
		}
	

	public boolean kidzFull()
	{
		 boolean full = false;
		if ((oneChild != null) && (twoChild != null) && (threeChild != null) && (fourChild != null) && (fiveChild != null) && (sixChild != null) && (sevenChild != null) && (eightChild != null))
		{
			full = true;
		}
		return full;
	}
	}

	//adds penelty to node to help with keeping track of soft constriants
	public void addPenalty (int penalty, Node n)
	{
		n.pathPenalty += penalty; 
	}
	
	//these 8 proceeding functions will make a child with task "task" to Node "n"
	public void addOneChild(Node n, char task)
	{
		Node mach1 = new Node('1', task, n);
		n.oneChild = mach1;
	} 

	public void addTwoChild(Node n, char task)
	{
		Node mach2 = new Node('2', task, n);
		n.twoChild = mach2;
	}
 
	public void addThreeChild(Node n, char task)
	{
		Node mach3 = new Node('3', task, n);
		n.threeChild = mach3;
	}
 
	public void addFourChild(Node n, char task)
	{
		Node mach4 = new Node('4', task, n);
		n.fourChild = mach4;
	}
 
	public void addFiveChild(Node n, char task)
	{
		Node mach5 = new Node('5', task, n);
		n.fiveChild = mach5;
	}
 
	public void addSixChild(Node n, char task)
	{
		Node mach6 = new Node('6', task, n);
		n.sixChild = mach6;
	}
 
	public void addSevenChild(Node n, char task)
	{
		Node mach7 = new Node('7', task, n);
		n.sevenChild = mach7;
	}
 
	public void addEightChild(Node n, char task)
	{
		Node mach8 = new Node('8', task, n);
		n.eightChild = mach8;
	} 


	// Expands the selected node to the next task and adds appropriate children
	// It's kinda big and could be made smaller but it is rather convienent this way
	public void block (Node n)
	{
		Node current = n;
		char task = 'N';
		boolean one = true;
		boolean two = true;
		boolean three = true;
		boolean four = true;
		boolean five = true;
		boolean six = true;
		boolean seven = true;
		boolean eight = true;
		// this while loop iterates through the parents and turns each machine 
		// that has already been used to false
		while (current.mach != 'R')
		{
			if (current.mach == '1')
			{
				one = false;
			}
			else if (current.mach == '2')
			{
				two = false;
			}
			else if (current.mach == '3')
			{
				three = false;
			}
			else if (current.mach == '4')
			{
				four = false;
			}
			else if (current.mach == '5')
			{
				five = false;
			}
			else if (current.mach == '6')
			{
				six = false;
			}
			else if (current.mach == '7')
			{
				seven = false;
			}
			else if (current.mach == '8')
			{
				eight = false;
			}
			current = current.parent;
		}

		// the following set of conditionals bassically says
		// if the machine is has not been set to false(already in use)
		// then make a child out of it
		if (one == false)
		{
			addOneChild(n, task);
		}
		if (two == false)
		{
			addTwoChild(n, task);
		}		
		if (three == false)
		{
			addThreeChild(n, task);
		}
		if (four == false)
		{
			addFourChild(n, task);
		}
		if (five == false)
		{
			addFiveChild(n, task);
		}
		if (six == false)
		{
			addSixChild(n, task);
		}
		if (seven == false)
		{
			addSevenChild(n, task);
		}
		if (eight == false)
		{
			addEightChild(n, task);
		}	
	}


	public void printChild (Node n)
	{
		System.out.println(n.oneChild.mach);
	}

	//Inital state of BBMap will be a root and 8 machines as children all running task 'A'
    	public BBMap ()
    	{
		root = new Node ('R', 'R', null); // set root to invalid values so it does not get confused with a machine
		root.pathPenalty = 0;
    	}
    	
	//Recursive dfs algorithm
	public static void DFSTraverse(Node n){
		if(n !=null){
			//Add penalty of current node
			DFSTraverse(n.oneChild);
			DFSTraverse(n.twoChild);
			DFSTraverse(n.threeChild);
			DFSTraverse(n.fourChild);
			DFSTraverse(n.fiveChild);
			DFSTraverse(n.sixChild);
			DFSTraverse(n.sevenChild);
			DFSTraverse(n.eightChild);
			}
			}
			
				
	

		
}
