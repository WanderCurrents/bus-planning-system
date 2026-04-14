package app;

import xml.UserManager;
import xml.StationManager;
import xml.BusManager;
import model.User;

import java.util.ArrayList;
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
		} catch (Exception e) {
		    System.out.print("Error creating managers. Error: ");
		    e.printStackTrace();
		}
		
		DisplayManager.printSplash(scanner);
		
		boolean loginSuccess = false;
		
		do
		{
			loginSuccess = loginProcess(um, scanner);
		} while(!loginSuccess);
		
	}
	
	
	
	public static boolean loginProcess(UserManager um, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("Login Screen");
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
			return false;	//Force re-login
		}
		else
		{
			System.out.print("Password: ");
			String password = scanner.nextLine();
			User foundUser = um.getUser(userID);
			if(foundUser.getPassword().equals(password))
			{
				System.out.println("\nSuccess! Redirecting to main menu!");
				um.setCurrentUser(userID);	//Set the current user after successful log in
				return true;
			}
			else
			{
				System.out.println("\nLogin failed, password incorrect.");
				System.out.print("\n\n\n\nPress ENTER to continue...");
				scanner.nextLine();
				return false;
			}
		}
	}
	public static void addUserProcess(UserManager um, Scanner scanner)
	{
		DisplayManager.clearScreen();
		DisplayManager.printHeader("User Creation Screen");
		System.out.print("Enter new username: ");
		String newUsername = scanner.nextLine();
		System.out.print("Enter new password: ");
		String newPassword = scanner.nextLine();
		boolean newAdmin = false;
		
		try 
		{
			um.addUser(newUsername, newPassword, newAdmin);

			System.out.println("\n\nUser created!");
			System.out.print("\n\n\n\nPress ENTER to continue...");
			scanner.nextLine();
		} catch (Exception e) 
		{
			System.out.println("ERROR: Creating new user, operation failed! :(");
			e.printStackTrace();
		}
		
	}
}
