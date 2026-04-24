// RoutingSystem Class
//----------------------
//Description: class that is instantiated at the beginning of the routing system process
//Attributes: 	stationManager : StationManager
//				travelPlan : ArrayList<Leg>
//				refuelStationsInRange : ArrayList<Station>
//				bus : Bus
//Methods:	RoutingSystem(bus : Bus, stationManager : StationManager)
//			getTravelPlan() : ArrayList<Leg>
//			getRefuelStationsInRange() : ArrayList<Station>
//			planRoute(stops : ArrayList<Station>) : void
//			planLeg(startStation : Station, endStation : Station) : void
//			canReachDestination(distToEnd : double) : boolean
//			findStationsInRangeCloserThanCurrent(current : Station, end : Station) : ArrayList<Station>
//			pickClosestStationToEnd(list : ArrayList<Station>, end : Station) : Station
//			addLeg(start : Station, end : Station) : void
//			insertRefuelStation(currentStation : Station, endStation : Station) : Station

package app;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import model.Bus;
import model.Station;
import model.FuelType;
import model.Leg;
import model.Geo;
import xml.StationManager;

public class RoutingSystem 
{
	StationManager stationManager;											//Same StationManager created in App
	private ArrayList<Leg> travelPlan = new ArrayList<>();					//The list of legs in the travel plan
	private ArrayList<Station> refuelStationsInRange = new ArrayList<>();	//Temporary list of stations that the bus can reach from current station
	private Bus bus;														//Bus that we're routing
	
	//Main constructor for Routing System
	public RoutingSystem(Bus bus, StationManager stationManager)	
	{
		this.bus = bus;
		this.stationManager = stationManager;	//Dependency injection for the Station Manager, only one exists still
	}
	
	//Getter for getting the travel plan list
	public ArrayList<Leg> getTravelPlan() 
	{
		return travelPlan;
	}
	
	//Getter for getting the refuel stations in range list
	public ArrayList<Station> getRefuelStationsInRange() 
	{
		return refuelStationsInRange;
	}
	
	//Primary method and entry point for planning a route (backend)
	public void planRoute(ArrayList<Station> stops) throws Exception
	{
		travelPlan = new ArrayList<>();
		
		for(int i = 0; i < stops.size() - 1; i++)
		{
			planLeg(stops.get(i), stops.get(i+1));
		}
	}
	
	//A single-leg solver method called by other methods
	private void planLeg(Station startStation, Station endStation) throws Exception 
	{	
		//Set current station to the starting station
		Station currentStation = startStation;
		Station next;
		
		//Check for various things and enter the different phases of leg planning
		while(!currentStation.equals(endStation))
		{
			double distToEnd = Geo.calcDist(currentStation.getLatitude(), currentStation.getLongitude(), endStation.getLatitude(), endStation.getLongitude());
	
			//Phase 1: If we can reach the destination directly, go there
			if(canReachDestination(distToEnd))
			{
				addLeg(currentStation, endStation);
				currentStation = endStation;
				
				continue;
			}
			else
			{
				//Phase 2: If not, find any stations in range AND that are closer than current station (don't go backwards)
				ArrayList<Station> candidates = findStationsInRangeCloserThanCurrent(currentStation, endStation);
				
				if(candidates.isEmpty())
				{
					//Phase 2.1: No suitable candidate stops -> insert a refuel station
					next = insertRefuelStation(currentStation, endStation);
				}
				else
				{
					//Phase 2.2: Suitable candidates found -> find the best one
					next = pickClosestStationToEnd(candidates, endStation);
				}
				
				//Phase 3: Safety check, make sure we actually made progress
				double newDist = Geo.calcDist(next.getLatitude(), next.getLongitude(), endStation.getLatitude(), endStation.getLongitude());
				if(newDist >= distToEnd)
				{
					throw new Exception("Routing failed: next station attempted does not reduce distance.");
				}
			}
			
			//Phase 4: Add the leg to the travel plan
			addLeg(currentStation, next);
			currentStation = next;
		}	
	}
	
	//Helper method to return a true/false value if the bus can reach the distance to the end
	private boolean canReachDestination(double distToEnd)
	{
		return bus.getMaxRange() >= distToEnd;
	}
	
	//Helper method to return a list of any station in range and is closer to the destination than the current station
	private ArrayList<Station> findStationsInRangeCloserThanCurrent(Station current, Station end)	//Beautiful method name I know, you're welcome
	{
		ArrayList<Station> list = new ArrayList<>();
		
		double distCurrentToEnd = Geo.calcDist(current.getLatitude(), current.getLongitude(), end.getLatitude(), end.getLongitude());
		for(Station s : stationManager.getStationList())
		{
			if(s.equals(current))
			{
				continue;	//If the station element is the current one, skip
			}
			
			double distToS = Geo.calcDist(current.getLatitude(), current.getLongitude(), s.getLatitude(), s.getLongitude());	//Distance from start to station element
			if(distToS > bus.getMaxRange())
			{
				continue;	//If the station element is too far away, skip
			}
			if(!s.supports(bus.getFuelType()))
			{
				continue;	//If the station element doesn't support fuel type for bus, skip
			}
			
			double distSToEnd = Geo.calcDist(s.getLatitude(), s.getLongitude(), end.getLatitude(), end.getLongitude());	//Distance from station element to target
			if(distSToEnd < distCurrentToEnd)
			{
				list.add(s);	//If going to this station element means we will be closer to the target than being at the current station, add it to the list
			}
		}
		
		return list;
	}
	
	//Helper method that takes a list of any station in range (see findStationsInRangeCloserThanCurrent method) and picks the best option
	private Station pickClosestStationToEnd(ArrayList<Station> list, Station end)
	{
		Station best = list.get(0);
		double bestDist = Geo.calcDist(best.getLatitude(), best.getLongitude(), end.getLatitude(), end.getLongitude());
		
		for(int i = 1; i < list.size(); i++)
		{
			Station s = list.get(i);	//Check station element
			double d = Geo.calcDist(s.getLatitude(), s.getLongitude(), end.getLatitude(), end.getLongitude());	//Distance of check station element to target
			if(d < bestDist)	//If this new check station element is closer, make it best
			{
				best = s;
				bestDist = d;
			}
		}
		
		return best;
	}
	
	//Helper method that adds a leg to the travelPlan
	private void addLeg(Station start, Station end)
	{
		travelPlan.add(new Leg(start, end, bus));
	}
	
	//Helper method that inserts a refuel station in the travel plan which requires adding a new leg
	private Station insertRefuelStation(Station currentStation, Station endStation) throws Exception 
	{
			System.out.println("\nTravel plan is impossible due to lack of refueling facilities. Inserting refuel station to plan.");
			
			int rStationID = stationManager.getIDIndex();	//Asks the manager for the next available ID for naming and object creation
			String rStationName = ("RefuelStation" + Integer.toString(rStationID));
			
			//Calculate how far along the route the station should be
			double distFromCurrentToEnd = Geo.calcDist(currentStation.getLatitude(), currentStation.getLongitude(), endStation.getLatitude(), endStation.getLongitude());
			double calcPortionOfRoute = ((bus.getMaxRange()-5) / distFromCurrentToEnd); //this is out of 1
			//Prevent tiny movements
			if(calcPortionOfRoute < 0.05)
			{
				calcPortionOfRoute = 0.05;	//Always move at least 5% forward
			}
			//Prevent overshooting the destination
			if(calcPortionOfRoute > 0.95)
			{
				calcPortionOfRoute = 0.95;	//Never place station too close to target
			}
			
			//"Your past already took enough from you, don't let it take your future too." -Unknown wise person
			
			//Calculate coordinates for the location
			double rLongitude = (currentStation.getLongitude() + calcPortionOfRoute * (endStation.getLongitude() - currentStation.getLongitude()));
			double rLatitude = (currentStation.getLatitude() + calcPortionOfRoute * (endStation.getLatitude() - currentStation.getLatitude()));
			
			EnumSet<FuelType> supported = EnumSet.of(bus.getFuelType());
			boolean isFuelOnly = true;
			
			//Create the station object
			Station newRefuelStation = new Station(rStationID, rStationName, rLatitude, rLongitude, supported, isFuelOnly);

			//Add it to the SAME manager that the App created, this saves it permanently
			stationManager.addStation(rStationName, rLatitude, rLongitude, supported, isFuelOnly); //why can we not do StationManager.add(...)?
			
			return newRefuelStation;
	}
}
