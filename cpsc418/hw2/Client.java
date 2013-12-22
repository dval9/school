import java.io.*;
import java.net.*;

/**
 * Client program.  Connects to the server and sends text accross.
 */

public class Client 
{
    private Socket sock;  //Socket to communicate with.
    private String seed;
    private String source_file;
    private String destination_file;
    static private boolean debug = false;
    private String status;
	static private int args_length;
	
    /**
     * Main method, starts the client.
     * @param args args[0] needs to be a hostname, args[1] a port number.
     */
    public static void main (String [] args)
    {
	args_length = args.length;
	if(args.length == 3){
		if(args[0].equals("debug")){
			debug = true;
			args_length--;
		}
	}
	if (args_length != 2) {
	    System.out.println ("Usage: java Client hostname port#");
	    System.out.println ("hostname is a string identifying your server");
	    System.out.println ("port is a positive integer identifying the port to connect to the server");
	    return;
	}

	try {
		if(debug){
			Client c = new Client (args[1], Integer.parseInt(args[2]));
		}
		else{
			Client c = new Client (args[0], Integer.parseInt(args[1]));
		}
	}
	catch (NumberFormatException e) {
	    System.out.println ("Usage: java Client hostname port#");
	    System.out.println ("Second argument was not a port number");
	    return;
	}
    }
	
    /**
     * Constructor, in this case does everything.
     * @param ipaddress The hostname to connect to.
     * @param port The port to connect to.
     */
    public Client (String ipaddress, int port)
    {
	/* Allows us to get input from the keyboard. */
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	String userinput;
	PrintWriter out;
	DataOutputStream out2;
		
	/* Try to connect to the specified host on the specified port. */
	try {
	    sock = new Socket (InetAddress.getByName(ipaddress), port);
	}
	catch (UnknownHostException e) {
	    System.out.println ("Usage: java Client hostname port#");
	    System.out.println ("First argument is not a valid hostname");
	    return;
	}
	catch (IOException e) {
	    System.out.println ("Could not connect to " + ipaddress + ".");
	    return;
	}
		
	/* Status info */
	System.out.println ("Connected to " + sock.getInetAddress().getHostAddress() + " on port " + port);
		
	try {
	    out = new PrintWriter(sock.getOutputStream());
	    out2 = new DataOutputStream(sock.getOutputStream());
	}
	catch (IOException e) {
	    System.out.println ("Could not create output stream.");
	    return;
	}
		
	/* Wait for the user to type stuff. */
	try {
	    System.out.print("Enter seed: ");
	    seed = stdIn.readLine();
	    System.out.print("Enter source file name: ");
	    source_file = stdIn.readLine();
	    System.out.print("Enter destination file name: ");
	    destination_file = stdIn.readLine();
		if(debug)System.out.println("DEBUG: SENDING DESTINATION FILE NAME");
	    out.println(destination_file);
	    if (out.checkError()) {
		System.out.println ("Client exiting.");
		stdIn.close ();
		out.close ();
		out2.close ();
		sock.close();
		return;
	    }
	    secureFile sf = new secureFile();
	    byte[] data = sf.secure(source_file, seed);	    
	    if(debug)System.out.println("DEBUG: SENDING FILE LENGTH");
	    out.println(data.length);
	    if (out.checkError()) {
		System.out.println ("Client exiting.");
		stdIn.close ();
		out.close ();
		out2.close ();
		sock.close();
		return;
	    }
	    if(debug)System.out.println("DEBUG: SENDING FILE");
		System.out.print("\n");
	    out2.write(data);
		out2.flush();
		if(debug)System.out.println("DEBUG: GETTING RESULT");
		BufferedReader in = new BufferedReader (new InputStreamReader (sock.getInputStream()));
		status = in.readLine();
		if(status.equals("0"))
			System.out.println("Success: File recieved and verified");
		else
			System.out.println("Failure: File not recieved and verified");
	} catch (Exception e) {
	    System.out.println ("error");
	    return;
	}		
    }
}
