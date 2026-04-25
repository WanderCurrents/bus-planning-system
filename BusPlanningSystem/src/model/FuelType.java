// FuelType Enum Class
//----------------------
//Description: enum class for working with fuel types,
//Current Enums:	GAS, DIESEL
//Attributes:	displayName : String
//Methods:	FuelType(displayName : String)		
//			getDisplayName() : String
//			parseFuelType(s : String) : FuelType
			
package model;


public enum FuelType 
{
	GAS("Gasoline"),
	DIESEL("Diesel");

	private final String displayName;
	
	//Enum constructor
	FuelType(String displayName)
	{
		this.displayName = displayName;
	}
	
	//Getter for returning the display name attribute of the enum entry
	public String getDisplayName()
	{
		return displayName;
	}
	
	//Additional future-proof method for input validation
	public static FuelType parseFuelType(String s)	//Allows data validation methods to make sure a user input maps correctly
	{
		if (s == null)
		{
			return null;
		}
		
		String normalizedS = s.trim().toLowerCase();
		
		switch (normalizedS)
		{
			//Currently supported entries that map to gas fuel type
			case "g":
			case "gas":
			case "gasoline":
				return GAS;
				
			//Currently supported entries that map to diesel fuel type
			case "d":
			case "dies":
			case "diesel":
				return DIESEL;
				
			default:
				return null;	//Data validation from caller will decide how to deal null issues
		}
	}
}

