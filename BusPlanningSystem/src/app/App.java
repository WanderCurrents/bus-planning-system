package app;

import xml.UserManager;
import xml.StationManager;
import xml.BusManager;
import model.User;
import model.Bus;
import model.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class App {

	
	public static void main(String[] args) 
	{
		Scanner scanner = new Scanner(System.in);
		//Create all of the XML managers
		UserManager um = null;
		StationManager sm = null;
		BusManager bm = null;
		try 
		{
		    um = new UserManager();
		    sm = new StationManager();
		    bm = new BusManager();
		} catch(Exception e) {
		    System.out.print("Error creating managers. Error: ");
		    e.printStackTrace();
		}
		
		//Print the splash screen warning upon start up
		DisplayManager.printSplash(scanner);
		
		//Do login process to establish user
		boolean loginSuccess = false;
		
		do
		{
			loginSuccess = loginProcess(um, scanner);
		} while(!loginSuccess);
		
		//Enter the main menu (run-time loop)
		while(mainMenu(um, sm, bm, scanner))
		{
			//While main menu method is true, continue calling it
			//Once it returns false this "loop" exits
			//Nothing is supposed to go here
		}
		
		//Exit program
		DisplayManager.clearScreen();
		System.out.println("Exiting program...");
	}
	
	
	//Is the login process, called by the main method once every time the system is run
	public static boolean loginProcess(UserManager um,  Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Login Screen");
		System.out.println("\n\n\n");
		System.out.print("Username: ");
		String username = scanner.nextLine();
		
		int userID = um.findUser(username);
		if(userID == -99)	//If no user found
		{
			System.out.println("User not found! Would you like to create a user?");
			System.out.print("[Y/n]: ");
			String userIn = scanner.nextLine();
			if(userIn.toLowerCase().equals("y"))
			{
				addUserProcess(um, scanner);	//Make a new user
				
			}
			else
			{
				System.out.println("\nPlease try again.");
			}
		}
		else
		{
			System.out.print("Password: ");
			String password = scanner.nextLine();
			User foundUser = um.getUser(userID);
			if(foundUser.getPassword().equals(password))
			{
				System.out.println("\n\nSuccess! Redirecting to main menu!");
				um.setCurrentUser(userID);	//Set the current user after successful log in
				return true;
			}
			else
			{
				System.out.println("\nLogin failed, password incorrect. Please try again.");
				
			}
		}
		
		System.out.print("\n\n\n\nPress ENTER to continue...");
		scanner.nextLine();
		return false;
	}
	
	//Main menu area, basically the "run-time" environment, everything "happens" from here
	public static boolean mainMenu(UserManager um, StationManager sm, BusManager bm, Scanner scanner)
	{
		String input;
		int userSelection;
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Main Menu  -  Welcome " + um.getCurrentUsername() + "!");
		System.out.println();
		System.out.println("1. Plan route");
		System.out.println("2. View buses/stations");
		System.out.println("3. Admin menu");
		System.out.println("4. More info");
		System.out.println("\n\n0. Exit program");
		System.out.print("\n\nEnter selection (0-4): ");
	
		input = scanner.nextLine();	//Take input from user
		
		try
		{
			userSelection = Integer.parseInt(input);
		} catch(Exception e) {
			System.out.println("\n\nERROR: Processing input. Please only input integers. Please try again.");
			System.out.print("\n\n\n\nPress ENTER to continue...");
			scanner.nextLine();
			return true;
		}
		
		switch(userSelection)
		{
			case 1: 
				while(planRouteMenu(um, sm, bm, scanner))
				{
					//While planRouteMenu is true, keep calling it
					//Once it returns false, exit
					//This loop is supposed to be empty
				}
				break;
			case 2: 
				//View bus & station stuff
				break;
			case 3:
				//Admin menu
				break;
			case 4:
				//More info
				break;
			case 0:
				return false;	//Exit run-time loop
			default:
				System.out.println("\n\n\n\n\nInvalid option selected, please try again.");
				System.out.print("\n\n\n\nPress ENTER to continue...");
				scanner.nextLine();
		}
		
		return true;	//Continue run-time loop
	}
	
	public static boolean planRouteMenu(UserManager um, StationManager sm, BusManager bm, Scanner scanner)
	{
		Bus selectedBus = selectBus(bm, scanner);	//Bus selection area
		if(selectedBus == null)	//If it returns null, then the user quit, quit the whole process
		{
			return false;
		}
		RoutingSystem rs = new RoutingSystem(selectedBus, sm);
		Station startStation = selectStartStation(sm, scanner);	//Start station selection area
		if(startStation == null)	//If it returns null, then the user quit, quit the whole process
		{
			return false;
		}
		
		System.out.println("DEBUG: " + selectedBus.getMakeModel() + startStation.getName());
		return false;	//Exit loop
	}
	public static Bus selectBus(BusManager bm, Scanner scanner)
	{
		String input;
		int userSelection;
		boolean selectionLoop = true;
		Bus selectedBus = null;
		
		//Main select bus loop
		do
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Plan Route > Bus Selection");
			System.out.println();
			System.out.print("Search for bus (press ENTER to see all bus options): ");
			input = scanner.nextLine();
			
			//Search for buses
			System.out.println("\nSearching for buses that match make and model \"" + input + "\"...");
			List<Bus> results = bm.subStringSearch(input);
			
			//Print the results
			if(results.isEmpty())	//If the search is bad, state it's empty
			{
				System.out.println("\nNo buses found!");
				System.out.print("\n\nWould you like to try again (n=quit)? [Y/n] ");
				if(scanner.nextLine().toLowerCase().equals("n"))
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
						    "**%d\t- %s  -  Max Range: %.2f miles -  Cruise Speed: %dmph -  Fuel Type: %s%n",
						    i + 1,
						    results.get(i).getMakeModel(),
						    results.get(i).getMaxRange(),
						    results.get(i).getCruiseSpeed(),
						    results.get(i).getFuelTypeDisplay()
						);

				}
				
				//Get the user's bus selection
				selectionLoop = true;
				DisplayManager.printFooter();
				do
				{
					System.out.print("Select bus option (1-" + results.size() + "): ");
					input = scanner.nextLine();
					//Attempt to turn input into int
					try
					{
						userSelection = Integer.parseInt(input);
						if(!(userSelection>0 && userSelection<=results.size()))
						{
							System.out.println("Not a valid bus option! Please try again...");
						}
						else
						{
							selectedBus = results.get(userSelection-1);	//Set the bus selection, -1 for index consideration
							selectionLoop = false;	//Exit this loop
						}
					} catch(Exception e) {
						System.out.println("\n\nERROR: Processing input. Please only input integers. Please try again...");
					}
				} while(selectionLoop);
			}
		} while(selectionLoop);
		return selectedBus;
	}
	public static Station selectStartStation(StationManager sm, Scanner scanner)
	{
		String input;
		int userSelection;
		boolean selectionLoop = true;
		Station selectedStart = null;
		
		//Main select station loop
		do
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Plan Route > Bus Selection > Start Station");
			System.out.println();
			System.out.print("Search for starting station (enter name of city): ");
			input = scanner.nextLine();
			
			//Search for buses
			System.out.println("\nSearching for stations that match \"" + input + "\"...");
			List<Station> results = sm.subStringSearch(input);
			
			//Print the results
			if(results.isEmpty())	//If the search is bad, state it's empty
			{
				System.out.println("\nNo stations found!");
				System.out.print("\n\nWould you like to try again (n=quit)? [Y/n] ");
				if(scanner.nextLine().toLowerCase().equals("n"))
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
						    "**%d\t- %s  -  Lat: %.4f  -  Long: %.4f",
						    i + 1,
						    results.get(i).getName(),
						    results.get(i).getLatitude(),
						    results.get(i).getLongitude()
						);

				}
				
				//Get the user's bus selection
				selectionLoop = true;
				DisplayManager.printFooter();
				do
				{
					System.out.print("Select start station option (1-" + results.size() + "): ");
					input = scanner.nextLine();
					//Attempt to turn input into int
					try
					{
						userSelection = Integer.parseInt(input);
						if(!(userSelection>0 && userSelection<=results.size()))
						{
							System.out.println("Not a valid bus option! Please try again...");
						}
						else
						{
							selectedStart = results.get(userSelection-1);	//Set the bus selection, -1 for index consideration
							selectionLoop = false;	//Exit this loop
						}
					} catch(Exception e) {
						System.out.println("\n\nERROR: Processing input. Please only input integers. Please try again...");
					}
				} while(selectionLoop);
			}
		} while(selectionLoop);
		return selectedStart;
	}
	public static List<Station> selectDestinations(StationManager sm, Scanner scanner)
	{
		String input;
		int userSelection;
		boolean selectionLoop = true;
		Station selectedStart = null;
		
		//Main select station loop
		do
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Plan Route > Bus Selection > Start Station > Destination Station(s)");
			System.out.println();
			System.out.print("Search for destination station (enter name of city): ");
			input = scanner.nextLine();
			
			//Search for buses
			System.out.println("\nSearching for stations that match \"" + input + "\"...");
			List<Station> results = sm.subStringSearch(input);
			
			//Print the results
			if(results.isEmpty())	//If the search is bad, state it's empty
			{
				System.out.println("\nNo stations found!");
				System.out.print("\n\nWould you like to try again? [Y/n] ");
				if(scanner.nextLine().toLowerCase().equals("n"))
				{
					selectionLoop = false;	//Exit loop, return to main menu
					continue;
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
						    "**%d\t- %s  -  Lat: %.4f  -  Long: %.4f",
						    i + 1,
						    results.get(i).getName(),
						    results.get(i).getLatitude(),
						    results.get(i).getLongitude()
						);

				}
				
				//Get the user's bus selection
				selectionLoop = true;
				DisplayManager.printFooter();
				do
				{
					System.out.print("Select start station option (1-" + results.size() + "): ");
					input = scanner.nextLine();
					//Attempt to turn input into int
					try
					{
						userSelection = Integer.parseInt(input);
						if(!(userSelection>0 && userSelection<=results.size()))
						{
							System.out.println("Not a valid bus option! Please try again...");
						}
						else
						{
							selectedStart = results.get(userSelection-1);	//Set the bus selection, -1 for index consideration
							selectionLoop = false;	//Exit this loop
						}
					} catch(Exception e) {
						System.out.println("\n\nERROR: Processing input. Please only input integers. Please try again...");
					}
				} while(selectionLoop);
			}
		} while(selectionLoop);
		return selectedStart;
	}
	
	//This is the adding user process, could be run by the main method upon login or in admin menu
	public static void addUserProcess(UserManager um, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("User Creation Screen");
		System.out.print("Enter new username: ");
		String newUsername = scanner.nextLine();
		System.out.print("Enter new password: ");
		String newPassword = scanner.nextLine();
		boolean newAdmin = false;
		
		if(um.isCurrentUserAdmin() == true)
		{
			System.out.print("Is the new user an Admin? [Y/n] ");
			if(scanner.nextLine().toLowerCase().equals("y"))
			{
				newAdmin = true;
			}
		}
		
		try 
		{
			um.addUser(newUsername, newPassword, newAdmin);

			System.out.println("\n\nUser created!");
		} catch (Exception e) {
			System.out.println("ERROR: Creating new user, operation failed! :(");
			e.printStackTrace();
		}
	}
}
