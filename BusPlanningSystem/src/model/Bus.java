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
	
	//Basic getters for object
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
	public String getFuelTypeDisplay()
	{
		return fuelType.getDisplayName();
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
	
	//Additional getters for optional and necessary operations
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
