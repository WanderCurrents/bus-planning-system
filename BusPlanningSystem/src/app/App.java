package app;

import xml.UserManager;
import xml.StationManager;
import xml.BusManager;

public class App {

	public static void main(String[] args) 
	{
		
		try	//Create the managers, these should be the only instances of these managers to exist in this program
		{
			UserManager userManager = new UserManager();
			StationManager stationManager = new StationManager();
			BusManager busManager = new BusManager();
		}
		catch(Exception e)
		{
			System.out.println("Error creating managers. Error: " + e);
		}
		
		

	}

}
