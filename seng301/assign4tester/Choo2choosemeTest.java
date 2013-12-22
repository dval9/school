package assign4tester;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import assign4.Choo2chooseme;
import assign4.StationList;
import assign4.TicketClassList;
import assign4.TicketList;
import assign4.TicketList.Ticket;

public class Choo2choosemeTest {

	StationList stationList;
	TicketList ticketList;
	TicketClassList ticketClassList;
	boolean logedIn;
	
	@Before
	public void setUp() throws Exception {
		stationList=new StationList();
		ticketList=new TicketList();
		ticketClassList=new TicketClassList();
		stationList.addStation("one");
		stationList.addStation("three");
		stationList.setDistance("three","one",15);
		ticketClassList.addTicketClass("low",1);;
		ticketList.addTicket();
		ticketList.setDeparture("three");
		ticketList.setDestination("one");
		ticketList.setType("low");
		logedIn=true;
	}

	@After
	public void tearDown() throws Exception {
		stationList=null;
		ticketList=null;
		ticketClassList=null;
	}

	//checks if system can calculate total cost, user then would input payment and receive tickets if input money amount==cost
	@Test
	public void testPay() {
		double cost=0;
		Object[] temp=(Object[])ticketList.getTickets();
		for(int i=0;i<temp.length;i++){
			cost+=(ticketClassList.getClassCost(((Ticket) temp[i]).getType()))*(stationList.getDistance(((Ticket) temp[i]).getDestination(),((Ticket) temp[i]).getDeparture()));
		}
		assertEquals(cost,15,0);
	}

	//checks if logout works
	@Test
	public void testLogout() {
		assertFalse(Choo2chooseme.logout());
	}

	//checks if transaction can be canceled 
	@Test
	public void testCancelTransaction() {
		assertTrue(Choo2chooseme.cancelTransaction());
	}

}
