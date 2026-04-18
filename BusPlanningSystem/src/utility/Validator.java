package utility;

import xml.BusManager;
import xml.StationManager;
import xml.UserManager;

//For RULES we set only, no java input validation
public class Validator 
{
	//Validator-used class that checks if string is alphanumeric
	private static boolean isAlphanumeric(String s)
	{
		for(int i = 0; i < s.length(); i++)
		{
			if(!Character.isLetterOrDigit(s.charAt(i)))	//If the character at i index isnt letter or digit, its not alphanumeric
			{
				return false;
			}
		}
		return true;	//If the whole string has no symbols, then it's alphanumeric, return true
	}
	
	//Validator-used class that checks if string is alphanumeric (excluding spaces)
	private static boolean isAlphanumericAndSpace(String s)
	{
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if(!Character.isLetterOrDigit(c) && c != ' ')	//Checks if character is not alphanumeric and is not a space
			{
				return false;
			}
		}
		return true;	//If the whole string has no symbols (excluding spaces), then it's true
	}
	
	//Validator-used class that checks if the string has atleast one letter, one number, and one symbol. Exits early if all are true
	private static boolean hasLetterNumberSymbol(String s)
	{
		boolean hasLetter = false;
		boolean hasNumber = false;
		boolean hasSymbol = false;
		
		for(char c : s.toCharArray())
		{
			if(Character.isLetter(c))
			{
				hasLetter = true;
			}
			else if(Character.isDigit(c))
			{
				hasNumber = true;;
			}
			else
			{
				hasSymbol = true;	//If not letter and not number then must be a symbol
			}
			
			//If all three are fulfilled, exit early with true
			if(hasLetter && hasNumber && hasSymbol)
			{
				return true;
			}
		}
		return false;
	}
	
	//Validator-used class that checks if the string input passes as a valid decimal number and has valid decimal places of accuracy
	private static boolean hasValidDecimalPlaces(String input, int minPlaces, int maxPlaces)
	{
		//Checks if the number is a decimal (has a decimal point)
		int dotIndex = input.indexOf('.');
		if(dotIndex == -1)
		{
			System.out.println("\nInvalid input. Value must contain a decimal point.");
			return false;
		}
		
		//Split into two parts
		String[] parts = input.split("\\.");
		
		
		
		//Must have exactly two parts, left of point and right of point (meaning there arent more than one decimal points)
		if(parts.length !=2)
		{
			System.out.println("\nInvalid input. Value must only contain one decimal point and must have digits after the decimal point.");
			return false;
		}
		
		String decimals = parts[1];	//Select the area to the right of the '.'
		
		//Must have digits after the decimal
		if(decimals.length() == 0)
		{
			System.out.println("\bnInvalid input. Value must have digits after the decimal point.");
			return false;
		}
		
		//Check the decimal place count
		if(decimals.length() < minPlaces || decimals.length() > maxPlaces)
		{
			System.out.println("\nInvalid input. Value must have between " + minPlaces + " and " + maxPlaces + " decimal places.");
			return false;
		}
		
		return true;
	}
	
	//Checks if username is valid, it must: not already exist, be size < 25 and > 4 characters, and only contain alphanumeric characters
	public static boolean isValidUsername(UserManager um, String newUsername)
	{
		//Check to see if the user already exists
		if(um.findUser(newUsername) != -99)	//Here, -99 is the value we chose to signal user not found, so if not equal then there must be a user already
		{
			System.out.println("\nInvalid input. Username must not match a user that already exists!");
			return false;
		}
		
		//Check to make sure size of username is acceptable (must be less than 25 chars long and longer than 4
		if(newUsername.length() > 25 || newUsername.length() < 4)
		{
			System.out.println("\nInvalid input. Username must be at least 4 characters long and no longer than 25 characters.");
			return false;
		}
		
		//Checks to make sure username has no symbols
		if(!isAlphanumeric(newUsername))
		{
			System.out.println("\nInvalid input. Username must not contain any symbols.");
			return false;
		}
		
		return true;	//Username passes, return true
	}
	
	//Checks if password is valid. Must be > 8 characters and must contain atleast one number, one letter, and on symbol.
	public static boolean isValidPassword(String newPassword)
	{
		//Check to make sure size of password is acceptable (Must be more than 8 characters long
		if(newPassword.length() < 8)
		{
			System.out.println("\nInvalid input. Password must be atleast 8 characters long.");
			return false;
		}
		
		//Check to make sure the password has atleast one of each: a number, a letter, and a symbol
		if(!hasLetterNumberSymbol(newPassword))
		{
			System.out.println("\nInvalid input. Password must have at least one letter, number, and symbol.");
			return false;
		}
		
		return true;	//Password passes, return true
	}
	
	//Checks if station name is valid. Must be size < 50 and > 4 characters long and contain only alphanumeric characters and spaces, and must not already exist
	public static boolean isValidStationName(StationManager sm, String newName)
	{
		//Check to see if the station already exists
		if(sm.findStation(newName) != -99)	//Here, -99 is the value we chose to signal station not found, so if not equal then there must be a station already
		{
			System.out.println("\nInvalid input. Station name must not match a station that already exists!");
			return false;
		}
		
		//Check to make sure size of station name is acceptable (must be less than 50 chars long and longer than 4)
		if(newName.length() > 50 || newName.length() < 4)
		{
			System.out.println("\nInvalid input. Station name must be atleast 4 characters long and no longer than 50 characters.");
			return false;
		}
		
		//Checks to make sure station name has no symbols (other than spaces)
		if(!isAlphanumericAndSpace(newName))
		{
			System.out.println("\nInvalid input. Station name must not contain any symbols other than spaces.");
			return false;
		}
		
		return true;	//Station name passes, return true
	}
	
	//Checks if the station latitude is valid. Must be a decimal number with atleast 4 decimal places of accuracy (no more than 8) and be a valid latitude
	public static boolean isValidLatitude(String inLat)
	{
		double latitude;
		
		//Make sure the input can be cast to a Double
		try
		{
			
			latitude = Double.parseDouble(inLat);
		} catch(Exception e)
		{
			System.out.println("\nInvalid input. Please enter a decimal number.");
			return false;
		}
		
		//Check if a valid decimal with atleast 4 places of accuracy and no more than 8
		if(!hasValidDecimalPlaces(inLat, 4, 8))
		{
			return false;	//No print statement needed, method handles that
		}
		
		//Check to see if the latitude passed in falls with the range of latitudes
		if(latitude < -90 || latitude > 90)
		{
			System.out.println("\nInvalid input. Latitude must be between -90 and 90");
			return false;
		}
		
		return true;	//Latitude passes, return true
	}
	
	//Checks if the station longitude is valid. Must be a decimal number with atleast 4 decimal places of accuracy (no more than 8) and be a valid longitude
	public static boolean isValidLongitude(String inLong)
	{
		double longitude;
		
		//Make sure the input can be cast to a Double
		try
		{
			
			longitude = Double.parseDouble(inLong);
		} catch(Exception e)
		{
			System.out.println("\nInvalid input. Please enter a decimal number.");
			return false;
		}
		
		//Check if a valid decimal with atleast 4 places of accuracy and no more than 8
		if(!hasValidDecimalPlaces(inLong, 4, 8))
		{
			return false;	//No print statement needed, method handles that
		}
		
		//Check to see if the longitude passed in falls with the range of latitudes
		if(longitude < -180 || longitude > 180)
		{
			System.out.println("\nInvalid input. Longitude must be between -180 and 180");
			return false;
		}
		
		return true;	//Longitude passes, return true
	}
	
	//Checks if make and model name is valid. Must be size < 50 and > 4 characters long and contain only alphanumeric characters and spaces, and must not already exist
	public static boolean isValidMakeModel(BusManager bm, String newMakeModel)
	{
		//Check to see if the bus already exists
		if(bm.findBus(newMakeModel) != -99)	//Here, -99 is the value we chose to signal bus make and model not found, so if not equal then there must be a bus already
		{
			System.out.println("\nInvalid input. Bus make and model must not match a bus make and model that already exists!");
			return false;
		}
		
		//Check to make sure size of bus make and model name is acceptable (must be less than 50 chars long and longer than 4)
		if(newMakeModel.length() > 50 || newMakeModel.length() < 4)
		{
			System.out.println("\nInvalid input. Bus make and model name must be atleast 4 characters long and no longer than 50 characters.");
			return false;
		}
		
		//Checks to make sure make and model name has no symbols (other than spaces)
		if(!isAlphanumericAndSpace(newMakeModel))
		{
			System.out.println("\nInvalid input. Bus make and model must not contain any symbols other than spaces.");
			return false;
		}
		
		return true;	//Station name passes, return true
	}
	
	//Checks if fuel tank size is valid. Must be integer that is > 0 and < 500
	public static boolean isValidFuelTankSize(String newFuelSize)
	{
		int fuelTankSize;
		
		//Make sure the input can be cast to an int
		try
		{
			
			fuelTankSize = Integer.parseInt(newFuelSize);
		} catch(Exception e)
		{
			System.out.println("\nInvalid input. Please enter an integer.");
			return false;
		}
		
		//Check to see if the fuel tank size passed in falls within acceptable range (0-500)
		if(fuelTankSize <= 0 || fuelTankSize >= 500)
		{
			System.out.println("\nInvalid input. Fuel tank size must be larger than 0 and less than 500.");
			return false;
		}
		
		return true;	//Fuel tank size passes, return true
	}
	
	//Checks if fuel burn rate is valid. Must be integer that is > 0 and < 50
	public static boolean isValidBurnRate(String newBurnRate)
	{
		int fuelBurnRate;
		
		//Make sure the input can be cast to an int
		try
		{
			
			fuelBurnRate = Integer.parseInt(newBurnRate);
		} catch(Exception e)
		{
			System.out.println("\nInvalid input. Please enter an integer.");
			return false;
		}
		
		//Check to see if the fuel tank size passed in falls within acceptable range (0-500)
		if(fuelBurnRate <= 0 || fuelBurnRate >= 50)
		{
			System.out.println("\nInvalid input. Fuel burn rate must be larger than 0 and less than 50.");
			return false;
		}
		
		return true;	//Fuel burn rate passes, return true
	}
	
	//Checks if bus cruise speed is valid. Must be integer that is > 0 and < 120
	public static boolean isValidCruiseSpeed(String newCruiseSpeed)
	{
		int cruiseSpeed;
		
		//Make sure the input can be cast to an int
		try
		{
			
			cruiseSpeed = Integer.parseInt(newCruiseSpeed);
		} catch(Exception e)
		{
			System.out.println("\nInvalid input. Please enter an integer.");
			return false;
		}
		
		//Check to see if the fuel tank size passed in falls within acceptable range (0-500)
		if(cruiseSpeed <= 0 || cruiseSpeed >= 120)
		{
			System.out.println("\nInvalid input. Bus cruise speed must be larger than 0 and less than 120.");
			return false;
		}
		
		return true;	//Cruise speed passes, return true
	}
}
