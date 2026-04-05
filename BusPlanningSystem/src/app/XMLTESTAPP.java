package app;

import xml.UserManager;
import xml.StationManager;
import xml.BusManager;

import java.util.EnumSet;
import java.util.Scanner;

import model.FuelType;

public class XMLTESTAPP {

	public static void main(String[] args) throws Exception 
	{
		UserManager userManager = new UserManager();
		BusManager busManager = new BusManager();
		StationManager stationManager = new StationManager();
		Scanner scanner = new Scanner(System.in);
		int selection = -1;

		System.out.println("XML TEST APP..................");
		do
		{
			System.out.println("\n\n");
			System.out.println("------------------------------------------------");
			System.out.println("1. Print all users");
			System.out.println("2. Print all buses");
			System.out.println("3. Print all stations");
			System.out.println("4. Add/remove users");
			System.out.println("5. Add/remove buses");
			System.out.println("6. Add/remove stations");
			System.out.println("\n0. Exit\n\n");
			System.out.print("Select option (0-6): ");
			selection = Integer.parseInt(scanner.nextLine());
			System.out.println("\n\n\n\n\n\n\n\n");
			switch(selection)
			{
				case 1:
					System.out.println("Printing users...");
					userManager.printUserList();
					break;
				case 2:
					System.out.println("Printing buses...");
					busManager.printBusList();
					break;
				case 3:
					System.out.println("Printing stations...");
					stationManager.printStationList();
					break;
				case 4:
					System.out.println("Do you want to add or remove a user?");
					System.out.print("Select option (1 = ADD; 2 = REMOVE): ");
					selection = Integer.parseInt(scanner.nextLine());
					if(selection == 1)
					{
						System.out.println("\n\n\n\n\n\n\n\n");
						System.out.print("Enter username: ");
						String username = scanner.nextLine();
						System.out.print("Enter password: ");
						String password = scanner.nextLine();
						System.out.print("Enter 1 if admin (0 by default): ");
						boolean isAdmin = false;
						if(Integer.parseInt(scanner.nextLine()) == 1)
						{
							isAdmin = true;
						}
						userManager.addUser(username, password, isAdmin);
					}
					else if(selection == 2)
					{
						System.out.println("\n\n\n\n\n\n\n\n");
						System.out.print("Enter the ID of the user to remove: ");
						int targetID = Integer.parseInt(scanner.nextLine());
						boolean removedStatus = userManager.removeUser(targetID);
						if(removedStatus == true)
						{
							System.out.println("User removed successfully!");
						}
						else
						{
							System.out.println("User not found!");
						}
					}
					else
					{
						System.out.println("Invalid option, returning...");
					}
					break;
				case 5:
					System.out.println("Do you want to add or remove a bus?");
					System.out.print("Select option (1 = ADD; 2 = REMOVE): ");
					selection = Integer.parseInt(scanner.nextLine());
					if(selection == 1)
					{
						System.out.println("\n\n\n\n\n\n\n\n");
						System.out.print("Enter make & model: ");
						String makeModel = scanner.nextLine();
						System.out.print("Enter the type (long_distance or city): ");		//need to enforce this
						String type = scanner.nextLine();
						System.out.print("Enter the fuel type (gas or diesel): ");
						FuelType fuelType = FuelType.parseFuelType(scanner.nextLine());
						System.out.print("Enter fuel tank size (gal): ");
						int fuelSize = Integer.parseInt(scanner.nextLine());
						System.out.print("Enter fuel burn rate (gal/hr): ");
						int fuelBurn = Integer.parseInt(scanner.nextLine());
						System.out.print("Enter cruise speed: ");
						int cruiseSpeed = Integer.parseInt(scanner.nextLine());
						busManager.addBus(makeModel, type, fuelType, fuelSize, fuelBurn, cruiseSpeed);
					}
					else if(selection == 2)
					{
						System.out.println("\n\n\n\n\n\n\n\n");
						System.out.print("Enter the ID of the bus to remove: ");
						int targetID = Integer.parseInt(scanner.nextLine());
						boolean removedStatus = busManager.removeBus(targetID);
						if(removedStatus == true)
						{
							System.out.println("Bus removed successfully!");
						}
						else
						{
							System.out.println("Bus not found!");
						}
					}
					else
					{
						System.out.println("Invalid option, returning...");
					}
					break;
				case 6:
					System.out.println("Do you want to add or remove a station?");
					System.out.print("Select option (1 = ADD; 2 = REMOVE): ");
					selection = Integer.parseInt(scanner.nextLine());
					if(selection == 1)
					{
						System.out.println("\n\n\n\n\n\n\n\n");
						System.out.print("Enter name: ");
						String name = scanner.nextLine();
						System.out.print("Enter the latitude: ");
						double lat = Double.parseDouble(scanner.nextLine());
						System.out.print("Enter the longitude: ");
						double lon = Double.parseDouble(scanner.nextLine());
						EnumSet<FuelType> supportedFuel = EnumSet.noneOf(FuelType.class);
						System.out.print("Does this station support gas? (y/n)");
						if(scanner.nextLine().toLowerCase().equals("y"))
						{
							supportedFuel.add(FuelType.GAS);
						}
						System.out.print("Does this station support diesel? (y/n)");
						if(scanner.nextLine().toLowerCase().equals("y"))
						{
							supportedFuel.add(FuelType.DIESEL);
						}
						System.out.print("Enter 1 if refuel station (0 by default): ");
						boolean isRefuel = false;
						if(Integer.parseInt(scanner.nextLine()) == 1)
						{
							isRefuel = true;
						}
						stationManager.addStation(name, lat, lon, supportedFuel, isRefuel);
					}
					else if(selection == 2)
					{
						System.out.println("\n\n\n\n\n\n\n\n");
						System.out.print("Enter the ID of the station to remove: ");
						int targetID = Integer.parseInt(scanner.nextLine());
						boolean removedStatus = stationManager.removeStation(targetID);
						if(removedStatus == true)
						{
							System.out.println("Station removed successfully!");
						}
						else
						{
							System.out.println("Station not found!");
						}
					}
					else
					{
						System.out.println("Invalid option, returning...");
					}
					break;
				case 0:
					System.out.println("Exiting program...");
					break;
				default:
					System.out.println("Invalid option, returning...");
			}
		}while(selection != 0);
	}

}
