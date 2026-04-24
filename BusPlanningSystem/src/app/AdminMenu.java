//   AdminMenu Class
//----------------------
//Description: class that holds the methods responsible for running and displaying things related to admin operations
//Attributes: n/a
//Methods: 	adminMenu(um : UserManager, sm : StationManager, bs : BusManager, scanner : Scanner) : boolean
//			displayUsers(um : UserManager, scanner : Scanner) : void
//			addUser(um : UserManager, scanner : Scanner) : boolean
//			removeUser(um : UserManager, scanner : Scanner) : boolean
//			modifyUser(um : UserManager, scanner : Scanner) : boolean
//			displayStations(sm : StationManager, scanner : Scanner) : void
//			addStation(sm : StationManager, scanner : Scanner) : boolean
//			removeStation(sm : StationManager, scanner : Scanner) : boolean
//			modifyStation(sm : StationManager, scanner : Scanner) : boolean
//			displayBuses(bm : BusManager, scanner : Scanner) : void
//			addBus(bm : BusManager, scanner : Scanner) : boolean
//			removeBus(bm : BusManager, scanner : Scanner) : boolean
//			modifyBus(bm : BusManager, scanner : Scanner) : boolean
//			selectSingleUser(um : UserManager, scanner : Scanner) : User
//			selectSingleStation(sm : StationManager, scanner : Scanner) : Station
//			selectSingleBus(bm : BusManager, scanner : Scanner) : Bus

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
	//Displays the main menu for admin operations
	public static boolean adminMenu(UserManager um, StationManager sm, BusManager bm, Scanner scanner)
	{
		int userSelection;
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu");
		if(!um.isCurrentUserAdmin())	//Blocks user if not an admin
		{
			System.out.println("\nERROR: Current user " + um.getCurrentUsername() + " does not have admin status!");
			System.out.println("Please log in as an admin user to access the Admin Menu");
			System.out.print("\n\n\nPress ENTER to continue...");
			scanner.nextLine();
			return false;
		}
		else if(um.isCurrentUserAdmin())	//Otherwise if admin, display the menu options
		{
			System.out.println("1. Display users");
			System.out.println("2. Add user");
			System.out.println("3. Remove user");
			System.out.println("4. Modify user");
			System.out.println("5. Display stations");
			System.out.println("6. Add station");
			System.out.println("7. Remove station");
			System.out.println("8. Modify station");
			System.out.println("9. Display buses");
			System.out.println("10. Add bus");
			System.out.println("11. Remove bus");
			System.out.println("12. Modify bus");
			System.out.println("\n\n0. Exit Admin Menu\n\n");
			
			userSelection = InputHelper.getIntInRange(scanner, 0, 12);	//Prompt user for selection via InputHelper class
			
			//Enter switch to do the admin operations based on the user selection
			switch(userSelection)
			{
				case 1:
					displayUsers(um, scanner);	//Call the displayUser method
					break;
				case 2:
					while(addUser(um, scanner))
					{
						//While addUser is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 3:
					while(removeUser(um, scanner))
					{
						//While removeUser is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 4: 
					while(modifyUser(um, scanner))
					{
						//While modifyUser is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 5:
					displayStations(sm, scanner);	//Call the displayStation method
					break;
				case 6:
					while(addStation(sm, scanner))
					{
						//While addStation is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 7:
					while(removeStation(sm, scanner))
					{
						//While removeStation is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 8:
					while(modifyStation(sm, scanner))
					{
						//While modifyStation is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 9:
					displayBuses(bm, scanner);	//Call the displayBuses method
					break;
				case 10:
					while(addBus(bm, scanner))
					{
						//While addBus is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 11:
					while(removeBus(bm, scanner))
					{
						//While removeBus is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 12:
					while(modifyBus(bm, scanner))
					{
						//While modifyBus is true, keep calling it
						//Once it returns false, exit
						//This loop is supposed to be empty
					}
					break;
				case 0:
					return false;
			}
		}
		else	//Else catch, this should never run but in the case it does, it means that the user is neither an admin or non-admin user, which should be impossible
		{
			System.out.println("ERROR: Cannot get valid status of user");
			System.out.print("\n\n\nPress ENTER to continue...");
			scanner.nextLine();
		}
		
		return true;
	}
	
	//Simple method for displaying all users, the admin can search for one or just press enter to show all
	private static void displayUsers(UserManager um, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Display Users");
		
		String input;
		boolean boolInput;
		
		System.out.println();
		System.out.print("Search for user (press ENTER to see all users): ");
		input = scanner.nextLine();
		
		//Search for users
		System.out.println("\nSearching for users that match \"" + input + "\"...");
		List<User> results = um.subStringSearch(input);
		
		//Print the results
		if(results.isEmpty())	//If the search is bad, state it's empty
		{
			System.out.println("\nNo users found!");
			boolInput = InputHelper.getYesNo(scanner, "\n\nWould you like to try again (or n=quit)?");
			if(!boolInput)
			{
				return;
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
					    "**%d\t  -  Username: %s  -  Password: %s  -  UserID: %d  -  Is Admin? %s%n",
					    i + 1,
					    results.get(i).getUsername(),
					    results.get(i).getPassword(),
					    results.get(i).getID(),
					    results.get(i).getIsAdmin()
					);

			}
			
			System.out.print("\nPress ENTER to continue...");
			scanner.nextLine();
		}
		
	}
	
	//Method for adding a new user
	private static boolean addUser(UserManager um, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Add User");
		
		//Getting the new username
		String newUsername;
		while(true)
		{
			System.out.print("Enter username: ");
			newUsername = scanner.nextLine();
			//Check that the new username is a valid username
			if(Validator.isValidUsername(um, newUsername))
			{
				break;
			}
		}
		
		//Getting the new password
		String newPassword;
		while(true)
		{
			System.out.print("Enter password: ");
			newPassword = scanner.nextLine();
			//Check that the new password is a valid password
			if(Validator.isValidPassword(newPassword))
			{
				break;
			}
		}
		
		//Prompt if the new add user is to be created as an admin
		boolean isAdmin = InputHelper.getYesNo(scanner, "Is this user an admin?");
		
		//Try to create the new user with all the new attributes
		//The XML handling may cause errors so wrap it in a try/catch
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
	
	//Method for removing a user
	private static boolean removeUser(UserManager um, Scanner scanner)	
	{
		boolean removedStatus = false;	//Boolean flag for it the user has been removed
		//While true loop for making sure that if the user fails to input valid data it or if the user wants to remove another, it restarts
		while(true)
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Admin Menu > Remove User");
			
			User userToRemove = selectSingleUser(um, scanner);	//Call the selectSingleUser which searches and returns a user or null if quit
			if(userToRemove == null)	//Null flag is used to show that the user quit the operation
			{
				return false;	//So quit
			}
			
			//Verify that the user wants to remove the user
			boolean boolInput = InputHelper.getYesNo(scanner, "Are you sure you want to remove " + userToRemove.getUsername()  + "?");
			if(boolInput)	//If the user verifies that they want to complete the operation
			{
				//Try to delete the user via UserID
				//The XML handling may cause errors so wrap it in a try/catch
				try 
				{
					removedStatus = um.removeUser(userToRemove.getID());
				} catch (Exception e) {
					System.out.println("ERROR: Cannot remove user :(");
					System.out.print("\n\n\nPress ENTER to continue...");
					return false;
				}
			}
			else	//Else the user did not say yes to the "are you sure" prompt, set flag to false again
			{
				removedStatus = false;
			}
			
			if(removedStatus)	//If the removed flag was set to true, the operation was completed
			{
				System.out.println("User removed successfully!\n");
			}
			else	//Else, the flag was never set, therefore the operation was cancelled
			{
				System.out.println("User not removed, operation cancelled.\n");
			}
			
			//Prompt the user if they would like to remove another user
			if(!InputHelper.getYesNo(scanner, "Would you like to remove another user?"))
			{
				return false;
			}
		}
	}
	
	//Method for modifying users, basically serves as a remove and add function. The old user is removed and the data + modified data is added as a new user with the same ID
	private static boolean modifyUser(UserManager um, Scanner scanner)
	{
		//While true loop for making sure that if the user fails to input valid data it or if the user wants to modify another, it restarts
		while(true)
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Admin Menu > Modify User");
			System.out.println("NOTE: press ENTER to skip modifying an input field");
			
			//Select the user that needs modification
			User userToRemove = selectSingleUser(um, scanner);	//Call the selectSingleUser which searches and returns a user or null if quit
			if(userToRemove == null)	//Null flag is used to show that the user quit the operation
			{
				return false;	//So quit
			}
			
			int newID = userToRemove.getID();	//Set the ID for the "new" user
			
			//Getting "new" username
			String newUsername;
			while(true)
			{
				System.out.print("Modify username (was: \"" + userToRemove.getUsername() + "\"): ");
				newUsername = scanner.nextLine();
				
				//If user presses enter, keep old value
				if(newUsername.isBlank())
				{
					newUsername = userToRemove.getUsername();
					break;
				}
				//Otherwise validate new value
				if(Validator.isValidUsername(um, newUsername))
				{
					break;
				}
			}
			
			//Getting "new" password
			String newPassword;
			while(true)
			{
				System.out.print("Enter new password: ");
				newPassword = scanner.nextLine();
				
				//If user presses enter, keep old value
				if(newPassword.isBlank())
				{
					newPassword = userToRemove.getPassword();
					break;
				}
				//Otherwise validate new value
				if(Validator.isValidPassword(newPassword))
				{
					break;
				}
			}
			
			//Prompt user if the user is an admin
			boolean isAdmin = InputHelper.getYesNo(scanner, "Is this user an admin?");
			
			//Try to remove the user and then create a new user with all the new attributes, the illusion of modification
			//The XML handling may cause errors so wrap it in a try/catch
			try 
			{
				um.removeUser(userToRemove.getID());	//First remove user
				um.addUser(newID, newUsername, newPassword, isAdmin);	//Then re-add user
			} catch (Exception e) {
				System.out.println("ERROR: Cannot modify user :(");
				System.out.print("\n\n\nPress ENTER to continue...");
				return false;
			}
			
			System.out.println("User modified successfully!\n");
			if(!InputHelper.getYesNo(scanner, "Would you like to modify another user?"))
			{
				return false;
			}
			return true;
		}
		
	}
	
	//Simple method for displaying all stations, the admin can search for one or just press enter to show all
	private static void displayStations(StationManager sm, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Display Stations");
		
		String input;
		boolean boolInput;
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
				return;
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
			
			System.out.print("\nPress ENTER to continue...");
			scanner.nextLine();
		}
	}
	
	//Method for adding a new station
	private static boolean addStation(StationManager sm, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Add Station");
		
		//Getting the new station name
		String newName;
		while(true)
		{
			System.out.print("Enter station name: ");
			newName = scanner.nextLine();
			//Check that the new name is a valid station name
			if(Validator.isValidStationName(sm, newName))
			{
				break;
			}
		}

		//Getting the new latitude
		String input;
		double newLat;
		while(true)
		{
			System.out.print("Enter latitude: ");
			input = scanner.nextLine();
			//Check that the new latitude is a valid latitude
			if(Validator.isValidLatitude(input))
			{
				newLat = Double.parseDouble(input);	//Safe to parse
				break;
			}
		}
		
		//Getting the new longitude
		double newLong;
		while(true)
		{
			System.out.print("Enter longitude: ");
			input = scanner.nextLine();
			//Check that the new longitude is a valid longitude
			if(Validator.isValidLongitude(input))
			{
				newLong = Double.parseDouble(input);	//Safe to parse
				break;
			}
		}
		
		//Getting the supported fuel type, as of right now, all stations support all fuel types by default
		EnumSet<FuelType> newSupportedFuel = EnumSet.allOf(FuelType.class);	
		
		//Prompt the user to see if the station is a refuel only station
		boolean isRefuel = InputHelper.getYesNo(scanner, "Is this a refuel station?");
	
		//Try to create the new station with all the new attributes
		//The XML handling may cause errors so wrap it in a try/catch
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
	
	//Method for removing a station
	private static boolean removeStation(StationManager sm, Scanner scanner)
	{
		boolean removedStatus = false;	//Boolean flag for it the station has been removed
		//While true loop for making sure that if the user fails to input valid data it or if the user wants to remove another, it restarts
		while(true)
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Admin Menu > Remove Station");
			
			Station stationToRemove = selectSingleStation(sm, scanner);	//Call the selectSingleStation which searches and returns a station or null if quit
			if(stationToRemove == null)	//Null flag is used to show that the user quit the operation
			{
				return false;	//So quit
			}
			
			//Verify that the user wants to remove the station
			boolean boolInput = InputHelper.getYesNo(scanner, "Are you sure you want to remove " + stationToRemove.getName()  + "?");
			if(boolInput)	//If the user verifies that they want to complete the operation
			{
				//Try to delete the user via StationID
				//The XML handling may cause errors so wrap it in a try/catch
				try 
				{
					removedStatus = sm.removeStation(stationToRemove.getID());
				} catch (Exception e) {
					System.out.println("ERROR: Cannot remove station :(");
					System.out.print("\n\n\nPress ENTER to continue...");
					return false;
				}
			}
			else	//Else the user did not say yes to the "are you sure" prompt, set flag to false again
			{
				removedStatus = false;
			}
			
			if(removedStatus)	//If the removed flag was set to true, the operation was completed
			{
				System.out.println("Station removed successfully!\n");
			}
			else	//Else, the flag was never set, therefore the operation was cancelled
			{
				System.out.println("Station not removed, operation cancelled.\n");
			}
			
			//Prompt the user if they would like to remove another user
			if(!InputHelper.getYesNo(scanner, "Would you like to remove another station?"))
			{
				return false;
			}
		}
	}
	
	//Method for modifying stations, basically serves as a remove and add function. The old station is removed and the data + modified data is added as a new station with the same ID
	private static boolean modifyStation(StationManager sm, Scanner scanner)
	{
		//While true loop for making sure that if the user fails to input valid data it or if the user wants to modify another, it restarts
		while(true)
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Admin Menu > Modify Station");
			System.out.println("NOTE: press ENTER to skip modifying an input field");
			
			//Select the station that needs modification
			Station stationToRemove = selectSingleStation(sm, scanner);	//Call the selectSingleStation which searches and returns a station or null if quit
			if(stationToRemove == null)	//Null flag is used to show that the user quit the operation
			{
				return false;	//So quit
			}
			
			int newID = stationToRemove.getID();	//Set the ID for the "new" station
			
			//Getting "new" station name
			String newName;
			while(true)
			{
				System.out.print("Modify station name (was: \"" + stationToRemove.getName() + "\"): ");
				newName = scanner.nextLine();
				
				//If user presses enter, keep old value
				if(newName.isBlank())
				{
					newName = stationToRemove.getName();
					break;
				}
				//Otherwise validate new value
				if(Validator.isValidStationName(sm, newName))
				{
					break;
				}
			}

			//Getting "new" latitude
			String input;
			double newLat;
			while(true)
			{
				System.out.print("Modify latitude (was: \"" + stationToRemove.getLatitude() + "\"): ");
				input = scanner.nextLine();
				
				//If user presses enter, keep old value
				if(input.isBlank())
				{
					newLat = stationToRemove.getLatitude();
					break;
				}
				//Otherwise validate input
				if(Validator.isValidLatitude(input))
				{
					newLat = Double.parseDouble(input);	//Safe to parse
					break;
				}
			}
			
			//Getting "new" longitude
			double newLong;
			while(true)
			{
				System.out.print("Modify longitude (was: \"" + stationToRemove.getLongitude() + "\"): ");
				input = scanner.nextLine();
				
				//If user presses enter, keep old value
				if(input.isBlank())
				{
					newLong = stationToRemove.getLongitude();
					break;
				}
				//Otherwise validate value
				if(Validator.isValidLongitude(input))
				{
					newLong = Double.parseDouble(input);	//Safe to parse
					break;
				}
			}
			
			
			EnumSet<FuelType> newSupportedFuel = EnumSet.allOf(FuelType.class);	
			
			//Prompt user if the station is a refuel only station
			boolean isRefuel = InputHelper.getYesNo(scanner, "Is this a refuel station?");
			
			//Try to remove the station and then create a new station with all the new attributes, the illusion of modification
			//The XML handling may cause errors so wrap it in a try/catch
			try 
			{
				sm.removeStation(stationToRemove.getID());	//First remove the station
				sm.addStation(newID, newName, newLat, newLong, newSupportedFuel, isRefuel);	//Then re-add the station
			} catch (Exception e) {
				System.out.println("ERROR: Cannot modify station :(");
				System.out.print("\n\n\nPress ENTER to continue...");
				return false;
			}
			
			System.out.println("Station modified successfully!\n");
			if(!InputHelper.getYesNo(scanner, "Would you like to modify another station?"))
			{
				return false;
			}
			return true;
		}
	}
	
	//Simple method for displaying all buses, the admin can search for one or just press enter to show all
	private static void displayBuses(BusManager bm, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Display Buses");
		
		String input;
		boolean boolInput;
		
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
				return;
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
					    "**%d\t- %s  -  Fuel Tank Size: %d gal  -  Fuel Burn: %d gal/hr  -  Max Range: %.2f miles  -  Cruise Speed: %dmph  -  Bus Type: %s  -  BusID: %d  -  Fuel Type: %s%n",
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
			
			System.out.print("\nPress ENTER to continue...");
			scanner.nextLine();
		}
	}
	
	//Method for adding a new bus
	private static boolean addBus(BusManager bm, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Add Bus");
		
		//Getting the new bus make and model
		String newMakeModel;
		while(true)
		{
			System.out.print("Enter bus make and model: ");
			newMakeModel = scanner.nextLine();
			//Check that the new make and model are valid
			if(Validator.isValidMakeModel(bm, newMakeModel))
			{
				break;
			}
		}
		
		//Prompt the user for the type of the new bus
		String newType;
		System.out.println("\nIs this bus:\n1. A city bus\nor\n2. A long distance bus");
		int userSelection = InputHelper.getIntInRange(scanner, 1, 2);
		//Set the value
		if(userSelection == 1)
		{
			newType = "city";
		}
		else
		{
			newType = "long_distance";
		}
		
		//Prompt the user for the fuel type of the new bus
		FuelType newFuelType;
		System.out.println("\nDoes this bus use:\n1. Gasoline\nor\n2. Diesel");
		userSelection = InputHelper.getIntInRange(scanner, 1, 2);
		//Set the value
		if(userSelection == 1)
		{
			newFuelType = FuelType.GAS;
		}
		else
		{
			newFuelType = FuelType.DIESEL;
		}
		
		//Getting the new fuel tank size
		String input;
		int newFuelSize;
		while(true)
		{
			System.out.print("Enter integer that represents fuel tank size (gal): ");
			input = scanner.nextLine();
			//Check that the new fuel size is valid
			if(Validator.isValidFuelTankSize(input))
			{
				newFuelSize = Integer.parseInt(input);
				break;
			}
		}
		
		//Getting the new burn rate
		int newBurnRate;
		while(true)
		{
			System.out.print("Enter integer that represents fuel burn rate (gal/hr): ");
			input = scanner.nextLine();
			//Check that the new fuel burn rate is valid
			if(Validator.isValidBurnRate(input))
			{
				newBurnRate = Integer.parseInt(input);
				break;
			}
				
		}
		
		//Getting the new cruise speed
		int newCruiseSpeed;
		while(true)
		{
			System.out.print("Enter integer that represents cruising speed (mph): ");
			input = scanner.nextLine();
			//Check that the new cruise speed is valid
			if(Validator.isValidCruiseSpeed(input))
			{
				newCruiseSpeed = Integer.parseInt(input);
				break;
			}
		}
		
		//Try to create the new bus with all the new attributes
		//The XML handling may cause errors so wrap it in a try/catch
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
	
	//Method for removing bus
	private static boolean removeBus(BusManager bm, Scanner scanner)	
	{
		boolean removedStatus = false;	//Boolean flag for it the bus has been removed
		//While true loop for making sure that if the user fails to input valid data it or if the user wants to remove another, it restarts
		while(true)
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Admin Menu > Remove Bus");
			
			Bus busToRemove = selectSingleBus(bm, scanner);	//Call the selectSingleBus which searches and returns a bus or null if quit
			if(busToRemove == null)	//Null flag is used to show that the user quit the operation
			{
				return false;	//So quit
			}
			
			//Verify that the user wants to remove the bus
			boolean boolInput = InputHelper.getYesNo(scanner, "Are you sure you want to remove " + busToRemove.getMakeModel() + " (ID: " + busToRemove.getID()  + ")?");
			if(boolInput)	//If the user verifies that they want to complete the operation
			{
				//Try to delete the user via BusID
				//The XML handling may cause errors so wrap it in a try/catch
				try 
				{
					removedStatus = bm.removeBus(busToRemove.getID());
				} catch (Exception e) {
					System.out.println("ERROR: Cannot remove bus :(");
					System.out.print("\n\n\nPress ENTER to continue...");
					return false;
				}
			}
			else	//Else the user did not say yes to the "are you sure" prompt, set flag to false again
			{
				removedStatus = false;
			}
			
			if(removedStatus)	//If the removed flag was set to true, the operation was completed
			{
				System.out.println("Bus removed successfully!\n");
			}
			else	//Else, the flag was never set, therefore the operation was cancelled
			{
				System.out.println("Bus not removed, operation cancelled.\n");
			}
			
			//Prompt the user if they would like to remove another user
			if(!InputHelper.getYesNo(scanner, "Would you like to remove another bus?"))
			{
				return false;
			}
		}
	}
	
	//Method for modifying bus, basically serves as a remove and add function. The old bus is removed and the data + modified data is added as a new bus with the same ID
	private static boolean modifyBus(BusManager bm, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Admin Menu > Modify Bus");
		System.out.println("NOTE: press ENTER to skip modifying an input field");
		
		//Select the bus that needs modification
		Bus busToRemove = selectSingleBus(bm, scanner);	//Call the selectSingleBus which searches and returns a bus or null if quit
		if(busToRemove == null)	//Null flag is used to show that the user quit the operation
		{
			return false;	//So quit
		}
		
		int newID = busToRemove.getID();	//Set the ID for the "new" bus
		
		//Getting "new" make and model
		String newMakeModel;
		while(true)
		{
			System.out.print("Modify bus make and model (was: \"" + busToRemove.getMakeModel() + "\"): ");
			newMakeModel = scanner.nextLine();
			
			//If user presses enter, keep old value
			if(newMakeModel.isBlank())
			{
				newMakeModel = busToRemove.getMakeModel();
				break;
			}
			//Otherwise validate value
			if(Validator.isValidMakeModel(bm, newMakeModel))
			{
				break;
			}
		}
		
		//Getting "new" type of bus
		String newType;
		System.out.println("\nModify bus type (was: \"" + busToRemove.getType() + "\"):\n1. A city bus\nor\n2. A long distance bus");
		int userSelection = InputHelper.getIntInRange(scanner, 1, 2);
		if(userSelection == 1)
		{
			newType = "city";
		}
		else
		{
			newType = "long_distance";
		}
		
		//Prompting for the "new" fuel type
		FuelType newFuelType;
		System.out.println("\nModify bus fuel type (was: \"" + busToRemove.getFuelTypeDisplay() + "\"):\n1. Gasoline\nor\n2. Diesel");
		userSelection = InputHelper.getIntInRange(scanner, 1, 2);
		//Set the value
		if(userSelection == 1)
		{
			newFuelType = FuelType.GAS;
		}
		else
		{
			newFuelType = FuelType.DIESEL;
		}
		
		//Getting the "new" fuel tank size
		String input;
		int newFuelSize;
		while(true)
		{
			System.out.print("Modify fuel tank size (was: \"" + busToRemove.getFuelSize() + "gal\"): ");
			input = scanner.nextLine();
			
			//If user presses enter, keep old value
			if(input.isBlank())
			{
				newFuelSize = busToRemove.getFuelSize();
				break;
			}
			//Otherwise validate input
			if(Validator.isValidFuelTankSize(input))
			{
				newFuelSize = Integer.parseInt(input);
				break;
			}
		}
		
		//Getting the "new" fuel burn rate
		int newBurnRate;
		while(true)
		{
			System.out.print("Modify fuel burn rate (was: \"" + busToRemove.getFuelBurn() + "gal/hr\"): ");
			input = scanner.nextLine();
			
			//If user presses enter, keep old value
			if(input.isBlank())
			{
				newBurnRate = busToRemove.getFuelBurn();
				break;
			}
			//Otherwise validate input
			if(Validator.isValidBurnRate(input))
			{
				newBurnRate = Integer.parseInt(input);
				break;
			}
		}
		
		//Getting the "new" cruise speed
		int newCruiseSpeed;
		while(true)
		{
			System.out.print("Modify cruising speed (was: \"" + busToRemove.getCruiseSpeed() + "mph\"): ");
			input = scanner.nextLine();
			
			//If user presses enter, keep old value
			if(input.isBlank())
			{
				newCruiseSpeed = busToRemove.getCruiseSpeed();
				break;
			}
			//Otherwise validate input
			if(Validator.isValidCruiseSpeed(input))
			{
				newCruiseSpeed = Integer.parseInt(input);
				break;
			}
		}
		
		//Try to remove the bus and then create a new bus with all the new attributes, the illusion of modification
		//The XML handling may cause errors so wrap it in a try/catch
		try 
		{
			bm.removeBus(busToRemove.getID());	//First remove the station
			bm.addBus(newID, newMakeModel, newType, newFuelType, newFuelSize, newBurnRate, newCruiseSpeed);	//Then re-add the station
		} catch (Exception e) {
			System.out.println("ERROR: Cannot modify bus :(");
			System.out.print("\n\n\nPress ENTER to continue...");
			return false;
		}
		
		System.out.println("Bus modified successfully!\n");
		if(!InputHelper.getYesNo(scanner, "Would you like to modify another bus?"))
		{
			return false;
		}
		return true;
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
			
			//Search for users
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
						    "**%d\t  -  Username: %s  -  Password: %s  -  UserID: %d  -  Is Admin? %s%n",
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
	
	//Admin single bus  selection method (shows more info than other single selection method)
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