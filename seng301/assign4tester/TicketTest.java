package assign4tester;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import assign4.StationList;
import assign4.TicketClassList;
import assign4.TicketList;

public class TicketTest {
	StationList stationList;
	TicketList ticketList;
	TicketClassList ticketClassList;
	@Before
	public void setUp() throws Exception {
		stationList=new StationList();
		ticketList=new TicketList();
		ticketClassList=new TicketClassList();
		stationList.addStation("one");
		stationList.addStation("two");
		stationList.setDistance("two","one",5);
		ticketClassList.addTicketClass("high",2);
		ticketList.addTicket();
	}

	@After
	public void tearDown() throws Exception {
		stationList=null;
		ticketList=null;
		ticketClassList=null;
	}

	//tests setting the destination for ticket
	@Test
	public void testSetDestination() {
		ticketList.setDestination("one");
		assertEquals(ticketList.getTicket().getDestination(),"one");
	}
	
	//tests setting the departure for the ticket
	@Test
	public void testSetDeparture() {
		ticketList.setDeparture("two");
		assertEquals(ticketList.getTicket().getDeparture(),"two");
	}

	//tests setting a destination for the ticket
	@Test
	public void testSetType() {
		ticketList.setType("high");
		assertEquals(ticketList.getTicket().getType(),"high");
	}

	//tests adding a ticket, if the old number of tickets if one less than the length after adding, it was successful
	@Test
	public void testAddTicket() {
		int count=ticketList.getTickets().length;
		ticketList.addTicket();
		int count2=ticketList.getTickets().length;
		assertEquals(count+1,count2);
	}

	//tests if a ticket was removed, the new number of tickets should be one less than before
	@Test
	public void testRemoveTicket() {
		int count=ticketList.getTickets().length;
		ticketList.removeTicket();
		int count2=ticketList.getTickets().length;
		assertEquals(count-1,count2);
	}

}
