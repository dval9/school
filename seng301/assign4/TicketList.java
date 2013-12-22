package assign4;
import java.util.ArrayList;

/*
 * Contains list of tickets.
 */
public class TicketList {
	private ArrayList<Ticket> tickets=new ArrayList<Ticket>();
	private Ticket currentTicket;
	
	/*
	 * Structure for the ticket.
	 * Each ticket will require two stations, and a type.
	 */
	public class Ticket{
		private String departure="";
		private String destination="";
		private String type="";
		
		public String getDeparture(){
			return departure;
		}
		
		public String getDestination(){
			return destination;
		}
		
		public String getType(){
			return type;
		}
	}
	
	public void setDestination(String destination){
		currentTicket.destination=destination;
	}
	
	public void setDeparture(String departure){
		currentTicket.departure=departure;
	}
	
	public void setType(String type){
		currentTicket.type=type;
	}
	
	public void changeTicket(int i){
		currentTicket=tickets.get(i-1);
	}

	//adds a ticket to the list
	public void addTicket(){
		tickets.add(new Ticket());
		currentTicket=tickets.get(tickets.size()-1);
	}
	
	//removes ticket from list
	public void removeTicket(){
		tickets.remove(currentTicket);
		if(tickets.size()!=0)
			currentTicket=tickets.get(tickets.size()-1);
	}
	
	public Ticket getTicket(){
		return currentTicket;
	}
	
	//returns all tickets
	public Object[] getTickets(){
		return (Object[])tickets.toArray();
	}
}
