package app;

import java.util.ArrayList;
import java.util.EnumSet;

import model.Bus;
import model.Station;
import model.FuelType;
import xml.StationManager;

public class RoutingSystem 
{
	private ArrayList<Leg> travelPlan = new ArrayList<>();
	private ArrayList<Station> refuelStationsInRange = new ArrayList<>();
	private Station nextRefuelStation;
	private Bus bus;
	private double maxRange;
	
	public ArrayList<Leg> getTravelPlan() 
	{
		return travelPlan;
	}
	
	public ArrayList<Station> getRefuelStationsInRange() 
	{
		return refuelStationsInRange;
	}
	
	public Bus getBus()
	{
		return bus;
	}
	
	public void setMaxRange(double fuelCapacity, double gallons, double speed) 
	{
		this.maxRange = (fuelCapacity/gallons) * speed;
	}
	
	public double getMaxRange()
	{
		return maxRange;
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
	
	public Station refuelStation(Station currentStation, Station endStations) throws Exception 
	{
		
			System.out.println("Travel plan is impossible due to lack of refueling facilities. Inserting refuel station.");
			
			int rStationID = StationManager.getIDIndex();
			String rStationName = ("RefuelStation" + Integer.toString(rStationID));
			
			double distance = calcDist(currentStation.getLongitude(), currentStation.getLatitude(), endStations.getLongitude(), endStations.getLatitude());
			double calcPortionOfRoute = (maxRange/distance);
			
			double rLongitude = (currentStation.getLongitude() + calcPortionOfRoute * (endStations.getLongitude()-currentStation.getLongitude()));
			double rLatitude = (currentStation.getLatitude() + calcPortionOfRoute * (endStations.getLatitude() - currentStation.getLatitude()));
			
			EnumSet<FuelType> supported = EnumSet.of(bus.getFuelType());
			boolean isFuelOnly = true;
			
			Station newRefuelStation = new Station(rStationID, rStationName, rLongitude, rLatitude, supported, isFuelOnly);
			StationManager stationManager = new StationManager();
			stationManager.addStation(rStationName, rLatitude, rLongitude, supported, isFuelOnly);
			
			return newRefuelStation;
	}
	
	public static double time(double distance, double speed)
	{
		double time = (distance/speed);
		return time;
	}
	
	public void planRoute(Station startStation, Station endStation) 
	{
		travelPlan = new ArrayList<>(); //travel plan for this 
		Station currentStation = startStation;
		
		while(!currentStation.equals(endStation))
		{
			double distance = calcDist(currentStation.getLongitude(), currentStation.getLatitude(), endStation.getLongitude(), endStation.getLatitude());
			
			Station nextStation = endStation;
			
			if(maxRange >= distance) //if it is possible then just add it to the travel plan
			{
				double heading = calcHeading(currentStation.getLongitude(), currentStation.getLatitude(), nextStation.getLongitude(), nextStation.getLatitude());
				double time = time(distance, );
				
				Leg leg = new Leg(currentStation, nextStation, distance, heading, time);
				travelPlan.add(leg);
				
				currentStation = nextStation;
			}
			else //look for possible stations in range
			{
				refuelStationsInRange = new ArrayList<>();
				
				for(Station stationInRange: StationManager.list) // The getStation method expects an integer argument.
				{
					if(!stationInRange.equals(currentStation))
					{
						double distToRefuelStation = calcDist(currentStation.getLongitude(), currentStation.getLatitude(), stationInRange.getLongitude(), stationInRange.getLatitude());
						
						//check for anyStation in range that supports that fuelType
						if(distToRefuelStation < maxRange && stationInRange.supports(bus.getFuelType()))
						{
							refuelStationsInRange.add(stationInRange); //if it does add it to rStationInRange
						}
					}
				
				}
				//check if list is empty
				if(!refuelStationsInRange.isEmpty())
				{
					//check which rStation is best
					nextStation = refuelStationsInRange.get(0);
					
					for(int x = 1; x < refuelStationsInRange.size(); x++)
					{
						Station possibleStation = refuelStationsInRange.get(x);
						
						//get distance from each possibleStation to endStation
						double rToEndDistance = calcDist(possibleStation.getLongitude(), possibleStation.getLatitude(), endStation.getLongitude(), endStation.getLatitude());
						//get distance from the nextStation to endStation
						double bestStation = calcDist(nextStation.getLongitude(), nextStation.getLatitude(), endStation.getLongitude(), endStation.getLatitude());
						//compare the two distances 
						if(rToEndDistance < bestStation )
						{
							nextStation = possibleStation;
						}
					}
				}
				else 
				{
					nextStation = refuelStation(currentStation, endStation);
				}
				
			}
			double legDistance = calcDist(currentStation.getLongitude(), currentStation.getLatitude(), nextStation.getLongitude(), nextStation.getLatitude());
			double heading = calcHeading(currentStation.getLongitude(), currentStation.getLatitude(), nextStation.getLongitude(), nextStation.getLatitude());
			double time = time(distance, );
			Leg leg = new Leg(currentStation, nextStation, distance, heading, time);
			travelPlan.add(leg);
			currentStation = nextStation;
		}
		
		
	}
}
