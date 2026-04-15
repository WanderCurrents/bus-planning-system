package app;

import java.util.List;
import java.util.Scanner;

import model.Bus;
import model.User;
import utility.InputHelper;
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
			System.out.println("7. See stats");
			System.out.println("\n\n0. Exit Admin Menu");
			
			userSelection = InputHelper.getIntInRange(scanner, 0, 7);
			
			switch(userSelection)
			{
				case 1:
					//Add user
					break;
				case 2:
					//Remove user
					break;
				case 3:
					//Add station
					break;
				case 4:
					//remove station
					break;
				case 5:
					//Add bus
					break;
				case 6:
					//remove bus
					break;
				case 7:
					//see stats
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
		
		return false;
	}
	
	private static boolean removeUser(UserManager um, Scanner scanner)
	{
		boolean searching = true;
		boolean removedStatus = false;
		while(searching)
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
					System.out.println("ERROR: Removing user :(");
					System.out.print("\n\n\nPress ENTER to continue...");
					return false;
				}
			}
			
			if(!InputHelper.getYesNo(scanner, "Would you like to remove another user?"))
			{
				searching = false;
			}
			
		}
		return false;	//Exit loop in the admin menu
	}
	
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
}
