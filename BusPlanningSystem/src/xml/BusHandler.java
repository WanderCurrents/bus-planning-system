package xml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BusHandler extends XMLHandler
{
	public BusHandler() throws Exception
	{
		super("resources/buses.xml");
	}
	
	protected void addBusXML(int idIndex, String inMakeModel, String inType, int inFuelSize, int inFuelBurn, int inCruiseSpeed)
	{
		Element eNewBus = doc.createElement("bus");
		eNewBus.setAttribute("id", String.valueOf(idIndex));
		Element makemodel = doc.createElement("makemodel");
		makemodel.setTextContent(inMakeModel);
		Element type = doc.createElement("type");
		type.setTextContent(inType);
		Element fuelsize = doc.createElement("fuelsize");
		fuelsize.setTextContent(String.valueOf(inFuelSize));
		Element fuelburn = doc.createElement("fuelburn");
		fuelburn.setTextContent(String.valueOf(inFuelBurn));
		Element cruisespeed = doc.createElement("cruisespeed");
		cruisespeed.setTextContent(String.valueOf(inCruiseSpeed));
		
		//Add all the inputs to the element
		eNewBus.appendChild(makemodel);
		eNewBus.appendChild(type);
		eNewBus.appendChild(fuelsize);
		eNewBus.appendChild(fuelburn);
		eNewBus.appendChild(cruisespeed);
		
		
		//Add the element to the document
		doc.getDocumentElement().appendChild(eNewBus);
	}
	
	protected boolean removeBusXML(int targetID)
	{
		NodeList nodes = doc.getElementsByTagName("bus");	//Not creating a new NodeList, just a pointer
		
		for(int i=0; i<nodes.getLength(); i++)
		{
			Element bus = (Element) nodes.item(i);
			if(Integer.parseInt(bus.getAttribute("id")) == targetID)
			{
				doc.getDocumentElement().removeChild(bus);
				return true;
			}
		}
		
		return false;
	}
}
