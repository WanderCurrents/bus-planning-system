//      Leg Class
//----------------------
//Description: model class that is instantiated to represent a leg of a travel plan
//Attributes:	startStation : Station
//				endStation : Station
//				dist : double
//				heading : double
//				time : double
//Methods:	Leg(startStation : Station, endStation : Station, bus : Bus)
//			getStartStation() : Station
//			getEndStation() : Station
//			getDist() : double
//			getHeading() : double
//			getTime() : double

package model;


public class Leg
{
	private Station startStation;	//Starting station of the leg of the trip
	private Station endStation;		//Ending station of the leg of the trip
	private double dist;			//Distance in miles of the leg of the trip
	private double heading;			//Heading in degrees that the bus travels on this leg of the trip
	private double time;			//Time in hours that the bus travels on this leg of this trip

	//Main constructor
	public Leg(Station startStation, Station endStation, Bus bus) 
	{
		this.startStation = startStation;
		this.endStation = endStation;
		this.dist = Geo.calcDist(startStation.getLatitude(), startStation.getLongitude(), endStation.getLatitude(), endStation.getLongitude());
		this.heading = Geo.calcHeading(startStation.getLatitude(), startStation.getLongitude(), endStation.getLatitude(), endStation.getLongitude());
		this.time = Geo.calcTime(startStation, endStation, bus);
	}

	//----------------------GETTERS----------------------
	public Station getStartStation()
	{
		return startStation;
	}

	public Station getEndStation() 
	{
		return endStation;
	}

	public double getDist() 
	{
		return dist;
	}

	public double getHeading() 
	{
		return heading;
	}

	public double getTime() 
	{
		return time;
	}
}
