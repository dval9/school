/**
 * assignment 1 cpsc 335
 * tom crowfoot 10037477
 * extendible hashing program
 * program works with variable bucket sizes
 * program does not work with creating an index and binary searching
 * the website http://underpop.free.fr/j/java/algorithims-in-java-1-4/ch16lev1sec4.htm was used to get some ideas for how to structure the code 
 */
 

import java.io.*;//reading file

public class as1{
    private class Bucket{
		String[] words;//things stored in bucket
		int size;//current size of bucket
		int depth;//number of bits for which items are identical
		Bucket(){
			words=new String[bucket_size];
			size=0;
			depth=0;
		}
    }

	static String file_name;//name of file with words
    static int number_keys;//number of keys to read/hash
    static int bucket_size;//maximum size of bucket
    String[] word_list;//list of words read
    Bucket[] dir;//list of buckets
    int local_depth;//number of bits to use
    int global_depth;//size of directory
    static int probes=0;//for computing average number of comparisons to find a word
    static int probes1=0;//^^^

    as1(){//directory starts containing only one bucket
		local_depth=0;
		global_depth=1;
		dir=new Bucket[global_depth];
		dir[0]=new Bucket();
		readFile();
    }

	//searches for the correct bucket to look in then calls helper function
    String search(String word){probes1++;//buckets searched
		return search(dir[getBits(word,local_depth)>>>1],word);
    }

	//helper method for search, linearly searches the bucket for the word
    private String search(Bucket b,String word){
	for(int j=0;j<b.size;j++){probes++;//this for looking up average number of compares
			if(word.equals(b.words[j]))
				return b.words[j];
		}
		return null;
    }
    
	//creates new directory and adds the bucket created by split
	private void addDir(Bucket b,int d){
		int i;
		int m; 
		String s=b.words[0];
		int x=getBits(s,d-1);  
		while(local_depth<d){ 
			Bucket[] old=dir; 
			local_depth+=1;
			global_depth+=global_depth;
			dir=new Bucket[global_depth];
			for(i=0;i<global_depth;i++)
				dir[i]=old[i/2]; 
			for(i=0;i<global_depth/2;i++)
			    old[i]=null;
			if(local_depth<d)
				dir[getBits(s,local_depth)^1]=new Bucket(); 
		} 
		for(m=1;d<local_depth;d++)
			m*=2;
		for(i=0;i<m;i++)
			dir[x*m+i]=b;
	}
  
	//splits the contents of an overflowed bucket, calls addDir to add the new bucket to the directory
	private void split(Bucket b){
		Bucket t=new Bucket();
		while(b.size==0||b.size>=bucket_size){ 
			b.size=0;
			t.size=0; 
			int ib=0;
			int it=0;
			for(int j=0;j<bucket_size;j++){
				if(b.words[j]==null)
				    j=bucket_size;
				else if(getBit(b.words[j],b.depth)==0){
					b.words[ib++]=b.words[j];
					b.size+=b.words[j].length();
					if((ib-1)!=j)
						b.words[j]=null;
				}
				else{
					t.words[it++]=b.words[j];
					t.size+=b.words[j].length();
				}
			}
			b.depth+=1;
			t.depth=b.depth;
			sort(b);
		} 
		addDir(t,t.depth);
	} 
	
	//helper function for insert, calls split if bucket overflows from addition
	private void insert(Bucket b, String word){
		int i;
		for(i=0;i<bucket_size;i++){
			if(b.words[i]==null){
				b.words[i]=word;
				break;
			}
		}
		b.size+=b.words[i].length(); 
		if(b.size>=bucket_size)
			split(b); 
	}
	
	//insert a word into table
	void insert(String word){
		insert(dir[getBits(word,local_depth)>>>1],word);
	} 

	//returns single bit from specified position
	int getBit(String word,int bit){
		return ((getHash(word)>>>(31-bit))&1);
	}
		
	//returns specified largest bits
    int getBits(String word,int bits){
		return (getHash(word)>>>(31-bits));
    }

	//hashes string with s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1], xor with 0xffffffff and reverses the bits ie bit 0 and 31 swap
	//reversing included because many words do not use bits > 20 or so
    int getHash(String word){
	return Integer.reverse(word.hashCode()^0xFFFFFFFF);
    }

	//sorts the null values to end of bucket 
	void sort(Bucket b){
		int i=0;
		int j=0;
		while(j<bucket_size){
			if(b.words[j]!=null)
				j++;
			else{
				for(i=bucket_size-1;i>j;i--){
					if(b.words[i]!=null){
						b.words[j]=b.words[i];
						b.words[i]=null;
					}
				}
				j++;
			}				
		}
	}
	
    //reads words from file, file_name and number_keys specified at command line
    private void readFile(){
		try{
			BufferedReader br=new BufferedReader(new FileReader(file_name));
			word_list=new String[number_keys];
			for(int i=0;i<number_keys;i++)
				word_list[i]=br.readLine();
			br.close();
		}catch(Exception e){
			System.out.println("Error reading file");
			System.exit(1);
		}
    }

    //program takes three args, args[0]=file_name, args[1]=number_keys, args[2]=bucket_size
    public static void main(String[] args){
		try{
			file_name=args[0];
			number_keys=Integer.parseInt(args[1]);
			bucket_size=Integer.parseInt(args[2]);
		}catch(Exception e){
			System.out.println("incorrect args --> 'java as1 file_name number_keys bucket_size'");
			System.exit(1);
		}
		as1 r=new as1();
		for(int z=0;z<number_keys;z++)
			r.insert(r.word_list[z]);
		//for(int z=0;z<number_keys;z++)
		//        System.out.println(r.word_list[z]+":"+r.search(r.word_list[z]));
		for(int z=0;z<number_keys;z++)
		    r.search(r.word_list[z]);
		System.out.println("Average: "+probes/number_keys+", Buckets searched: "+probes1/number_keys);
	}    
}



























