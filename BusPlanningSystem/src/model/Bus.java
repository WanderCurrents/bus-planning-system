//      Bus Class
//----------------------
//Description: model class that is instantiated to represent a bus object
//Attributes: 	id : int
//				makeModel : String
//				type : String
//				fuelType : FuelType
//				fuelSize : int
//				fuelBurn : int
//				cruiseSpeed : int
//Methods:	Bus(id : int, makeModel : String, type : String, fuelType : FuelType, fuelSize : int, fuelBurn : int, cruiseSpeed : int)
//			getID() : int
//			getMakeModel() : String
//			getType() : String
//			getTypeDisplay() : String
//			getFuelType() : FuelType
//			getFuelTypeDisplay() : String
//			getFuelSize() : int
//			getFuelBurn() : int
//			getCruiseSpeed() : int
//			getMPG() : double
//			getHoursUntilEmpty() : double
//			getMaxRange : double

package model;


public class Bus 
{
	int id;				//Bus' ID number in the XML database
	String makeModel;	//Bus' make and model expressed as one String
	String type;		//Bus' type (should either be "city" or "long_distance")
	FuelType fuelType;	//Bus' fuel type expressed as the Enum class FuelType
	int fuelSize;		//Bus' fuel take size expressed as an int in gallons
	int fuelBurn;		//Bus' fuel burn rate expressed as an int in gallons per hour
	int cruiseSpeed;	//Bus' cruising speed expressed as an int in miles per hour
	
	//Main constructor
	public Bus(int id, String makeModel, String type, FuelType fuelType, int fuelSize, int fuelBurn, int cruiseSpeed)
	{
		this.id = id;
		this.makeModel = makeModel;
		this.type = type;
		this.fuelType = fuelType;
		this.fuelSize = fuelSize;
		this.fuelBurn = fuelBurn;
		this.cruiseSpeed = cruiseSpeed;
	}
	
	//----------------------GETTERS----------------------
	public int getID()
	{
		return id;
	}
	
	public String getMakeModel()
	{
		return makeModel;
	}
	
	public String getType()
	{
		return type;
	}
	
	//Additional getter for the type useful when needing to display the bus type
	public String getTypeDisplay()
	{
		if(type.equals("long_distance"))
		{
			return "Long Distance";
		}
		else if(type.equals("city"))
		{
			return "City";
		}
		else
		{
			return "ERROR";
		}
	}
	
	public FuelType getFuelType()
	{
		return fuelType;
	}
	
	//Additional getter for the fuel type useful when needing to display the fuel type
	public String getFuelTypeDisplay()
	{
		return fuelType.getDisplayName();	//Uses the Enum class method for display names
	}
	
	public int getFuelSize()
	{
		return fuelSize;
	}
	
	public int getFuelBurn()
	{
		return fuelBurn;
	}
	
	public int getCruiseSpeed()
	{
		return cruiseSpeed;
	}
	
	//----------------------ADDITIONAL GETTERS FOR CALCULATIONS----------------------
	public double getMPG()
	{
		return (double) cruiseSpeed / fuelBurn;
	}
	
	public double getHoursUntilEmpty()
	{
		return (double) fuelSize / fuelBurn;
	}
	
	public double getMaxRange()
	{
		double hours = (double) fuelSize / fuelBurn;	//Number of hours the bus can operate on a full tank of gas
		return hours * cruiseSpeed;
	}
}
