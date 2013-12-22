import java.net.*;
import java.io.*;

/**
 * Thread to deal with clients who connect to Server.  Put what you want the
 * thread to do in it's run() method.
 */

public class ServerThread extends Thread
{
    private Socket sock;  //The socket it communicates with the client on.
    private Server parent;  //Reference to Server object for message passing.
    private int idnum;  //The client's id number.
    private boolean debug = false;
    private String seed;
    private String destination_file;
    private int check;
	
    /**
     * Constructor, does the usual stuff.
     * @param s Communication Socket.
     * @param p Reference to parent thread.
     * @param id ID Number.
     */
    public ServerThread (Socket s, Server p, int id, boolean d)
    {
	parent = p;
	sock = s;
	idnum = id;
	debug = d;
    }
	
    /**
     * Getter for id number.
     * @return ID Number
     */
    public int getID ()
    {
	return idnum;
    }
	
    /**
     * Getter for the socket, this way the parent thread can
     * access the socket and close it, causing the thread to
     * stop blocking on IO operations and see that the server's
     * shutdown flag is true and terminate.
     * @return The Socket.
     */
    public Socket getSocket ()
    {
	return sock;
    }
	
    /**
     * This is what the thread does as it executes.  Listens on the socket
     * for incoming data and then echos it to the screen.  A client can also
     * ask to be disconnected with "exit" or to shutdown the server with "die".
     */
    public void run ()
    {
	BufferedReader in = null;
	DataInputStream in2 = null;
	String incoming = null;
		
	try {
	    in = new BufferedReader (new InputStreamReader (sock.getInputStream()));
	    in2 = new DataInputStream(sock.getInputStream());
	}
	catch (UnknownHostException e) {
	    System.out.println ("Unknown host error.");
	    return;
	}
	catch (IOException e) {
	    System.out.println ("Could not establish communication.");
	    return;
	}
	try{
	    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	    System.out.print("Enter seed: ");
	    seed = stdIn.readLine();	
	}catch(Exception e){
	    //e.printStackTrace();
	}
	/* Try to read from the socket */
	try {
	    decryptFile df = new decryptFile();		
	    incoming = in.readLine ();
		if(debug)System.out.println("DEBUG: GETTING FILE NAME");
	    destination_file = incoming;
	    System.out.println ("Output file: " + incoming);
		if(debug)System.out.println("DEBUG: GETTING FILE LENGTH");
	    incoming = in.readLine ();
	    byte[] data = new byte[Integer.parseInt(incoming)];
	    System.out.println ("File size: " + incoming);
		if(debug)System.out.println("DEBUG: GETTING FILE");
	    in2.read (data);
	    check = df.decrypt(data, destination_file, seed);
	    if(check == 0)
		System.out.println("Success");
	    if(check != 0)
		System.out.println("Fail");
		if(debug)System.out.println("DEBUG: SENDING RESULT");
		PrintWriter out = new PrintWriter(sock.getOutputStream());
		out.println(String.valueOf(check));
		out.checkError();
	}
	catch (Exception e) {
	    System.out.println ("error");
	    return;
	}
	return;
    }					
}