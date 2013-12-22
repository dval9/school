package assign4;
import java.util.Scanner;
import assign4.TicketList.Ticket;
import assign4.StationList.Station;
import assign4.TicketClassList.TicketClass;

public class Choo2chooseme {
	private static String input;
	private static Scanner in=new Scanner(System.in);
	private static StationList stationList=new StationList();
	private static TicketList ticketList=new TicketList();
	private static TicketClassList ticketClassList=new TicketClassList();
	private static AccountList accountList=new AccountList();
	private static Hardware hardware;
	private static boolean logedIn;
	private static double total=0;
	private static double cost=0;
	
	public static void createNewTicket(){
		ticketList.addTicket();
	}
	
	public static void removeATicket(){
    	Object[] temp=(Object[])ticketList.getTickets();
    	if(temp.length==0){
    		System.out.println("There are no tickets to remove.");
    		return;
    	}
    	System.out.println("Select a ticket to remove:");
    	for(int i=0;i<temp.length;i++){
    		 System.out.println((i+1)+":"+((Ticket) temp[i]).getDeparture()+"\n  "+((Ticket) temp[i]).getDestination()+"\n  "+((Ticket) temp[i]).getType());
    	}
    	System.out.println("Press (C) to cancel.");
    	input=in.nextLine();
    	if(input.equalsIgnoreCase("C"))
    		return;
    	try{
    		ticketList.changeTicket(Integer.parseInt(input));
	    	ticketList.removeTicket();
    	 }catch(Exception e){
	    	 	System.out.println("Not a valid ticket choice.");
	     }
	}

	//not actually used
	public static void selectATicket(){
		Object[] temp=(Object[])ticketList.getTickets();
	   	 for(int i=0;i<temp.length;i++){
	   		 System.out.println((i+1)+":"+((Ticket) temp[i]).getDeparture()+"\n  "+((Ticket) temp[i]).getDestination()+"\n  "+((Ticket) temp[i]).getType());
	   	 }
	   	 System.out.println("Press (C) to cancel.");
	    input=in.nextLine();
	    if(input.equalsIgnoreCase("C"))
	    	return;
	   	 try{
	   		 ticketList.changeTicket(Integer.parseInt(input));
	   	 }catch(Exception e){
	   	 	System.out.println("Not a valid ticket choice.");
	    }
	}

	public static void selectDestination(){
	    Object[] temp=(Object[])stationList.getStations();
	    if(ticketList.getTickets().length==0){
	    	System.out.println("You must first create a ticket.");
	    	return;
	    }
	    System.out.println("Select a station for your destination:");
	    int i;
    	for(i=0;i<temp.length;i++){
    		 System.out.println(((Station) temp[i]).getName());
    	}
    	System.out.println("Press (C) to cancel.");
    	input=in.nextLine();
    	if(input.equalsIgnoreCase("C"))
    		return;
    	if(ticketList.getTicket().getDeparture().equals(input))
    		System.out.println("You cannot select the same station as your departure.");
    	for(i=0;i<temp.length;i++){
    		 if(input.equals(((Station)temp[i]).getName())){
    			 ticketList.setDestination(input);
    		 	return;
    		 }
    	}
    	System.out.println("Invalid selection.");
	}

	public static void selectDeparture(){
	    Object[] temp=(Object[])stationList.getStations();
	    if(ticketList.getTickets().length==0){
	    	System.out.println("You must first create a ticket.");
	    	return;
	    }
	    System.out.println("Select a station for your departure:");
	    int i;
    	for(i=0;i<temp.length;i++){
    		 System.out.println(((Station) temp[i]).getName());
    	}
    	System.out.println("Press (C) to cancel.");
    	input=in.nextLine();
    	if(input.equalsIgnoreCase("C"))
    		return;
    	if(ticketList.getTicket().getDestination().equals(input))
    		System.out.println("You cannot select the same station as your destination.");
    	for(i=0;i<temp.length;i++){
    		 if(input.equals(((Station)temp[i]).getName())){
    			 ticketList.setDeparture(input);
    		 	return;
    		 }
    	}
    	System.out.println("Invalid selection.");
	}

	public static void browse(){
		Object[] temp=(Object[])stationList.getStations();
   	 	for(int i=0;i<temp.length;i++){
   		 System.out.println(((Station) temp[i]).getName());
   	 	}	
	}

	public static void search(){
		System.out.print("Enter station to search for: ");
		System.out.println("Press (C) to cancel.");
    	input=in.nextLine();
    	if(input.equalsIgnoreCase("C"))
    		return;
   	 	Object[] temp=(Object[])stationList.getStations(input);
   	 	for(int i=0;i<temp.length;i++){
   		 System.out.println(((Station) temp[i]).getName());
   	 }
	}

	public static void selectClass(){
		Object[] temp=(Object[])ticketClassList.getTickets();
		if(ticketList.getTickets().length==0){
	    	System.out.println("You must first create a ticket.");
	    	return;
	    }
		System.out.println("Select a class of ticket:");
   	 	System.out.println("Name	:	Cost");
   	 	for(int i=0;i<temp.length;i++){
   	 		System.out.println(((TicketClass) temp[i]).getName()+"	:	"+((TicketClass) temp[i]).getCost());
   	 	}
   	 	System.out.println("Press (C) to cancel.");
   	 	input=in.nextLine();
   	 	if(input.equalsIgnoreCase("C"))
   	 		return;
   	 	for(int i=0;i<temp.length;i++){
   		 if(input.equals(((TicketClass)temp[i]).getName())){
   			 ticketList.setType(input);
   		 	 return;
   		 }
   	 	}
   	 	System.out.println("Invalid selection.");
	}
	
	public static void tryLogin(){
		System.out.println("Press (C) to cancel.");
   	 	System.out.print("Enter username: ");
   	 	input=in.nextLine();
   	 	if(input.equalsIgnoreCase("C"))
	 		return;
   	 	System.out.print("Enter password: ");
   	 	String input2=in.nextLine();
	 	if(input2.equalsIgnoreCase("C"))
	 		return;
	 	logedIn=accountList.verifyLogin(input,input2);
	 	if(logedIn)
	 		System.out.println("Login successful.");
	 	else
	 		System.out.println("Incorrect login info.");
	}

	public static void pay(){
		Object[] temp=(Object[])ticketList.getTickets();
    	if(temp.length==0){
    		System.out.println("There are no tickets to purchase.");
    		return;
    	}
		for(int i=0;i<temp.length;i++){
			if(((Ticket) temp[i]).getDeparture().equals("")){
				System.out.println("Tickets are not filled out.");
				return;
			}
			if(((Ticket) temp[i]).getDestination().equals("")){
				System.out.println("Tickets are not filled out.");
				return;
			}
			if(((Ticket) temp[i]).getType().equals("")){
				System.out.println("Tickets are not filled out.");
				return;
			}
		}
		for(int i=0;i<temp.length;i++){
			cost+=(ticketClassList.getClassCost(((Ticket) temp[i]).getType()))*(stationList.getDistance(((Ticket) temp[i]).getDestination(),((Ticket) temp[i]).getDeparture()));
		}
		total=cost;
		System.out.println("Press (C) to cancel.");
		System.out.println("Cost of ticket is: "+cost);
		while(cost>0){
			System.out.println("Insert card or cash to pay for ticket(s).\nRemaining cost is: "+cost);
			input=in.nextLine();
			if(input.equalsIgnoreCase("C")){
				System.out.println("Canceling ticket payment, returning "+Math.abs(cost-total)+" from payment.");
				return;
			}
			if(hardware.getInputCash(input))//pretend user input
				cost-=Double.parseDouble(input);
			if(input.equalsIgnoreCase("card")){
				if(hardware.getInputCard()){
					System.out.print("Enter pin number:");
					if(hardware.checkPin(in.nextLine()))
						if(hardware.requestFunds(cost)!=0)
							cost-=hardware.requestFunds(cost);	
						else
							System.out.println("Transaction was declined.");
					else
						System.out.println("Invalid pin.");
					if(!hardware.returnCard())
						System.out.println("Error returning card.");
					else
						System.out.println("Returning card.");
				}
			}				
		}
		System.out.println("Printing tickets, and returning "+Math.abs(cost)+" in change.");
		if(!hardware.printTickets(ticketList.getTickets()))
			System.out.println("Error printing tickets.");
		if(!hardware.returnChange(Math.abs(cost)))
			System.out.println("Error returning change.");
	}
	
	public static void addStation(){
		System.out.println("Press (C) to cancel.");
   	 	System.out.print("Enter station name: ");
   	 	String input2=in.nextLine();
	 	if(input2.equalsIgnoreCase("C"))
	 		return;
	 	Object[] temp=stationList.getStations();
	 	for(int i=0;i<temp.length;i++){
	 		if(((Station) temp[i]).getName().equalsIgnoreCase(input2)){
	 			System.out.println("A station with that name already exists.");
	 			return;
	 		}
	 	}
	 	stationList.addStation(input2);
	 	temp=stationList.getStations();
	 	for(int i=0;i<temp.length-1;i++){
	 		System.out.print("Enter distance to station "+((Station) temp[i]).getName()+":");
	 		input=in.nextLine();
	 		stationList.setDistance(input2,((Station) temp[i]).getName(),Double.parseDouble(input));
	 	}
	}
	
	public static void modifyStation(){
		System.out.println("Press (C) to cancel.");
		System.out.println("Choose a station to modify:");
		Object[] temp=(Object[])stationList.getStations();
   	 	for(int i=0;i<temp.length;i++){
   	 		System.out.println(((Station) temp[i]).getName());
   	 	}	
   	 	String input2=in.nextLine();
   	 	if(input2.equalsIgnoreCase("C"))
   	 		return;
   	 	System.out.println("What do you want to do:\nChange name(N).\nChange distances(D).\nFinish modifying(C).");
   	 	do{
   	 		input=in.nextLine();
   	 		if(input.equalsIgnoreCase("N")){
   	 			System.out.print("Enter new name: ");
	    	 	input=in.nextLine();
	    	 	Object[] temp2=stationList.getStations();
	    	 	for(int i=0;i<temp2.length;i++){
	    	 		if(((Station) temp2[i]).getName().equalsIgnoreCase(input))
	    	 			System.out.println("A station with that name already exists.");
	    	 	}
	    	 	stationList.renameStation(input2,input);
	    	 	input2=input;
   	 		}
			else if(input.equalsIgnoreCase("D")){
				System.out.print("Enter station to change distance to: ");
		    	 input=in.nextLine();
		    	 System.out.print("Enter new distance:");
		    	 String dist=in.nextLine();
		    	 stationList.setDistance(input2,input,Double.parseDouble(dist));
			}
			else
				System.out.println("Not a valid selection.");
   	 	}while(!input.equalsIgnoreCase("C"));
	}

	public static void deleteStation(){
		System.out.println("Press (C) to cancel.");
		System.out.println("Choose a station to delete:");
		Object[] temp=(Object[])stationList.getStations();
   	 	for(int i=0;i<temp.length;i++){
   	 		System.out.println(((Station) temp[i]).getName());
   	 	}	
   	 	input=in.nextLine();
   	 	if(input.equalsIgnoreCase("C"))
   	 		return;
   	 	stationList.removeStation(input);
	}

	public static void addTicketClass(){
		System.out.println("Press (C) to cancel.");
   	 	System.out.print("Enter ticket class name: ");
   	 	String input2=in.nextLine();
	 	if(input2.equalsIgnoreCase("C"))
	 		return;
	 	Object[] temp=ticketClassList.getTickets();
	 	for(int i=0;i<temp.length;i++){
	 		if(((TicketClass) temp[i]).getName().equalsIgnoreCase(input2)){
	 			System.out.println("A ticket class with that name already exists.");
	 			return;
	 		}
	 	}
	 	System.out.print("Enter cost of ticket:");
	 	input=in.nextLine();
	 	ticketClassList.addTicketClass(input2,Double.parseDouble(input));
	}

	public static void modifyTicketClass(){
		System.out.println("Press (C) to cancel.");
		System.out.println("Choose a ticket class to modify:");
		Object[] temp=(Object[])ticketClassList.getTickets();
   	 	for(int i=0;i<temp.length;i++){
   	 		System.out.println(((TicketClass) temp[i]).getName());
   	 	}	
   	 	String input2=in.nextLine();
   	 	if(input2.equalsIgnoreCase("C"))
   	 		return;
   	 	System.out.println("What do you want to do:\nChange name(N).\nChange cost(D).\nFinish modifying(C).");
   	 	do{
   	 		input=in.nextLine();
   	 		if(input.equalsIgnoreCase("N")){
   	 			System.out.print("Enter new name: ");
	    	 	input=in.nextLine();
	    	 	Object[] temp2=ticketClassList.getTickets();
	    	 	for(int i=0;i<temp2.length;i++){
	    	 		if(((TicketClass) temp2[i]).getName().equalsIgnoreCase(input))
	    	 			System.out.println("A ticket class with that name already exists.");
	    	 	}
	    	 	ticketClassList.renameClass(input2,input);
	    	 	input2=input;
   	 		}
			else if(input.equalsIgnoreCase("D")){
		    	 System.out.print("Enter new cost:");
		    	 String cost=in.nextLine();
		    	 ticketClassList.changeCost(input2,Double.parseDouble(cost));
			}
			else
				System.out.println("Not a valid selection.");
   	 	}while(!input.equalsIgnoreCase("C"));
	}

	public static void removeTicketClass(){
		System.out.println("Press (C) to cancel.");
		System.out.println("Choose a ticket class to delete:");
		Object[] temp=(Object[])ticketClassList.getTickets();
   	 	for(int i=0;i<temp.length;i++){
   	 		System.out.println(((TicketClass) temp[i]).getName());
   	 	}	
   	 	input=in.nextLine();
   	 	if(input.equalsIgnoreCase("C"))
   	 		return;
   	 	ticketClassList.removeClass(input);
	}
	
	public static boolean logout(){
		logedIn=false;
   	 	System.out.println("Loging out.");
   	 	return logedIn;
	}
	
	public static boolean cancelTransaction(){
		System.out.println("Canceling session.");
		ticketList=new TicketList();
   	 	createNewTicket();
   	 	return true;
	}
	
	public static void main(String[] args){
			//setup stuff so program will run 
			stationList.addStation("one");
			stationList.addStation("two");
			stationList.addStation("three");
			stationList.setDistance("two","one",5);
			stationList.setDistance("three","one",15);
			stationList.setDistance("three","two",10);
			ticketClassList.addTicketClass("low",1);
			ticketClassList.addTicketClass("high",2);
			accountList.addAccount("admin","password");
			logedIn=false;
			ticketList.addTicket();
			ticketList.setDeparture("three");
			ticketList.setDestination("one");
			ticketList.setType("low");
			hardware=new Hardware(5,"1234");
			//end setup stuff
		while(true){
			if(logedIn){
				System.out.println("Currently loged in as administrator. What do you want to do:\nLogout(L).");
				System.out.println("Add station(A).\nModify station(M).\nDelete station(D).");
				System.out.println("Add ticket class(C).\nModify ticket class(T).\nDelete ticket class(R).");
				input=in.nextLine();
				if(input.equalsIgnoreCase("L")){
			    	 logout();
			     }
				else if(input.equalsIgnoreCase("A")){
					addStation();
			     }
				else if(input.equalsIgnoreCase("M")){
					modifyStation();
				 }
				else if(input.equalsIgnoreCase("D")){
					deleteStation();
			     }
				else if(input.equalsIgnoreCase("C")){
					addTicketClass();
			     }
				else if(input.equalsIgnoreCase("T")){
					modifyTicketClass();
			     }
				else if(input.equalsIgnoreCase("R")){
					removeTicketClass();
			     }
			}
			else{
			 System.out.println("\nCurrent ticket:\nDeparture station: "+ticketList.getTicket().getDeparture()+"\nDestination Station: "+ticketList.getTicket().getDestination()+"\nTicket Class: "+ticketList.getTicket().getType());
			 System.out.println("\nWhat do you want to do:\nCreate new ticket(N).\nRemove a ticket(D).\nSet ticket class(C).");
			 System.out.println("Set destination station(E).\nSet departure station(R).\nBrowse stations(B).\nSearch stations(S).");
			 System.out.println("Pay for tickets(P).\nCancel purchase(Q).");
		     input=in.nextLine();
		     if(input.equalsIgnoreCase("N")){
		    	 createNewTicket();
		     }
		     else if(input.equalsIgnoreCase("D")){
		    	 removeATicket();
		     }
		     else if(input.equalsIgnoreCase("E")){
			   selectDestination();
		     }
		     else if(input.equalsIgnoreCase("R")){
		    	 selectDeparture();
		     }
		     else if(input.equalsIgnoreCase("B")){
		    	 browse();
		     }
		     else if(input.equalsIgnoreCase("S")){
		    	 search();
		     }
		     else if(input.equalsIgnoreCase("C")){
		    	 selectClass();
		     }
		     else if(input.equalsIgnoreCase("P")){
			     pay();
		     }
		     else if(input.equalsIgnoreCase("Q")){
		    	cancelTransaction();
		     }
		     else if(input.equalsIgnoreCase("shutdown"))
		    	 System.exit(1);
		     else if(input.equalsIgnoreCase("login")){
		    	 tryLogin();
		     }
		     else
		    	 System.out.println("\nNot a valid choice.\n");
			}
		}
	}
}
