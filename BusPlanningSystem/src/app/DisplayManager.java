//   DisplayManager Class
//----------------------
//Description: class that holds the methods helpful for displaying general things
//Attributes: n/a
//Methods:	clearScreen() : void
//			printHeader(headerInput : String) : void
//			printHeader() : void
//			printFooter() : void
//			printSplash(scanner : Scanner) : void
//			printTravelPlan(bus : Bus, travelPlan : ArrayList<Leg>, scanner : Scanner) : void
//			printMoreInfo(scanner : Scanner) : void

package app;

import model.Bus;
import model.Leg;
import utility.InputHelper;

import java.util.ArrayList;
import java.util.Scanner;

public class DisplayManager 
{
	//Method for clearing the screen, supports ANSI clearing codes for terminal environments and also fallback to printing a bunch of new lines
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
	
	//Method for printing a header for a screen
	public static void printHeader()
	{
		System.out.println("================================================================\n");
	}
	//Overloaded method for printing a header, takes the input as an argument and prints it
	public static void printHeader(String headerInput)
	{
		System.out.println("================================================================");
		System.out.println(" " + headerInput);
		System.out.println("----------------------------------------------------------------\n");
	}
	
	//Prints the footer, useful for separating user input fields occasionally
	public static void printFooter()
	{
		System.out.println("----------------------------------------------------------------");
	}
	
	//Prints the warning splash screen
	public static void printSplash(Scanner scanner)
	{
		
		clearScreen();
							
		System.out.println("****************************************************************");
		System.out.println("                           WARNING!                            ");
		System.out.println("****************************************************************");
		System.out.println("\n\n\n\n\n  THIS SOFTWARE IS NOT TO BE USED FOR ROUTE PLANNING PURPOSES\n\n");
		System.out.print("\n\n\nPress ENTER to continue...");
		scanner.nextLine();
	}
	
	//Prints the travel plan, given a list of legs from route planning system
	public static void printTravelPlan(Bus bus, ArrayList<Leg> travelPlan, Scanner scanner) 
	{
		clearScreen();
		printHeader("Plan Route Results");

		//Summary information graphic
		double totalTravelTime = 0;
		double totalDistance = 0;
		System.out.println("Summary of Travel Plan:");
		System.out.print("(start) " + travelPlan.get(0).getStartStation().getName());
		for(Leg l : travelPlan)
		{
			totalTravelTime += l.getTime();
			totalDistance += l.getDist();
			System.out.printf(" >>>(%smiles)>>> ",		//Part that is going to be the blueprint for formatted data for distance
				    String.format("%.2f", l.getDist()).replaceAll("\\.?0+$", "")	//Distance is formatted to only have 2 digits of precision when necessary
				);
			System.out.print(l.getEndStation().getName());
			
		}
		System.out.print(" (end)\n\n");
		
		//Summary information for the whole trip
		int travelTimeHrs = (int) totalTravelTime;	//Figuring out the hours
		int travelTimeMin = (int) Math.round((totalTravelTime - travelTimeHrs) * 60);	//Figuring out the remaining minutes
		if(travelTimeMin == 60)	//Bug can happen where the minutes 59.999 is rounded to 60, when this happens do this
		{
			travelTimeHrs += 1;	//Add one hour to hours
			travelTimeMin = 0;	//Reset minutes back to 0
		}
		System.out.printf("Total Travel Time: %d hours %d minutes%n", travelTimeHrs, travelTimeMin);	//Print the total time
		System.out.printf("Total Distance: %.3f miles%n", totalDistance);
		System.out.println();
		
		//Gives the option to print travel plan a leg at a time for a more detailed view
		if(InputHelper.getYesNo(scanner, "See detailed travel plan?"))
		{
			System.out.println("\n-----Detailed Travel Plan-----");
			for(int i = 0; i < travelPlan.size(); i++)
			{
				System.out.println("Leg " + (i+1) + "  -  " + travelPlan.get(i).getStartStation().getName() + " to " + travelPlan.get(i).getEndStation().getName());
				System.out.printf("**Distance: %.4f miles%n", travelPlan.get(i).getDist());
				System.out.printf("**Heading: %.4f degrees%n", travelPlan.get(i).getHeading());
				int legTimeHrs = (int) travelPlan.get(i).getTime();
				int legTimeMin = (int) Math.round((travelPlan.get(i).getTime() - legTimeHrs) * 60);
				if(legTimeMin == 60)
				{
					legTimeHrs += 1;
					legTimeMin = 0;
				}
				System.out.println("**Travel Time: " + legTimeHrs + " hours " + legTimeMin + " minutes");
				System.out.println();
			}
		}
	}
	
	//Prints the group information in the "More Info" menu option
	public static void printMoreInfo(Scanner scanner)
	{
		clearScreen();
		printHeader("More Info");
		
		System.out.println("Bus Planning System developed by Group 3");
		System.out.println("*Andrew Stevens - System Architect & Technical Lead");
		System.out.println("*Eva Cerda - Algorithm/Routing Logic Specialist & Lead QA");
		System.out.println("*Caleb Wheeler - Documentation Lead");
		System.out.println("*Joseph Deskevich - Team Lead");
		System.out.println("\nFor CSCIA360 - Spring 2026");
		System.out.print("\n\nPress ENTER to continue...");
		scanner.nextLine();
	}
}

//"...You know, the sun doesn't care whether the grass appreciates its rays, right? It just keeps on shining..." -Ethan Hawke, March 2026