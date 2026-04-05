package model;

import java.util.EnumSet;

public class Station 
{
	int id;
	String name;
	double latitude;
	double longitude;
	EnumSet<FuelType> supportedTypes;
	boolean isFuelOnly;
	
	public Station(int id, String name, double latitude, double longitude, EnumSet<FuelType> supportedTypes, boolean isFuelOnly)
	{
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.supportedTypes = supportedTypes;
		this.isFuelOnly = isFuelOnly;
	}
	
	//Getters & other fun methods
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
	public boolean supports(FuelType type)	//Checks if a certain fuel type is supported by the station by checking the enum set for it
	{
		return supportedTypes.contains(type);
	}
	public EnumSet<FuelType> getSupportedFuelTypes()
	{
		return EnumSet.copyOf(supportedTypes);
	}
	public boolean getIsFuelOnly()
	{
		return isFuelOnly;
	}
}
