package app;

import java.util.Scanner;

public class DisplayManager 
{
	public static void clearScreen()
	{
		try
		{
			if(System.console() == null)	//If the Eclipse terminal is detected, fall back to spray and pray method of clearing screen
			{
				for (int i = 0; i < 50; i++) 
				{
		            System.out.println();
				}
			}
			else
			{
				//Using ANSI codes, clear the terminal screen, this is the ideal way
				System.out.println("\033[H\033[2J");	//"\033[H" moves cursor to top left, "\033[2J" clears the screen
				System.out.flush();	//Force it to update
			}
			
		} catch (Exception e)
		{
			//Old terminals sometimes don't support ANSI codes, this is the fall-back
			for(int i = 0; i < 50; i++)	//Spray and pray baby
			{
				System.out.println();
			}
		}
	}
	
	public static void printSplash(Scanner scanner)
	{
		
		clearScreen();
							
		System.out.println("****************************************************************");
		System.out.println("                           WARNING!                            ");
		System.out.println("****************************************************************");
		System.out.println("\n\n\n\n\n  THIS SOFTWARE IS NOT TO BE USED FOR ROUTE PLANNING PURPOSES\n\n");
		System.out.print("\n\n\n\nPress ENTER to continue...");
		scanner.nextLine();
	}
	
	//Method for printing a header for a screen, if there is an input passed in then a special header is printed
	public static void printHeader(String headerInput)
	{
		System.out.println("================================================================");
		System.out.println(" " + headerInput);
		System.out.println("----------------------------------------------------------------\n\n");
	}
	public static void printHeader()
	{
		System.out.println("================================================================\n\n");
	}
	
	public static void printFooter()
	{
		System.out.println("----------------------------------------------------------------");
	}
}
