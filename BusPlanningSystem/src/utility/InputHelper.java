package utility;

import java.util.Scanner;

public class InputHelper 
{
	//Basic Yes or No prompt that handles non-Y or N inputs, returns the appropriate boolean value
	public static boolean getYesNo(Scanner scanner, String prompt)
	{
		while(true)
		{
			System.out.print(prompt + "[Y/n] ");
			String input = scanner.nextLine().toLowerCase();
			
			if(input.equals("y"))
			{
				return true;
			}
			if(input.equals("n"))
			{
				return false;
			}
			
			System.out.println("Invalid input. Please enter 'y' or 'n'.");
		}
	}
	
	//Parses the input to make sure a legal integer is submitted
	public static int getInt(Scanner scanner)
	{
		while(true)
		{
			try
			{
				return Integer.parseInt(scanner.nextLine());
			} catch(Exception e)
			{
				System.out.println("\nInvalid input. Please enter an integer.");
			}
		}
	}
	//Overloaded version of above, allowing for a prompt to be passed in and making it look better
	public static int getInt(Scanner scanner, String prompt)
	{
		while(true)
		{
			System.out.print(prompt);
			try
			{
				return Integer.parseInt(scanner.nextLine());
			} catch(Exception e)
			{
				System.out.println("\nInvalid input. Please enter an integer.");	
			}
		}
	}
	
	//Prompts the user for an integer in the appropriate range
	public static int getIntInRange(Scanner scanner, int min, int max)
	{
		while(true)
		{
			int value = getInt(scanner, "Enter a selection (" + min + "-" + max + "): ");
			
			if(value >= min && value <= max)
			{
				return value;
			}
			
			System.out.println("\nInvalid selection. Enter a number between " + min + " and " + max + ".");
		}
	}
}
