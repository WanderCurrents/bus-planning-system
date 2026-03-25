package xml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class StationHandler extends XMLHandler 
{
	public StationHandler() throws Exception
	{
		super("resources/stations.xml");
	}
	
	protected void addStationXML(int idIndex, String inName, double inLat, double inLong, boolean inIsFuelOnly)
	{
		Element eNewStation = doc.createElement("station");
		eNewStation.setAttribute("id", String.valueOf(idIndex));
		Element name = doc.createElement("name");
		name.setTextContent(inName);
		Element latitude = doc.createElement("latitude");
		latitude.setTextContent(String.valueOf(inLat));
		Element longitude = doc.createElement("longitude");
		longitude.setTextContent(String.valueOf(inLong));
		Element isFuelOnly = doc.createElement("isfuelonly");
		isFuelOnly.setTextContent(String.valueOf(inIsFuelOnly));
		
		//Add all the inputs to the element
		eNewStation.appendChild(name);
		eNewStation.appendChild(latitude);
		eNewStation.appendChild(longitude);
		eNewStation.appendChild(isFuelOnly);
		
		//Add the element to the document
		doc.getDocumentElement().appendChild(eNewStation);
	}
	
	protected boolean removeStationXML(int targetID)
	{
		NodeList nodes = doc.getElementsByTagName("station");	//Not creating a new NodeList, just a pointer
		
		for(int i=0; i<nodes.getLength(); i++)
		{
			Element station = (Element) nodes.item(i);
			if(Integer.parseInt(station.getAttribute("id")) == targetID)
			{
				doc.getDocumentElement().removeChild(station);
				return true;
			}
		}
		
		return false;
	}
}
