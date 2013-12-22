/*CPSC 449 Project- Java 
 *October 10, 2013
 *Group: Mac Haffey, Tom Crowfoot, Christian Daniel, Sukhdeep Bratch, Subhodeep Ray-Chaudhuri
 */

//Main class

public class main{
    public static void main(String[] args){
		if(args.length!=2){
			System.out.println("Incorrect args: java main input_file output_file");
			System.exit(1);
		}
		io reader = new io();
		Datastruct data=new Datastruct();
		reader.read_file(args[0], data, args[1]);
		BBMap tree = new BBMap();
		bb search = new bb(data);
		search.makeTree(tree.root, tree);
		reader.write_file(args[1],data);
    }
}
