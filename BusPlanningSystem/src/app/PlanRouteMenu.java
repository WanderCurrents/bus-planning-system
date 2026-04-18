package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Bus;
import model.Leg;
import model.Station;
import utility.InputHelper;
import xml.BusManager;
import xml.StationManager;
import xml.UserManager;

public class PlanRouteMenu 
{
	public static boolean planRouteMenu(UserManager um, StationManager sm, BusManager bm, Scanner scanner)
	{
		Bus selectedBus = selectSingleBus(bm, scanner);	//Bus selection area
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
		
		DisplayManager.printTravelPlan(selectedBus, travelPlan, scanner);
		System.out.print("\n\nPress ENTER to continue...");
		scanner.nextLine();
		return false;	//Exit loop
	}
	
	private static Bus selectSingleBus(BusManager bm, Scanner scanner)
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
						    "**%d\t- %s  -  Max Range: %.2f miles  -  Cruise Speed: %dmph  -  Bus Type: %s%n",
						    i + 1,
						    results.get(i).getMakeModel(),
						    results.get(i).getMaxRange(),
						    results.get(i).getCruiseSpeed(),
						    results.get(i).getTypeDisplay()
						);

				}
				
				//Get the user's bus selection
				
				DisplayManager.printFooter();
				
				userSelection = InputHelper.getIntInRange(scanner, 1, results.size());
				
				
				selectedBus = results.get(userSelection-1);	//Set the bus selection, -1 for index consideration
				searching = false;	//Exit the outer loop
			}
		}
		return selectedBus;
	}
	
	private static Station selectSingleStation(StationManager sm, Scanner scanner)
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
			List<Station> results = sm.subStringSearch(input, false);	//Since not admin search, set false flag to not display refuel stations
			
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
	private static Station selectStartStation(StationManager sm, Scanner scanner)
	{
		Station selectedStart = null;
		
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Plan Route > Bus Selection > Start Station");
		selectedStart = selectSingleStation(sm, scanner);
		
		return selectedStart;
	}
	private static ArrayList<Station> selectDestinations(StationManager sm, Station startStation, Scanner scanner)
	{
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
			if(startStation.equals(nextStation) || selectedDestinations.contains(nextStation))
			{
				System.out.println("\nInvalid input. Your next destination cannot be the same as your starting station or any destination station.");
				System.out.print("\n\nPress ENTER to continue...");
				scanner.nextLine();
				continue; 
			}
			
			selectedDestinations.add(nextStation);
			
			//This is the logic that allows the user to break out of this loop
			while (true) 
			{
				boolean boolInput = InputHelper.getYesNo(scanner, "Would you like to add another station destination?");
				
			    if(boolInput) 
			    {
			        moreStations = true;
			        break;
			    }
			    if(!boolInput) 
			    {
			        moreStations = false;
			        break;
			    }
			}
		}
		return selectedDestinations;
	}
	
}
