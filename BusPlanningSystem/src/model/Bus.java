package model;

public class Bus 
{
	int id;
	String makeModel;
	String type;
	int fuelSize;
	int fuelBurn;
	int cruiseSpeed;
	
	public Bus(int id, String makeModel, String type, int fuelSize, int fuelBurn, int cruiseSpeed)
	{
		this.id = id;
		this.makeModel = makeModel;
		this.type = type;
		this.fuelSize = fuelSize;
		this.fuelBurn = fuelBurn;
		this.cruiseSpeed = cruiseSpeed;
	}
	
	//Getters
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
	
}
