//  StationHandler Class
//----------------------
//Description: class designed for handling XML low-level operations for Stations and the stations.xml file. Extends XMLHandler class. Interacts with the StationManager when needed.
//Attributes: n/a
//Methods:	StationHandler()
//			addStationXML(idIndex : int, inName : String, inLat : double, inLong : double, inFuelType : EnumSet<FuelType>, inIsFuelOnly : boolean) : void
//			removeStationXML(targetID : int) : boolean

package xml;

import model.FuelType;

import java.util.EnumSet;
import java.util.stream.Collectors;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class StationHandler extends XMLHandler 
{
	//Constructor
	public StationHandler() throws Exception
	{
		super("resources/stations.xml");	//Sets the correct file path from the XMLHandler constructor
	}
	
	//Method that adds a station to the XML document/file
	protected void addStationXML(int idIndex, String inName, double inLat, double inLong, EnumSet<FuelType> inFuelType, boolean inIsFuelOnly)
	{
		Element eNewStation = doc.createElement("station");
		eNewStation.setAttribute("id", String.valueOf(idIndex));
		Element name = doc.createElement("name");
		name.setTextContent(inName);
		Element latitude = doc.createElement("latitude");
		latitude.setTextContent(String.valueOf(inLat));
		Element longitude = doc.createElement("longitude");
		longitude.setTextContent(String.valueOf(inLong));
		Element fueltypes = doc.createElement("fueltypes");
		String joinedFuelTypes = inFuelType
									.stream()
									.map(Enum::name)
									.collect(Collectors.joining(","));
		fueltypes.setTextContent(joinedFuelTypes);
		
		Element isFuelOnly = doc.createElement("isfuelonly");
		isFuelOnly.setTextContent(String.valueOf(inIsFuelOnly));
		
		//Add all the inputs to the element
		eNewStation.appendChild(name);
		eNewStation.appendChild(latitude);
		eNewStation.appendChild(longitude);
		eNewStation.appendChild(fueltypes);
		eNewStation.appendChild(isFuelOnly);
		
		//Add the element to the document
		doc.getDocumentElement().appendChild(eNewStation);
	}
	
	//Method that removes a station from the XML document/file
	protected boolean removeStationXML(int targetID)
	{
		NodeList nodes = doc.getElementsByTagName("station");	//Not creating a new NodeList, just a pointer
		
		for(int i=0; i<nodes.getLength(); i++)
		{
			Element station = (Element) nodes.item(i);
			if(Integer.parseInt(station.getAttribute("id")) == targetID)
			{
				doc.getDocumentElement().removeChild(station);
				return true;	//Return true if found and removed
			}
		}
		return false;	//Return false if not found
	}
}
