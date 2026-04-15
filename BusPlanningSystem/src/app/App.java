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



public class App 
{	
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
		int userSelection;
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Main Menu  -  Welcome " + um.getCurrentUsername() + "!");
		System.out.println("1. Plan route");
		System.out.println("2. View buses/stations");
		System.out.println("3. Admin menu");
		System.out.println("4. More info");
		System.out.println("\n\n0. Exit program\n\n");
	
		userSelection = InputHelper.getIntInRange(scanner, 0, 4);
		
		switch(userSelection)
		{
			case 1: 
				while(PlanRouteMenu.planRouteMenu(um, sm, bm, scanner))
				{
					//While planRouteMenu is true, keep calling it
					//Once it returns false, exit
					//This loop is supposed to be empty
				}
				break;
			case 2: 
				
				break;
			case 3:
				while(AdminMenu.adminMenu(um, sm, bm, scanner))
				{
					//While adminMenu is true, keep calling it
					//Once it returns false, exit
					//This loop is supposed to be empty
				}
				break;
			case 4:
				//More info
				break;
			case 0:
				return false;	//Exit run-time loop
			default:
				System.out.println("\n\n\n\n\nInvalid option selected, please try again.");
				System.out.print("\n\n\nPress ENTER to continue...");
				scanner.nextLine();
		}
		
		return true;	//Continue run-time loop
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
