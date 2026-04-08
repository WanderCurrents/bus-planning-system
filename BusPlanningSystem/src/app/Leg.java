package app;

import model.Station;

public class Leg 
{
	private Station startStation; 
	private Station endStation;
	private double dist;
	private double heading;
	private double time;

	Leg(Station startStation, Station endStation, double dist, double heading, double time ) 
	{
		this.startStation = startStation;
		this.endStation = endStation;
		this.dist = dist;
		this.heading = heading;
		this.time = time;
	}

	public Station startStation()
	{
		return startStation;
	}

	public Station endStation() 
	{
		return endStation;
	}

	public double dist() 
	{
		return dist;
	}

	public double heading() 
	{
		return heading;
	}

	public double time() 
	{
		return time;
	}
}
