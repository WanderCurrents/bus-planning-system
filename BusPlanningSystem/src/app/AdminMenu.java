package app;

import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

import model.Bus;
import model.FuelType;
import model.Station;
import model.User;
import utility.InputHelper;
import utility.Validator;
import xml.BusManager;
import xml.StationManager;
import xml.UserManager;

public class AdminMenu 
{
	public static boolean adminMenu(UserManager um, StationManager sm, BusManager bm, Scanner scanner)
	{
		int userSelection;
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu");
		if(!um.isCurrentUserAdmin())
		{
			System.out.println("\nERROR: Current user " + um.getCurrentUsername() + " is does not have admin status!");
			System.out.println("Please log in as an admin user to access the Admin Menu");
			System.out.print("\n\n\nPress ENTER to continue...");
			scanner.nextLine();
		}
		else if(um.isCurrentUserAdmin())
		{
			System.out.println("1. Add user");
			System.out.println("2. Remove user");
			System.out.println("3. Add station");
			System.out.println("4. Remove station");
			System.out.println("5. Add bus");
			System.out.println("6. Remove bus");
			System.out.println("\n\n0. Exit Admin Menu\n\n");
			
			userSelection = InputHelper.getIntInRange(scanner, 0, 6);
			
			switch(userSelection)
			{
				case 1:
					while(addUser(um, scanner))
					{
						//While addUser is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 2:
					while(removeUser(um, scanner))
					{
						//While removeUser is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 3:
					while(addStation(sm, scanner))
					{
						//While addStation is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 4:
					while(removeStation(sm, scanner))
					{
						//While removeStation is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 5:
					while(addBus(bm, scanner))
					{
						//While addBus is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 6:
					while(removeBus(bm, scanner))
					{
						//While removeBus is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 0:
					return false;
			}
		}
		else
		{
			System.out.println("ERROR: Cannot get valid status of user");
			System.out.print("\n\n\nPress ENTER to continue...");
			scanner.nextLine();
		}
		
		return true;
	}
	
	private static boolean addUser(UserManager um, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Add User");
		
		String newUsername;
		while(true)
		{
			System.out.print("Enter username: ");
			newUsername = scanner.nextLine();
			if(Validator.isValidUsername(um, newUsername))
			{
				break;
			}
		}
		
		String newPassword;
		while(true)
		{
			System.out.print("Enter password: ");
			newPassword = scanner.nextLine();
			if(Validator.isValidPassword(newPassword))
			{
				break;
			}
		}
		
		boolean isAdmin = InputHelper.getYesNo(scanner, "Is this user an admin?");
		try 
		{
			um.addUser(newUsername, newPassword, isAdmin);
		} catch (Exception e) {
			System.out.println("ERROR: Cannot create user :(");
			System.out.print("\n\n\nPress ENTER to continue...");
			return false;
		}
		
		System.out.println("User created successfully!\n");
		if(!InputHelper.getYesNo(scanner, "Would you like to add another user?"))
		{
			return false;
		}
		return true;
	}
	
	private static boolean removeUser(UserManager um, Scanner scanner)	
	{
		boolean removedStatus = false;
		while(true)
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Admin Menu > Remove User");
			
			User userToRemove = selectSingleUser(um, scanner);
			
			boolean boolInput = InputHelper.getYesNo(scanner, "Are you sure you want to remove " + userToRemove.getUsername()  + "?");
			if(boolInput)
			{
				try 
				{
					removedStatus = um.removeUser(userToRemove.getID());
				} catch (Exception e) {
					System.out.println("ERROR: Cannot remove user :(");
					System.out.print("\n\n\nPress ENTER to continue...");
					return false;
				}
			}
			else
			{
				removedStatus = false;
			}
			
			if(removedStatus)
			{
				System.out.println("User removed successfully!\n");
			}
			else
			{
				System.out.println("User not removed, operation cancelled.\n");
			}
			
			if(!InputHelper.getYesNo(scanner, "Would you like to remove another user?"))
			{
				return false;
			}
			
		}
	}
	
	private static boolean addStation(StationManager sm, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Add Station");
		
		String newName;
		while(true)
		{
			System.out.print("Enter station name: ");
			newName = scanner.nextLine();
			if(Validator.isValidStationName(sm, newName))
			{
				break;
			}
		}

		String input;
		double newLat;
		while(true)
		{
			System.out.print("Enter latitude: ");
			input = scanner.nextLine();
			if(Validator.isValidLatitude(input))
			{
				newLat = Double.parseDouble(input);	//Safe to parse
				break;
			}
		}
		double newLong;
		while(true)
		{
			System.out.print("Enter longitude: ");
			input = scanner.nextLine();
			if(Validator.isValidLongitude(input))
			{
				newLong = Double.parseDouble(input);	//Safe to parse
				break;
			}
		}
		
		
		EnumSet<FuelType> newSupportedFuel = EnumSet.allOf(FuelType.class);	
		
		boolean isRefuel = InputHelper.getYesNo(scanner, "Is this a refuel station?");
		try 
		{
			sm.addStation(newName, newLat, newLong, newSupportedFuel, isRefuel);
		} catch (Exception e) {
			System.out.println("ERROR: Cannot create station :(");
			System.out.print("\n\n\nPress ENTER to continue...");
			return false;
		}
		
		System.out.println("Station created successfully!\n");
		if(!InputHelper.getYesNo(scanner, "Would you like to add another station?"))
		{
			return false;
		}
		return true;
	}
	
	private static boolean removeStation(StationManager sm, Scanner scanner)
	{
		boolean removedStatus = false;
		while(true)
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Admin Menu > Remove Station");
			
			Station stationToRemove = selectSingleStation(sm, scanner);
			
			boolean boolInput = InputHelper.getYesNo(scanner, "Are you sure you want to remove " + stationToRemove.getName()  + "?");
			if(boolInput)
			{
				try 
				{
					removedStatus = sm.removeStation(stationToRemove.getID());
				} catch (Exception e) {
					System.out.println("ERROR: Cannot remove station :(");
					System.out.print("\n\n\nPress ENTER to continue...");
					return false;
				}
			}
			else
			{
				removedStatus = false;
			}
			
			if(removedStatus)
			{
				System.out.println("Station removed successfully!\n");
			}
			else
			{
				System.out.println("Station not removed, operation cancelled.\n");
			}
			
			if(!InputHelper.getYesNo(scanner, "Would you like to remove another station?"))
			{
				return false;
			}
			
		}
	}
	
	private static boolean addBus(BusManager bm, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Add Bus");
		
		String newMakeModel;
		while(true)
		{
			System.out.print("Enter bus make and model: ");
			newMakeModel = scanner.nextLine();
			if(Validator.isValidMakeModel(bm, newMakeModel))
			{
				break;
			}
		}
		
		String newType;
		System.out.println("\nIs this bus:\n1. A city bus\nor\n2. A long distance bus");
		int userSelection = InputHelper.getIntInRange(scanner, 1, 2);
		if(userSelection == 1)
		{
			newType = "city";
		}
		else
		{
			newType = "long_distance";
		}
		
		FuelType newFuelType;
		System.out.println("\nDoes this bus use:\n1. Gasoline\nor\n2. Diesel");
		userSelection = InputHelper.getIntInRange(scanner, 1, 2);
		if(userSelection == 1)
		{
			newFuelType = FuelType.GAS;
		}
		else
		{
			newFuelType = FuelType.DIESEL;
		}
		
		String input;
		int newFuelSize;
		while(true)
		{
			System.out.print("Enter integer that represents fuel tank size (gal): ");
			input = scanner.nextLine();
			if(Validator.isValidFuelTankSize(input))
			{
				newFuelSize = Integer.parseInt(input);
				break;
			}
		}
		
		int newBurnRate;
		while(true)
		{
			System.out.print("Enter integer that represents fuel burn rate (gal/hr): ");
			input = scanner.nextLine();
			if(Validator.isValidBurnRate(input))
			{
				newBurnRate = Integer.parseInt(input);
				break;
			}
				
		}
		
		int newCruiseSpeed;
		while(true)
		{
			System.out.print("Enter integer that represents cruising speed (mph): ");
			input = scanner.nextLine();
			if(Validator.isValidCruiseSpeed(input))
			{
				newCruiseSpeed = Integer.parseInt(input);
				break;
			}
		}
		
		try 
		{
			bm.addBus(newMakeModel, newType, newFuelType, newFuelSize, newBurnRate, newCruiseSpeed);
		} catch (Exception e) {
			System.out.println("ERROR: Cannot create bus :(");
			System.out.print("\n\n\nPress ENTER to continue...");
			return false;
		}
		
		System.out.println("Bus created successfully!\n");
		if(!InputHelper.getYesNo(scanner, "Would you like to add another bus?"))
		{
			return false;
		}
		return true;
	}
	
	private static boolean removeBus(BusManager bm, Scanner scanner)	
	{
		boolean removedStatus = false;
		while(true)
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Admin Menu > Remove Bus");
			
			Bus busToRemove = selectSingleBus(bm, scanner);
			
			boolean boolInput = InputHelper.getYesNo(scanner, "Are you sure you want to remove " + busToRemove.getMakeModel() + " (ID: " + busToRemove.getID()  + ")?");
			if(boolInput)
			{
				try 
				{
					removedStatus = bm.removeBus(busToRemove.getID());
				} catch (Exception e) {
					System.out.println("ERROR: Cannot remove bus :(");
					System.out.print("\n\n\nPress ENTER to continue...");
					return false;
				}
			}
			else
			{
				removedStatus = false;
			}
			
			if(removedStatus)
			{
				System.out.println("Bus removed successfully!\n");
			}
			else
			{
				System.out.println("Bus not removed, operation cancelled.\n");
			}
			
			if(!InputHelper.getYesNo(scanner, "Would you like to remove another bus?"))
			{
				return false;
			}
			
		}
	}
	
	//Admin single user selection method (shows more info than other single selection method)
	private static User selectSingleUser(UserManager um, Scanner scanner)
	{
		String input;
		boolean boolInput;
		boolean searching = true;
		int userSelection;
		User selectedUser = null;
		while(searching)
		{
			System.out.println();
			System.out.print("Search for user (press ENTER to see all users): ");
			input = scanner.nextLine();
			
			//Search for buses
			System.out.println("\nSearching for users that match \"" + input + "\"...");
			List<User> results = um.subStringSearch(input);
			
			//Print the results
			if(results.isEmpty())	//If the search is bad, state it's empty
			{
				System.out.println("\nNo users found!");
				boolInput = InputHelper.getYesNo(scanner, "\n\nWould you like to try again (or n=quit)?");
				if(!boolInput)
				{
					return null;
				}
				else
				{
					continue;	//Just pass through this iteration and redo outerloop
				}
			}
			else	//If search isn't bad, continue
			{
				System.out.println("\nFound " + results.size() + " results:");
				for(int i = 0; i < results.size(); i++)
				{
					//Note, the index in the list is 1 off the printed option number, make sure to remember that
					//Very fancy looking, but it just formats the outputs to make things look cleaner
					System.out.printf(
						    "**%d\t- %s  -  Username: %s  -  Password: %s  -  UserID: %d  -  Is Admin? %s%n",
						    i + 1,
						    results.get(i).getUsername(),
						    results.get(i).getPassword(),
						    results.get(i).getID(),
						    results.get(i).getIsAdmin()
						);

				}
				
				//Get the admin user choice
				DisplayManager.printFooter();
				
				userSelection = InputHelper.getIntInRange(scanner, 1, results.size());
				
				
				selectedUser = results.get(userSelection-1);	//Set the bus selection, -1 for index consideration
				searching = false;	//Exit the outer loop
			}
		}
		return selectedUser;
	}
	
	//Admin single station selection method (shows more info than other single selection method)
	private static Station selectSingleStation(StationManager sm, Scanner scanner)
	{
		String input;
		boolean boolInput;
		boolean searching = true;
		int userSelection;
		Station selectedStation = null;
		while(searching)
		{
			System.out.println();
			System.out.print("Search for station (press ENTER to see all stations): ");
			input = scanner.nextLine();
			
			//Search for buses
			System.out.println("\nSearching for stations that match \"" + input + "\"...");
			List<Station> results = sm.subStringSearch(input, true);	//Do search in admin mode to see all refuel stations because, you know, this is in the admin panel
			
			//Print the results
			if(results.isEmpty())	//If the search is bad, state it's empty
			{
				System.out.println("\nNo stations found!");
				boolInput = InputHelper.getYesNo(scanner, "\n\nWould you like to try again (or n=quit)?");
				if(!boolInput)
				{
					return null;
				}
				else
				{
					continue;	//Just pass through this iteration and redo outerloop
				}
			}
			else	//If search isn't bad, continue
			{
				System.out.println("\nFound " + results.size() + " results:");
				for(int i = 0; i < results.size(); i++)
				{
					//Note, the index in the list is 1 off the printed option number, make sure to remember that
					//Very fancy looking, but it just formats the outputs to make decimals look cleaner, formatted to 4 decimal points for lat and long
					System.out.printf(
						    "**%d\t- %s  -  Lat: %.4f  -  Long: %.4f  -  StationID: %d  -  Is Refuel Station?: %s%n",
						    i + 1,
						    results.get(i).getName(),
						    results.get(i).getLatitude(),
						    results.get(i).getLongitude(),
						    results.get(i).getID(),
						    results.get(i).getIsFuelOnly()
						);

				}
				//Get the admin user choice
				DisplayManager.printFooter();
				
				userSelection = InputHelper.getIntInRange(scanner, 1, results.size());
				
				
				selectedStation = results.get(userSelection-1);	//Set the bus selection, -1 for index consideration
				searching = false;	//Exit the outer loop
			}
		}
		return selectedStation;
	}
	
	private static Bus selectSingleBus(BusManager bm, Scanner scanner)
	{
		String input;
		boolean boolInput;
		boolean searching = true;
		int userSelection;
		Bus selectedBus = null;
		while(searching)
		{
			System.out.println();
			System.out.print("Search for bus (press ENTER to see all buses): ");
			input = scanner.nextLine();
			
			//Search for buses
			System.out.println("\nSearching for buses that match \"" + input + "\"...");
			List<Bus> results = bm.subStringSearch(input);
			
			//Print the results
			if(results.isEmpty())	//If the search is bad, state it's empty
			{
				System.out.println("\nNo buses found!");
				boolInput = InputHelper.getYesNo(scanner, "\n\nWould you like to try again (or n=quit)?");
				if(!boolInput)
				{
					return null;
				}
				else
				{
					continue;	//Just pass through this iteration and redo outerloop
				}
			}
			else	//If search isn't bad, continue
			{
				System.out.println("\nFound " + results.size() + " results:");
				for(int i = 0; i < results.size(); i++)
				{
					//Note, the index in the list is 1 off the printed option number, make sure to remember that
					//Very fancy looking, but it just formats the outputs to make decimals look cleaner, formatted to 2 decimal points for max range
					System.out.printf(
						    "**%d\t- %s  -  Fuel Tank Size: %dgal  -  Fuel Burn: %dgal/hr  -  Max Range: %.2f miles  -  Cruise Speed: %dmph  -  Bus Type: %s  -  BusID: %d  -  Fuel Type: %s%n",
						    i + 1,
						    results.get(i).getMakeModel(),
						    results.get(i).getFuelSize(),
						    results.get(i).getFuelBurn(),
						    results.get(i).getMaxRange(),
						    results.get(i).getCruiseSpeed(),
						    results.get(i).getTypeDisplay(),
						    results.get(i).getID(),
						    results.get(i).getFuelTypeDisplay()
						);

				}
				
				//Get the admin user choice
				DisplayManager.printFooter();
				
				userSelection = InputHelper.getIntInRange(scanner, 1, results.size());
				
				
				selectedBus = results.get(userSelection-1);	//Set the bus selection, -1 for index consideration
				searching = false;	//Exit the outer loop
			}
		}
		return selectedBus;
	}
}
