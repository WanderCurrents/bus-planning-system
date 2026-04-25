//  BusManager Class
//----------------------
//Description: class designed for managing middle-level operations between the application classes and low-level operations of the XML handling. Maintains the active list of buses.
//Attributes:	handler : BusHandler
//				idIndex : int
//				list : List<Bus>
//Methods:	BusManager()
//			getIDIndex() : int
//			convertElementToBus(e : Element) : Bus
//			addBus(inMakeModel : String, inType : String, inFuelType : FuelType, inFuelSize : int, inFuelBurn : int, inCruiseSpeed : int) : void
//			addBus(inID : int, inMakeModel : String, inType : String, inFuelType : FuelType, inFuelSize : int, inFuelBurn : int, inCruiseSpeed : int) : void
//			removeBus(targetID : int) : boolean
//			findBus(busMakeModel : String) : int
//			getBus(targetID : int) : Bus
////		printBusList() : void
//			subStringSearch(substring : String) : List<Bus>

package xml;

import model.Bus;
import model.User;
import model.FuelType;
import model.Station;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class BusManager 
{
	private BusHandler handler;						//Respective handler
	protected int idIndex;							//Next available ID
	protected List<Bus> list = new ArrayList<>();	//Maintained list of Buses managed by the manager
	
	//Main constructor, creates the respective handler and sets in motion the XML database setup when a manager is created
	public BusManager() throws Exception
	{
		handler = new BusHandler();	//Create the handler
		
		//Establish the list
		NodeList nodeBList = handler.doc.getElementsByTagName("bus");
		for(int i=0; i<nodeBList.getLength(); i++)
		{
			Element e = (Element) nodeBList.item(i);
			list.add(convertElementToBus(e));
		}
		
		idIndex = getIDIndex();	//Set the ID index
	}
	
	//Finds the next available ID index and sets it
	protected int getIDIndex()
	{
		int maxID = -1;
		//Loop through the XML file and find the highest ID
		for(int i=0; i<list.size(); i++)
		{
			Bus checkBus = list.get(i);
			int listID = checkBus.getID();
			if(listID > maxID)
				maxID = listID;
		}
		return maxID+1;	//Returns the next available ID value that can be used
	}
	
	//Converts DOM information into a Bus object
	private Bus convertElementToBus(Element e)
	{
		int id = Integer.parseInt(e.getAttribute("id"));
		String makeModel = e.getElementsByTagName("makemodel").item(0).getTextContent();
		String type = e.getElementsByTagName("type").item(0).getTextContent();
		FuelType fuelType = FuelType.parseFuelType(e.getElementsByTagName("fueltype").item(0).getTextContent());
		int fuelSize = Integer.parseInt(e.getElementsByTagName("fuelsize").item(0).getTextContent());
		int fuelBurn = Integer.parseInt(e.getElementsByTagName("fuelburn").item(0).getTextContent());
		int cruiseSpeed = Integer.parseInt(e.getElementsByTagName("cruisespeed").item(0).getTextContent());
		
		return new Bus(id, makeModel, type, fuelType, fuelSize, fuelBurn, cruiseSpeed);
	}
	
	//Primary method for creating a bus
	public void addBus(String inMakeModel, String inType, FuelType inFuelType, int inFuelSize, int inFuelBurn, int inCruiseSpeed) throws Exception
	{
		//Create the new bus addition
		Bus newBus = new Bus(idIndex, inMakeModel, inType, inFuelType, inFuelSize, inFuelBurn, inCruiseSpeed);
		
		//Deal with DOM stuff
		handler.addBusXML(idIndex, inMakeModel, inType, inFuelType, inFuelSize, inFuelBurn, inCruiseSpeed);
		
		//Update the list
		list.add(newBus);
		
		//Save changes to XML file
		handler.saveXML();
		
		idIndex++;	//Update the index counter
	}
	
	//Overloaded alternative method for creating Bus, passed in ID, no need to update ID, meant for bus modification
	public void addBus(int inID, String inMakeModel, String inType, FuelType inFuelType, int inFuelSize, int inFuelBurn, int inCruiseSpeed) throws Exception
	{
		//Create the new bus addition
		Bus newBus = new Bus(inID, inMakeModel, inType, inFuelType, inFuelSize, inFuelBurn, inCruiseSpeed);
		
		//Deal with DOM stuff
		handler.addBusXML(inID, inMakeModel, inType, inFuelType, inFuelSize, inFuelBurn, inCruiseSpeed);
		
		//Update the list
		list.add(newBus);
		
		//Save changes to XML file
		handler.saveXML();
		
		//DO NOT UPDATE ID, NO NEED
	}
	
	//Primary method for removing a bus
	public boolean removeBus(int targetID) throws Exception
	{
		for(int i=0; i<list.size(); i++)
		{
			if(list.get(i).getID() == targetID)
			{
				list.remove(i);		//Remove from the working list
				handler.removeBusXML(targetID);	//Remove from XML DOM
				handler.saveXML(); 	//Save XML to file
				return true;	//Return true if bus was found and removed
			}
		}
		return false;	//Return false if the bus was not found
	}
	
	//Returns the busID for a bus based on make and model name, not case sensitive, this method is called in the adding/modifying process to make sure there are no duplicates
	public int findBus(String busMakeModel)
	{
		for(Bus b : list)
		{
			if(b.getMakeModel().toLowerCase().equals(busMakeModel))
			{
				return b.getID();
			}
		}
		return -99;	//Just a number to signal bus not found
	}
	
	//Returns a bus object based on the targetID
	public Bus getBus(int targetID)
	{
		for(Bus b : list)
		{
			if(b.getID() == targetID)
			{
				return b;
			}
		}
		return null;
	}
	
	//Debug method for printing the list of buses
//	public void printBusList()
//	{
//		for(Bus b : list)
//		{
//			System.out.println("ID: " + b.getID() + "\n\tMake & Model: " + b.getMakeModel() + "\n\tType: " + b.getType() 
//					+ "\n\tFuel Type: " + b.getFuelTypeDisplay() + "\n\tFuel Size: " + b.getFuelSize() 
//					+ "\n\tFuel Burn: " + b.getFuelBurn() + "\n\tCruise Speed: " + b.getCruiseSpeed());
//		}
//	}
	
	//Method for substring search and returns a list of matches
	public List<Bus> subStringSearch(String substring)
	{
		List<Bus> matches = new ArrayList<>();
		String lower = substring.toLowerCase();
		for (Bus b : list)
		{
			if (b.getMakeModel().toLowerCase().contains(lower))
			{
				matches.add(b);
			}
		}
		return matches;
	}
}

//"Time flies like an arrow. Fruit flies like banana." -Unknown
