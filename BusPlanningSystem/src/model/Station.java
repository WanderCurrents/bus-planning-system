//    Station Class
//----------------------
//Description: model class that is instantiated to represent a station object
//Attributes:	id : int
//				name : String
//				latitude : double
//				longitude : double
//				supportedTypes : EnumSet<FuelType>
//				isFuelOnly : boolean
//Methods:	Station(id : int, latitude : double, longitude : double, supportedTypes : EnumSet<FuelType>, isFuelOnly : boolean)
//			supports(type : FuelType) : boolean
//			getID() : int
//			getName() : String
//			getLatitude() : double
//			getLongitude() : double
//			getSupportedFuelTypes() : EnumSet<FuelType>
//			getIsFuelOnly() : boolean


package model;

import java.util.EnumSet;


public class Station 
{
	int id;								//Station's ID number in the XML database
	String name;						//Station's name expressed as a string
	double latitude;					//Station's geographic latitude expressed as a double in degrees
	double longitude;					//Station's geographic longitude expressed as a double in degrees
	EnumSet<FuelType> supportedTypes;	//List that has the supported fuel types expressed as an EnumSet of FuelType enums
	boolean isFuelOnly;					//Boolean flag that determines if the station is a refuel-only station
	
	//Main constructor
	public Station(int id, String name, double latitude, double longitude, EnumSet<FuelType> supportedTypes, boolean isFuelOnly)
	{
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.supportedTypes = supportedTypes;
		this.isFuelOnly = isFuelOnly;
	}
	
	//Method that checks that the fuel type passed in is found in the list of supported fuel types, returns boolean value
	public boolean supports(FuelType type)
	{
		return supportedTypes.contains(type);
	}
	
	//----------------------GETTERS----------------------
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
	
	//"Not all storms come to disrupt your life, some come to clear your path." -Paulo Coelho
	
	public EnumSet<FuelType> getSupportedFuelTypes()
	{
		return EnumSet.copyOf(supportedTypes);
	}
	
	public boolean getIsFuelOnly()
	{
		return isFuelOnly;
	}
}
