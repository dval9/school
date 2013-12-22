/**
 *Assignment 3 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */
 
 /**
 * Class that computes statistics on the height of m different binary search trees created by intertomg n random integers into the tree.
 */
@SuppressWarnings("unchecked")
public class MapExp{

	public static void main(String[] args){
		if(args.length!=2){
			System.out.println("Usage: java MapExp m n");
			System.exit(1);
		}
		int m=Integer.parseInt(args[0]);
		int n=Integer.parseInt(args[1]);
		int min=n;
		int max=-1;
		float average=0;
		BSTMap<Integer,Integer> M;
		java.util.Random seed_generator=new java.util.Random(331);
		for(int tree_count=1;tree_count<=m;tree_count++){
			M=new BSTMap<Integer,Integer>();
			java.util.Random integer_generator=new java.util.Random(seed_generator.nextInt());
			for(int integer_count=1;integer_count<=n;integer_count++){
				int value=integer_generator.nextInt();
				try{
					Integer freq=M.search(value);
					M.modify(value,freq+1);
				}catch(KeyNotFoundException e) {
				   	M.insert(value,1);
				}
			}
			int height=M.height();
			if(height>max)
				max=height;
			if(height<min)
				min=height;
			average=average+height;
		}
		average=average/m;
		System.out.println("Max height = "+max+"\nMin height = "+min+"\nAverage height = "+average);
	}
}