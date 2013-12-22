package assign4tester;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import assign4.StationList;
import assign4.StationList.Station;

public class StationTest {
	StationList stationList;

	@Before
	public void setUp() throws Exception {
		stationList=new StationList();
		stationList.addStation("one");
		stationList.addStation("two");
		stationList.addStation("three");
		stationList.setDistance("two", "one", 5);
		stationList.setDistance("three", "one", 10);
		stationList.setDistance("three", "two", 15);
	}

	@After
	public void tearDown() throws Exception {
		stationList=null;
	}

	//checks if the distance between stations can be changed/set
	@Test
	public void testSetDistance() {
		stationList.setDistance("two","one",50);
		assertEquals(stationList.getDistance("one","two"),50,0);
	}

	//tests that a station can be added
	@Test
	public void testAddStation() {
		stationList.addStation("test");
		Object[] temp=stationList.getStations("test");
		assertEquals(((Station) temp[0]).getName(),"test");
		
	}

	//tests if it was possible to remove the desired station
	@Test
	public void testRemoveStation() {
		assertTrue(stationList.removeStation("three"));
	}

	//tests if it was possible to rename a station
	@Test
	public void testRenameStation() {
		stationList.renameStation("one", "new");
		Object[] temp=stationList.getStations("new");
		assertEquals(((Station) temp[0]).getName(),"new");
	}

	//checks to see if all stations were returned, should match the number of stations added in setup
	@Test
	public void testGetStations() {
		assertEquals(stationList.getStations().length,3);
	}

	//checks if stations starting with the string are only returned, should match the number of stations added in setup that start with "t", namely 2 stations
	@Test
	public void testGetStationsString() {
		assertEquals(stationList.getStations("t").length,2);
	}

}
