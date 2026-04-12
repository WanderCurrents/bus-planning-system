package app;

import java.util.ArrayList;
import java.util.EnumSet;

import model.Bus;
import model.Station;
import model.FuelType;
import xml.StationManager;

public class RoutingSystem 
{
	StationManager stationManager;											//Same StationManager created in App
	private ArrayList<Leg> travelPlan = new ArrayList<>();					//The final list of legs
	private ArrayList<Station> refuelStationsInRange = new ArrayList<>();	//Temporary list of stations that the bus can reach from current station
	private Station nextRefuelStation;										//A variable that ends up holding the chosen next station temporarily
	private Bus bus;														//Bus that we're routing
	
	public RoutingSystem(Bus bus, StationManager stationManager)	//Main constructor for Routing System
	{																//Requires bus and StationManager parameters
		this.bus = bus;
		this.stationManager = stationManager;	//Dependency injection for the Station Manager, only one exists still
	}
	
	public ArrayList<Leg> getTravelPlan() 
	{
		return travelPlan;
	}
	
	public ArrayList<Station> getRefuelStationsInRange() 
	{
		return refuelStationsInRange;
	}
	
	
	
	public static double calcDist(double longitude1, double latitude1, double longitude2, double latitude2) 
	{
		double dLat = latitude2 - latitude1;
		double dLon = longitude2 - longitude1;

		double latAvg = (latitude1 + latitude2) / 2.0;
		double milesPerLon = 69.0 * Math.cos(Math.toRadians(latAvg));

		double x = dLon * milesPerLon;
		double y = dLat * 69.0;

		double distance = Math.sqrt(x*x + y*y);
		return distance;
	}
	public static double calcHeading(double longitude1, double latitude1, double longitude2, double latitude2) 
	{		
		double differenceLon = longitude2 - longitude1;
		double differenceLat = latitude2 - latitude1; 

	    // Convert degrees to miles
	    double latitudeAvg = (latitude1 + latitude2) / 2.0;
	    double milesPerLong = 69.0 * Math.cos(Math.toRadians(latitudeAvg));

	    double uLong = differenceLon * milesPerLong;  // east-west
	    double uLat = differenceLat * 69.0;  
		
	    double cosVal = (uLat/(Math.sqrt(uLong * uLong + uLat *uLat)));
	    
	    double theta = Math.toDegrees(Math.acos(cosVal));
	    
	    double headingDegrees;
	    if(uLong >= 0) //X is right of North
	    	headingDegrees = theta;
	    else //X is left of North
	    	headingDegrees = 360-theta;
	    
	    return headingDegrees;
	}
	public static double calcTime(Station startStation, Station endStation, double cruiseSpeed)
	{
		double distance = calcDist(startStation.getLongitude(), startStation.getLatitude(), endStation.getLongitude(), endStation.getLatitude());
		
		double time = (distance/cruiseSpeed);
		
		return time;
	}
	
	
	public Station insertRefuelStation(Station currentStation, Station endStation) throws Exception 
	{
		
			System.out.println("Travel plan is impossible due to lack of refueling facilities. Inserting refuel station to plan.");
			
			int rStationID = stationManager.getIDIndex();	//Asks the manager for the next available ID for naming and object creation
			String rStationName = ("RefuelStation" + Integer.toString(rStationID));
			
			//Calculate how far along the route the station should be
			double distFromCurrentToEnd = calcDist(currentStation.getLongitude(), currentStation.getLatitude(), endStation.getLongitude(), endStation.getLatitude());
			double calcPortionOfRoute = (bus.getMaxRange() / distFromCurrentToEnd); //this is out of 1
			
			//Calculate coordinates for the location
			double rLongitude = (currentStation.getLongitude() + calcPortionOfRoute * (endStation.getLongitude() - currentStation.getLongitude()));
			double rLatitude = (currentStation.getLatitude() + calcPortionOfRoute * (endStation.getLatitude() - currentStation.getLatitude()));
			
			EnumSet<FuelType> supported = EnumSet.of(bus.getFuelType());
			boolean isFuelOnly = true;
			
			//Create the station object
			Station newRefuelStation = new Station(rStationID, rStationName, rLongitude, rLatitude, supported, isFuelOnly);

			//Add it to the SAME manager that the App created, this saves it permanently
			stationManager.addStation(rStationName, rLatitude, rLongitude, supported, isFuelOnly); //why can we not do StationManager.add(...)?
			
			return newRefuelStation;
	}
	
	public void planRoute(ArrayList<Station> stops) throws Exception
	{
		travelPlan = new ArrayList<>();
		
		for(int i = 0; i < stops.size() - 1; i++)
		{
			planLeg(stops.get(i), stops.get(i+1));
		}
	}
	//A single-leg solver
	public void planLeg(Station startStation, Station endStation) throws Exception 
	{
		Station currentStation = startStation;	//Sets the starting point at the starting station
		
		while(!currentStation.equals(endStation))
		{
			double distCurrentToEnd = calcDist(currentStation.getLongitude(), currentStation.getLatitude(), endStation.getLongitude(), endStation.getLatitude());
			
			Station nextStation;
			
			if(bus.getMaxRange() >= distCurrentToEnd) //If it is possible then just add it to the travel plan
			{
				//double heading = calcHeading(currentStation.getLongitude(), currentStation.getLatitude(), nextStation.getLongitude(), nextStation.getLatitude());
				//double time = calcTime(currentStation, nextStation, bus.getCruiseSpeed());
				
				//Leg leg = new Leg(currentStation, nextStation, distCurrentToEnd, heading, time); necessary or no since this is done later on at the end
				//travelPlan.add(leg);
				
				nextStation = endStation;
			}
			else //First, look for possible stations in range
			{
				refuelStationsInRange = new ArrayList<>();
				
				//Create a list of stations in range
				for(Station s : stationManager.getStationList())  //Look through s objects in the entire list of stations looking for stations in range
				{
					if(!s.equals(currentStation)) //Makes sure the station element looked at isn't the starting station
					{
						double distToRefuelStation = calcDist(currentStation.getLongitude(), currentStation.getLatitude(), s.getLongitude(), s.getLatitude());
						
						//Check for any Station in range and supports that fuelType
						if(distToRefuelStation < bus.getMaxRange() && s.supports(bus.getFuelType()))
						{
							refuelStationsInRange.add(s); //If it does add it to rStationInRange
						}
					}
				}
				//Check if list is empty, meaning no stations in range
				if(refuelStationsInRange.isEmpty())
				{
					nextStation = insertRefuelStation(currentStation, endStation);	//Add a refuel station
				}
				//If stations found in range, pick the best one
				else 
				{
					//Check which rStation is best
					nextStation = refuelStationsInRange.get(0); //saves the station with the min distance (aka best distance)
					
					//get distance from the nextStation to endStation
					double bestDistFromStationToEnd = calcDist(nextStation.getLongitude(), nextStation.getLatitude(), endStation.getLongitude(), endStation.getLatitude());
					
					for(int x = 1; x < refuelStationsInRange.size(); x++) //iterates through the ArrayList
					{
						Station possibleStation = refuelStationsInRange.get(x);
						
						//get distance from each possibleStation to endStation
						double refuelToEndDist = calcDist(possibleStation.getLongitude(), possibleStation.getLatitude(), endStation.getLongitude(), endStation.getLatitude());
						
						//compare the two distances 
						if(refuelToEndDist < bestDistFromStationToEnd)
						{
							nextStation = possibleStation;
							bestDistFromStationToEnd = refuelToEndDist;
						}
					}
				}
			}
			
			//Build the leg
			double legDistance = calcDist(currentStation.getLongitude(), currentStation.getLatitude(), nextStation.getLongitude(), nextStation.getLatitude());
			double heading = calcHeading(currentStation.getLongitude(), currentStation.getLatitude(), nextStation.getLongitude(), nextStation.getLatitude());
			double time = calcTime(currentStation, nextStation, bus.getCruiseSpeed() );
			Leg leg = new Leg(currentStation, nextStation, legDistance, heading, time);
			travelPlan.add(leg);
			currentStation = nextStation;	//Move to the next station
		}		
		
	}
}
