package assign4tester;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import assign4.AccountList;

public class AccountTest {
	AccountList accountList;
	
	@Before
	public void setUp() throws Exception {
		accountList=new AccountList();
		accountList.addAccount("admin","password");
	}

	@After
	public void tearDown() throws Exception {
		accountList=null;
	}

	//test to see if login is correct, this case should return true as the account is added in setup
	@Test
	public void testLogin() {
		assertTrue(accountList.verifyLogin("admin", "password"));
	}
	
	//test to see if login is correct, this case should return false as no such account should exist
	@Test
	public void testNotAuthenticated() {
		assertFalse(accountList.verifyLogin("notadmin", "notpassword"));
	}

}
