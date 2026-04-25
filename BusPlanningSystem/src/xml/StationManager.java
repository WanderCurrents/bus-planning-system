// StationManager Class
//----------------------
//Description: class designed for managing middle-level operations between the application classes and low-level operations of the XML handling. Maintains the active list of stations.
//Attributes:	handler : StationHandler
//				idIndex : int
//				list : List<Station>
//Methods:	StationManager()
//			getIDIndex() : int
//			convertElementToStation(e : Element) : Station
//			addStation(inName : String, inLat : double, inLong : double, inFuelTypes : EnumSet<FuelType>, isFuelOnly : boolean) : void
//			addStation(inID : int, inName : String, inLat : double, inLong ; double, inFuelTypes : EnumSet<FuelType> : isFuelOnly : boolean) : void
//			removeStation(targetID : int) : boolean
//			findStation(stationName : String) : int
//			getStation(targetID : int) : Station
//			getStationList() : List<Station>
////		printStationList() : void
//			subStringSearch(substring : String, adminSearch : boolean) : List<Station>

package xml;

import model.FuelType;
import model.Station;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class StationManager 
{
	private StationHandler handler;						//Respective handler
	protected int idIndex;								//Next available ID
	protected List<Station> list = new ArrayList<>();	//Maintained list of Stations managed by the manager
	
	//Main constructor, creates the respective handler and sets in motion the XML database setup when a manager is created
	public StationManager() throws Exception
	{
		handler = new StationHandler();	//Create the handler
		
		//Establish the list
		NodeList nodeBList = handler.doc.getElementsByTagName("station");
		for(int i=0; i<nodeBList.getLength(); i++)
		{
			Element e = (Element) nodeBList.item(i);
			list.add(convertElementToStation(e));
		}
		
		idIndex = getIDIndex();	//Set the ID index
	}
	
	//Finds the next available ID index and sets it
	public int getIDIndex()
	{
		int maxID = -1;
		//Loop through the XML file and find the highest ID
		for(int i=0; i<list.size(); i++)
		{
			Station checkStation = list.get(i);
			int listID = checkStation.getID();
			if(listID > maxID)
				maxID = listID;
		}
		return maxID+1;	//Returns the next available ID value that can be used
	}
	
	//Converts DOM information into a Station object
	private Station convertElementToStation(Element e)
	{
		int id = Integer.parseInt(e.getAttribute("id"));
		String name = e.getElementsByTagName("name").item(0).getTextContent();
		double latitude = Double.parseDouble(e.getElementsByTagName("latitude").item(0).getTextContent());
		double longitude = Double.parseDouble(e.getElementsByTagName("longitude").item(0).getTextContent());
		String rawFuelTypes = e.getElementsByTagName("fueltypes").item(0).getTextContent().trim();
		EnumSet<FuelType> supported = EnumSet.noneOf(FuelType.class);
		for (String token : rawFuelTypes.split(","))
		{
			supported.add(FuelType.valueOf(token.trim()));
		}
		boolean isFuelOnly = Boolean.parseBoolean(e.getElementsByTagName("isfuelonly").item(0).getTextContent());
		
		return new Station(id, name, latitude, longitude, supported, isFuelOnly);
	}
	
	//Primary method for creating a station
	public void addStation(String inName, double inLat, double inLong, EnumSet<FuelType> inFuelTypes, boolean inIsFuelOnly) throws Exception
	{
		//Create the new station addition
		Station newStation = new Station(idIndex, inName, inLat, inLong, inFuelTypes, inIsFuelOnly);
		
		//Deal with DOM stuff
		handler.addStationXML(idIndex, inName, inLat, inLong, inFuelTypes, inIsFuelOnly);
		
		//Update the list
		list.add(newStation);
		
		//Save changes to XML file
		handler.saveXML();
		
		idIndex++;	//Update the index counter
	}
	
	//Overloaded method for creating Stations, passed in ID, no need to update ID, meant for station modification
	public void addStation(int inID, String inName, double inLat, double inLong, EnumSet<FuelType> inFuelTypes, boolean inIsFuelOnly) throws Exception
	{
		//Create the new station addition
		Station newStation = new Station(inID, inName, inLat, inLong, inFuelTypes, inIsFuelOnly);
		
		//Deal with DOM stuff
		handler.addStationXML(inID, inName, inLat, inLong, inFuelTypes, inIsFuelOnly);
		
		//Update the list
		list.add(newStation);
		
		//Save changes to XML file
		handler.saveXML();
		
		//DO NOT UPDATE ID, NO NEED
	}
	
	//Primary method for removing a station
	public boolean removeStation(int targetID) throws Exception
	{
		for(int i=0; i<list.size(); i++)
		{
			if(list.get(i).getID() == targetID)
			{
				list.remove(i);		//Remove from the working list
				handler.removeStationXML(targetID);		//Remove from the XML DOM
				handler.saveXML();	//Save XML to file
				return true;	//Return true if station was found and removed
			}
		}
		return false;	//Return false if the station was not found
	}
	
	//Returns the stationID for a station based on station name, not case sensitive, this method is called in the adding/modifying process to make sure there are no duplicates
	public int findStation(String stationName)
	{
		for(Station s : list)
		{
			if(s.getName().toLowerCase().equals(stationName))
			{
				return s.getID();
			}
		}
		return -99;	//Just a number to signal station not found
	}
	
	//Returns a station object based on the targetID
	public Station getStation(int targetID)
	{
		for(Station s : list)
		{
			if(s.getID() == targetID)
			{
				return s;
			}
		}
		return null;
	}
	
	//Returns the maintained list of stations
	public List<Station> getStationList()
	{
		return list;
	}
	
	//Debug method for printing the list of stations
//	public void printStationList()
//	{
//		for(Station s : list)
//		{
//			System.out.println("ID: " + s.getID() + "\n\tName;  " + s.getName() + "\n\tLatitude: " + s.getLatitude() + "\n\tLongitude: " 
//					+ s.getLongitude() + "\n\tSupported Fuel Types :" + s.getSupportedFuelTypes() + "\n\tIs Fuel Station: " + s.getIsFuelOnly());
//		}
//	}
	
	//Method for substring search and returns a list of matches
	public List<Station> subStringSearch(String substring, boolean adminSearch)
	{
		List<Station> matches = new ArrayList<>();
		String lower = substring.toLowerCase();
		if(!adminSearch)	//If not in admin search, do not return any refuel stations
		{
			for (Station s : list)
			{
				if (s.getName().toLowerCase().contains(lower) && s.getIsFuelOnly() == false)
				{
					matches.add(s);
				}
			}
		} 
		else if(adminSearch)	//If in admin search, return any station including refuel stations
		{
			for (Station s : list)
			{
				if (s.getName().toLowerCase().contains(lower))
				{
					matches.add(s);
				}
			}
		}
		return matches;
	}
}
