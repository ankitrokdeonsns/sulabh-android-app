package com.thoughtworks.sulabh.gateways;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.thoughtworks.sulabh.Loo;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class SulabhGatewayTest {

	private DB db;
	private MongoCollection locations;

	@Before
	public void setUp() throws Exception {
		db = new MongoClient().getDB("sulabh");
		Jongo jongo = new Jongo(db);
		locations = jongo.getCollection("locations");
		locations.remove();
	}

	@Test
	public void showsLoosAround2kmRadiusOfYourCurrentLocation() throws UnknownHostException {
		String name = "abcd";
		double latitude = 18.55586959;
		double longitude = 73.89150673;
		Loo loo = new Loo(new String[]{"Men","Women"},"Indian",true, false, true, 5, new double[]{latitude, longitude}, "abcd");
		locations.save(loo);
		List<Loo> looList = new SulabhGateway().getLoos(18.54, 73.89, 2);
		assertEquals(1, looList.size());
		for (Loo loo1 : looList) {
			assertEquals(name, loo1.getName());
			assertEquals(latitude, loo1.getCoordinates()[0]);
			assertEquals(longitude, loo1.getCoordinates()[1]);
		}
	}
}