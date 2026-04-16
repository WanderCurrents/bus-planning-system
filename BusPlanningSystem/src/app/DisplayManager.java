package app;

import model.Bus;
import model.Leg;

import java.util.ArrayList;
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
	
	//Method for printing a header for a screen, if there is an input passed in then a special header is printed
	public static void printHeader(String headerInput)
	{
		System.out.println("================================================================");
		System.out.println(" " + headerInput);
		System.out.println("----------------------------------------------------------------\n");
	}
	public static void printHeader()
	{
		System.out.println("================================================================\n");
	}
	
	public static void printFooter()
	{
		System.out.println("----------------------------------------------------------------");
	}
	
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
	

	public static void printTravelPlan(Bus bus, ArrayList<Leg> travelPlan, Scanner scanner) 
	{
		clearScreen();
		printHeader("Plan Route Results");

		double totalTravelTime = 0;
		double totalDistance = 0;
		System.out.println("Travel Plan:");
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
		
		int travelTimeHrs = (int) totalTravelTime;	//Figuring out the hours
		int travelTimeMin = (int) Math.round((totalTravelTime - travelTimeHrs) * 60);	//Figuring out the remaining minutes
		
		System.out.printf("Total Travel Time: %d hours %d minutes%n", travelTimeHrs, travelTimeMin);	//Print the total time
		System.out.printf("Total Distance: %.3f miles%n", totalDistance);	
	}
	
	public static void printMoreInfo(Scanner scanner)
	{
		clearScreen();
		printHeader("More Info");
		
		System.out.println("Bus Planning System developed by Group 3");
		System.out.println("*Joseph Deskevich - Team Lead");
		System.out.println("*Andrew Stevens - System Architect & Technical Lead");
		System.out.println("*Eva Cerda - Algorithm & Routing Logic Specialist");
		System.out.println("*Caleb Wheeler - Documentation Lead & Software Support Developer");
		System.out.println("\nFor CSCIA360 - Spring 2026");
		System.out.print("\n\nPress ENTER to continue...");
		scanner.nextLine();
	}
}
