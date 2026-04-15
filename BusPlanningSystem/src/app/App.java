package app;

import xml.UserManager;
import xml.StationManager;
import xml.BusManager;
import model.User;
import model.Bus;
import model.Station;
import model.Leg;
import utility.InputHelper;

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
			if(InputHelper.getYesNo(scanner, "User not found! Would you like to create a user?"))
			{
				addUserProcess(um, scanner);	//Make a new user
			}
			else
			{
				System.out.println("\nLogin failed, username incorrect. Please try again.");
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
		
		System.out.print("\nPress ENTER to continue...");
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
		System.out.println("1. Plan route");
		System.out.println("2. View buses/stations");
		System.out.println("3. Admin menu");
		System.out.println("4. More info");
		System.out.println("\n\n0. Exit program\n\n\n");
	
		userSelection = InputHelper.getIntInRange(scanner, 0, 4);
		
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
		ArrayList<Station> stops = selectDestinations(sm, startStation, scanner);	//Start the destination selection area
		if(stops == null)
		{
			return false;
		}
		stops.add(0, startStation);	//Adding the starting station to the beginning of the stops
		
		//Start doing the routing system stuff
		try 
		{
			rs.planRoute(stops);
		} catch(Exception e) 
		{
			System.out.println("ERROR: Calling plan route method, operation failed! :'(");
			e.printStackTrace();
			System.out.print("\n\nPress ENTER to continue...");
			scanner.nextLine();
			return false;
		}
		//Now that the routing system ran the plan route method, the travel plan should be updated
		ArrayList<Leg> travelPlan = rs.getTravelPlan();
		
		
		//DEBUG
//
//		System.out.println("DEBUG selected bus: " + selectedBus.getMakeModel());
//		System.out.println("DEBUG selected start: " + startStation.getName());
//		System.out.println("DEBUG destination: ");
//		for(Station s : stops)
//		{
//			System.out.println("Dest: " + s.getName());
//		}
//		System.out.println("DEBUG Legs:");
//		for(Leg l : travelPlan)
//		{
//			System.out.println("Start: " + l.getStartStation().getName() + "\tEnd: " + l.getEndStation().getName());
//		}
//		System.out.print("\n\n\n\nPress ENTER to continue...");
//		scanner.nextLine();
		////
		
		DisplayManager.printTravelPlan(selectedBus, travelPlan, scanner);
		System.out.print("\n\nPress ENTER to continue...");
		scanner.nextLine();
		return false;	//Exit loop
	}
	public static Bus selectBus(BusManager bm, Scanner scanner)
	{
		String input;
		boolean boolInput;
		int userSelection;
		boolean searching = true;
		Bus selectedBus = null;
		
		//Main select bus loop
		while(searching)
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
						    "**%d\t- %s  -  Max Range: %.2f miles -  Cruise Speed: %dmph -  Fuel Type: %s%n",
						    i + 1,
						    results.get(i).getMakeModel(),
						    results.get(i).getMaxRange(),
						    results.get(i).getCruiseSpeed(),
						    results.get(i).getFuelTypeDisplay()
						);

				}
				
				//Get the user's bus selection
				
				DisplayManager.printFooter();
				
				System.out.print("Select bus option (1-" + results.size() + "): ");
				userSelection = InputHelper.getIntInRange(scanner, 1, results.size());
				
				
				selectedBus = results.get(userSelection-1);	//Set the bus selection, -1 for index consideration
				searching = false;	//Exit the outer loop
			}
		}
		return selectedBus;
	}
	public static Station selectSingleStation(StationManager sm, Scanner scanner)
	{
		String input;
		boolean boolInput;
		int userSelection;
		Station selectedStation = null;
		
		//Main select station loop
		boolean searching = true;
		while(searching)
		{
			System.out.println();
			System.out.print("Search for station (enter name of city): ");
			input = scanner.nextLine();
			
			//Search for stations
			System.out.println("\nSearching for stations that match \"" + input + "\"...");
			List<Station> results = sm.subStringSearch(input);
			
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
						    "**%d\t- %s  -  Lat: %.4f  -  Long: %.4f%n",
						    i + 1,
						    results.get(i).getName(),
						    results.get(i).getLatitude(),
						    results.get(i).getLongitude()
						);

				}
				
				//Get the user's station selection
				DisplayManager.printFooter();
				
				userSelection = InputHelper.getIntInRange(scanner, 1, results.size());
				
				selectedStation = results.get(userSelection-1);	//Set the station selection, -1 for index consideration
				searching = false;	//Exit the outer loop
			}
		}
		return selectedStation;
	}
	
	public static Station selectStartStation(StationManager sm, Scanner scanner)
	{
		Station selectedStart = null;
		
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Plan Route > Bus Selection > Start Station");
		selectedStart = selectSingleStation(sm, scanner);
		
		return selectedStart;
	}
	public static ArrayList<Station> selectDestinations(StationManager sm, Station startStation, Scanner scanner)
	{
		String input;
		int userSelection;
		ArrayList<Station> selectedDestinations = new ArrayList<>();
		
		//This allows for multiple destination stations to be selected by user
		boolean moreStations = true;
		while(moreStations)
		{
			DisplayManager.clearScreen();
			DisplayManager.printHeader("Plan Route > Bus Selection > Start Station > Destination Station(s)");
			System.out.println("Current starting station: " + startStation.getName());
			//This allows for the current destination stations to be displayed, possible that none are so there is a case for this
			if(!selectedDestinations.isEmpty())
			{
				System.out.print("Current destinations: ");
				for(Station s : selectedDestinations)
				{
					System.out.print(s.getName() + " > ");
				}
				System.out.print("[currently selecting]");
			}
			else 
			{
				System.out.println("Current destinations: none yet!");
			}
			System.out.println("\n");
			
			//Add station to the list
			Station nextStation = selectSingleStation(sm, scanner);
			if(nextStation == null)	//If returned null, it means user quit inside this method...
			{
				return null;	//...so quit
			}
			
			selectedDestinations.add(selectSingleStation(sm, scanner));
			
			//This is the logic that allows the user to break out of this loop
			while (true) 
			{
				boolean boolInput = InputHelper.getYesNo(scanner, "Would you like to add another station destination? [Y/n] ");
				
			    if (boolInput) 
			    {
			        moreStations = true;
			        break;
			    }
			    if (!boolInput) 
			    {
			        moreStations = false;
			        break;
			    }
			}
		}
		return selectedDestinations;
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
			if(InputHelper.getYesNo(scanner, "Is the new user an Admin? [Y/n] "))
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
			System.out.print("\n\n\nPress ENTER to continue...");
			scanner.nextLine();
		}
	}
}
