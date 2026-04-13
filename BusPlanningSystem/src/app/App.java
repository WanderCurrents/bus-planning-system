package app;

import xml.UserManager;
import xml.StationManager;
import xml.BusManager;

import java.util.ArrayList;

//FOR TESTING ONLY, REMOVE THESE
import model.*;

public class App {

	public static void main(String[] args) 
	{
		
		UserManager um = null;
		StationManager sm = null;
		BusManager bm = null;
		try 
		{
		    um = new UserManager();
		    sm = new StationManager();
		    bm = new BusManager();
		} catch(Exception e) {
		    System.out.print("Error creating managers. Error: ");
		    e.printStackTrace();
		}

		ArrayList<Station> testStations = new ArrayList<>();
		testStations.add(sm.getStation(1));	//Add Augusta station
		testStations.add(sm.getStation(4));	//Add Atlanta station
//		testStations.add(sm.getStation(3)); //Add Charlotte station
		Bus bus1 = new Bus(99, "test", "Infinity tank bus 1", FuelType.GAS, 9999, 1, 80);
		Bus bus2 = new Bus(98, "test", "medium range bus", FuelType.GAS, 1, 1, 60);
		
		RoutingSystem rs = new RoutingSystem(bus2, sm);
		try 
		{
			rs.planRoute(testStations);
		} catch (Exception e) 
		{
			System.out.print("Error calling plan route. Error: ");
			e.printStackTrace();
		}

		ArrayList<Leg> resultPlan = rs.getTravelPlan();
		System.out.println("Bus: " + bus2.getMakeModel() + " Range: " + bus2.getMaxRange());
		for(Leg leg : resultPlan)
		{
			System.out.println(
			        "Leg: " + leg.getStartStation().getName() +
			        " → " + leg.getEndStation().getName() +
			        " | Dist: " + leg.getDist() +
			        " | Heading: " + leg.getHeading() +
			        " | Time: " + leg.getTime()
			    );

		}
	}

}
