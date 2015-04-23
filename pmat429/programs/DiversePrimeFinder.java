import java.math.BigInteger;

public class DiversePrimeFinder {

	static BigInteger best = new BigInteger("0");
	
	public static void main(String[] args){
		
		String num1 = "987654320";
		String num2 = "987654310";
		String num4 = "987653210";
		String num5 = "987643210";
		String num7 = "986543210";
		String num8 = "976543210";
		
		long start = System.currentTimeMillis();
		
		permute(num1);
		permute(num2);
		permute(num4);
		permute(num5);
		permute(num7);
		permute(num8);
		
		System.out.println("Largest Diverse Prime number is "+best.toString());
		
		System.out.println("Runtime: "+(System.currentTimeMillis()-start)/1000+" seconds");
	}

	static void permute(String str) { 
	    permute("", str); 
	}

	static void permute(String prefix, String str) {
	    int n = str.length();
	    if (n == 0){
	    	BigInteger temp = new BigInteger(prefix);
	    	if(temp.isProbablePrime(5) && (temp.compareTo(best) == 1)){
	    		best = temp;
	    	}
	    }
	    else {
	        for (int i = 0; i < n; i++)
	            permute(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
	    }
	}
}
