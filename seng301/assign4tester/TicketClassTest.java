package assign4tester;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import assign4.TicketClassList;

public class TicketClassTest {
	TicketClassList ticketClassList;
	
	@Before
	public void setUp() throws Exception {
		ticketClassList=new TicketClassList();
		ticketClassList.addTicketClass("init1", 1);
		ticketClassList.addTicketClass("init2", 3);
	}

	@After
	public void tearDown() throws Exception {
		ticketClassList=null;
	}

	//test to check adding a class of ticket, it should be able to find the new class by name and return the correct cost
	@Test
	public void testAddTicketClass() {
		ticketClassList.addTicketClass("class", 5);
		assertEquals(ticketClassList.getClassCost("class"),5,0);
	}

	//check if the method removed the object from its list, returns true if successful otherwise false
	@Test
	public void testRemoveClass() {
		assertTrue(ticketClassList.removeClass("init1"));
	}

	//check if a rename was successful, returns false if it was not able to do it
	@Test
	public void testRenameClass() {
		assertTrue(ticketClassList.renameClass("init1", "newName"));
	}

	//check if it was possible to change the cost, returns false if unsuccessful
	@Test
	public void testChangeCost() {
		assertTrue(ticketClassList.changeCost("init2", 5));
	}

}
