package assign4tester;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import assign4.Hardware;

public class HardwareTest {
		Hardware hardware;
		
	@Before
	public void setUp() throws Exception {
		hardware=new Hardware(20,"1234");
	}

	@After
	public void tearDown() throws Exception {
		hardware=null;
	}

	//tests if hardware can accept cash
	@Test
	public void testGetInputCash() {
		assertTrue(hardware.getInputCash("10"));
	}

	//tests that the hardware can accept a card
	@Test
	public void testGetInputCard() {
		assertTrue(hardware.getInputCard());
	}

	//checks if valid pin accepted
	@Test
	public void testCheckPin() {
		assertTrue(hardware.checkPin("1234"));
	}
	
	//checks if invalid pin is rejected
	@Test
	public void testCheckPinInvalid() {
		assertFalse(hardware.checkPin("1243"));
	}
	
	//checks that hardware returns maximum funds only
	@Test
	public void testReceiveMaxFunds() {
		assertEquals(hardware.requestFunds(30),20,0);
	}
	
	//checks that hardware returns 0 if funds is set to 0
	@Test
	public void testTransactionDeclined() {
		hardware=new Hardware(0,"");
		assertEquals(hardware.requestFunds(20),0,0);
	}

}
