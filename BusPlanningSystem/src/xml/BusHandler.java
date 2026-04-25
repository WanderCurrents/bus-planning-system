//  BusHandler Class
//----------------------
//Description: class designed for handling XML low-level operations for Buses and the buses.xml file. Extends XMLHandler class. Interacts with the BusManager when needed.
//Attributes: n/a
//Methods:	BusHandler()
//			addBusXML(idIndex : int, inMakeModel : String, inType : String, inFuelType : FuelType, inFuelSize : int, inFuelBurn : int, inCruiseSpeed : int) : void
//			removeBusXML(targetID : int) : boolean

package xml;

import model.FuelType;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class BusHandler extends XMLHandler
{
	//Constructor
	public BusHandler() throws Exception
	{
		super("resources/buses.xml");	//Sets the correct file path from the XMLHandler constructor
	}
	
	//Method that adds a bus to the XML document/file
	protected void addBusXML(int idIndex, String inMakeModel, String inType, FuelType inFuelType, int inFuelSize, int inFuelBurn, int inCruiseSpeed)
	{
		Element eNewBus = doc.createElement("bus");
		eNewBus.setAttribute("id", String.valueOf(idIndex));
		Element makemodel = doc.createElement("makemodel");
		makemodel.setTextContent(inMakeModel);
		Element type = doc.createElement("type");
		type.setTextContent(inType);
		Element fueltype = doc.createElement("fueltype");
		fueltype.setTextContent(inFuelType.toString());
		Element fuelsize = doc.createElement("fuelsize");
		fuelsize.setTextContent(String.valueOf(inFuelSize));
		Element fuelburn = doc.createElement("fuelburn");
		fuelburn.setTextContent(String.valueOf(inFuelBurn));
		Element cruisespeed = doc.createElement("cruisespeed");
		cruisespeed.setTextContent(String.valueOf(inCruiseSpeed));
		
		//Add all the inputs to the element
		eNewBus.appendChild(makemodel);
		eNewBus.appendChild(type);
		eNewBus.appendChild(fueltype);
		eNewBus.appendChild(fuelsize);
		eNewBus.appendChild(fuelburn);
		eNewBus.appendChild(cruisespeed);
		
		//Add the element to the document
		doc.getDocumentElement().appendChild(eNewBus);
	}
	
	//Method that removes a bus from the XML document/file
	protected boolean removeBusXML(int targetID)
	{
		NodeList nodes = doc.getElementsByTagName("bus");	//Not creating a new NodeList, just a pointer
		
		for(int i=0; i<nodes.getLength(); i++)
		{
			Element bus = (Element) nodes.item(i);
			if(Integer.parseInt(bus.getAttribute("id")) == targetID)
			{
				doc.getDocumentElement().removeChild(bus);
				return true;	//Return true if found and removed
			}
		}
		return false;	//Return false if not found
	}
}

//"But if I see one more cyber truck, I swear to God I'm gonna floor it." -Noah Kahan, 2026