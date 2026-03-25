package model;

public class Station 
{
	int id;
	String name;
	double latitude;
	double longitude;
	boolean isFuelOnly;
	
	public Station(int id, String name, double latitude, double longitude, boolean isFuelOnly)
	{
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isFuelOnly = isFuelOnly;
	}
	
	//Getters
	public int getID()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public double getLatitude()
	{
		return latitude;
	}
	public double getLongitude()
	{
		return longitude;
	}
	public boolean getIsFuelOnly()
	{
		return isFuelOnly;
	}
}
