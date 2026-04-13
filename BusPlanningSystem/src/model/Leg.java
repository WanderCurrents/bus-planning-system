package model;

import model.Geo;
public class Leg
{
	private Station startStation; 
	private Station endStation;
	private double dist;
	private double heading;
	private double time;

	public Leg(Station startStation, Station endStation, Bus bus) 
	{
		this.startStation = startStation;
		this.endStation = endStation;
		this.dist = Geo.calcDist(startStation.getLatitude(), startStation.getLongitude(), endStation.getLatitude(), endStation.getLongitude());
		this.heading = Geo.calcHeading(startStation.getLatitude(), startStation.getLongitude(), endStation.getLatitude(), endStation.getLongitude());
		this.time = Geo.calcTime(startStation, endStation, bus);
	}

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
