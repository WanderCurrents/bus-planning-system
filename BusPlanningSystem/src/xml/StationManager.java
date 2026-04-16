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
	private StationHandler handler;
	protected int idIndex;
	protected List<Station> list = new ArrayList<>();
	
	public StationManager() throws Exception
	{
		handler = new StationHandler();
		
		//Establish the list
		NodeList nodeBList = handler.doc.getElementsByTagName("station");
		for(int i=0; i<nodeBList.getLength(); i++)
		{
			Element e = (Element) nodeBList.item(i);
			list.add(convertElementToStation(e));
		}
		
		idIndex = getIDIndex();
	}
	
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
	
	public boolean removeStation(int targetID) throws Exception
	{
		for(int i=0; i<list.size(); i++)
		{
			if(list.get(i).getID() == targetID)
			{
				list.remove(i);		//Remove from the working list
				handler.removeStationXML(targetID);		//Remove from the XML DOM
				handler.saveXML();	//Save XML to file
				return true;
			}
		}
		return false;	//Not found
	}
	
	//Returns the stationID for a station based on station name, not case sensitive
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
	
	public List<Station> getStationList()
	{
		return list;
	}
	
	public void printStationList()
	{
		for(Station s : list)
		{
			System.out.println("ID: " + s.getID() + "\n\tName;  " + s.getName() + "\n\tLatitude: " + s.getLatitude() + "\n\tLongitude: " 
					+ s.getLongitude() + "\n\tSupported Fuel Types :" + s.getSupportedFuelTypes() + "\n\tIs Fuel Station: " + s.getIsFuelOnly());
		}
	}
	//SubString Search for regular stations only
	public List<Station> subStringSearch(String substring)
	{
		List<Station> matches = new ArrayList<>();
		String lower = substring.toLowerCase();
		for (Station s : list)
		{
			if (s.getName().toLowerCase().contains(lower) && s.getIsFuelOnly() == false)
			{
				matches.add(s);
			}
		}
		return matches;
	}
	
}
