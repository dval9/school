/**
 * Tom Crowfoot
 * 10037477
 * CPSC 335 Assignment 2 B-Tree program
 * http://algs4.cs.princeton.edu/62btrees/BTree.java.html
 */
import java.io.BufferedReader;
import java.io.FileReader;

public class as2{
    private static String file_name;
    private static final int M=5;//order of tree
    private static int[] nums;
    private Node root;// root of the tree
    private int height;//height of tree(root only =1)
    private int sizeNodes;//size of tree
    private int sizeKeys;//number of keys in tree
    
    //node data type
    private class Node{
	private int m;//number of keys in node(max is M-1)
	private Key[] keys=new Key[M];//array with keys size M to make splitting easier, but will only ever fill up M-1
	private Node(int k){
	    m=k;
	}
    }

    //stored in nodes, has values and the nodes to branch to
    private class Key{
	private int val;//the number stored
	private Node left;//left child node
	private Node right;//right child node
	Key(int val,Node left,Node right){
	    this.val=val;
	    this.left=left;
	    this.right=right;
	}
    }

    as2(){
	read();
	root=new Node(0);
	height=1;
	sizeNodes=1;
	sizeKeys=0;
    }

    //inserts the key int the tree
    private void insert(int k){
	//if(k==search(k))//duplicate so do not insert
	//    return;
	if(height==1){//special case when tree only contains root
	    int i=0;
	    for(i<root.m;i++){
		if(k<root.keys[i]){
		    for(int z=root.m;z>i;z--){//shift everything one to right
			root.keys[z]=root.keys[z-1];
		    }
		}
		root.keys[i]=new Key(k,null,null);
	    }
	    //check to see if root is full
	    //if it is must split
	    return;
	}
	return;
	//find approperate node(non root only cases)
	//recursive?
    }

    //searches tree for given int, returns -1 if not found
    private int search(int k){
	Key temp=search(root,k);
	while(temp!=null){
	    if(k==temp.val)
		return temp.val;
	    else if(k<temp.val)
		temp=search(temp.left,k);
	    else
		temp=search(temp.right,k);	    
	}
	return -1;
    }

    //binary searches the node for the key, if key not found returns key to compare and then branch
    private Key search(Node node,int k){
	if(node==null)
	    return null;
	int min=0;
	int max=(node.m);
	int mid=-1;//should never return -1
	if(min==max)
	    return node.keys[min];
	while(min<=max){
	    mid=min+(max-min)/2;
	    if(k<(node.keys[mid]).val)
		max=mid-1;
	    else if(k>(node.keys[mid]).val)
		min=mid+1;
	    else
		return node.keys[mid];
	}
	return node.keys[mid];
    }

    //reads values from file to array to use
    private static void read(){
	try{
	    int i=0;
	    BufferedReader br=new BufferedReader(new FileReader(file_name));
	    while(br.readLine()!=null)
	    	i++;
	    nums=new int[i];
	    br=new BufferedReader(new FileReader(file_name));
	    for(int j=0;j<i;j++)
		nums[j]=Integer.parseInt(br.readLine());
	    br.close();
	}catch(Exception e){
	    System.out.println("error reading file");
	    System.exit(1);
	}
    }

    //arg0 is file_name
    public static void main(String[] args){
	try{
	    file_name=args[0];
	}catch(Exception e){
	    System.out.println("incorrect args or filename --> 'java as2 file_name'");
	    System.exit(1);
	}
	as2 bTree=new as2();
	//for(int i=0;i<10;i++)
	//    bTree.insert(nums[i]);
	for(int i=0;i<10;i++)
	    System.out.println(nums[i]+":"+bTree.search(nums[i]));
    }
}

