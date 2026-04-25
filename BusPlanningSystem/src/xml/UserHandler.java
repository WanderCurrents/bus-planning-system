//  UserHandler Class
//----------------------
//Description: class designed for handling XML low-level operations for Users and the users.xml file. Extends XMLHandler class. Interacts with the UserManager when needed.
//Attributes: n/a
//Methods:	UserHandler()
//			addUserXML(idIndex : int, inUsername : String, inPassword : String, inIsAdmin : boolean) : void
//			removeUserXML(targetID : int) : boolean

package xml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class UserHandler extends XMLHandler 
{
	//Constructor
	public UserHandler() throws Exception
	{
		super("resources/users.xml");	//Sets the correct file path from the XMLHandler constructor
	}
	
	//Method that adds a user to the XML document/file
	protected void addUserXML(int idIndex, String inUsername, String inPassword, boolean inIsAdmin)
	{
		Element eNewUser = doc.createElement("user");
		eNewUser.setAttribute("id", String.valueOf(idIndex));
		Element username = doc.createElement("username");
		username.setTextContent(inUsername);
		Element password = doc.createElement("password");
		password.setTextContent(inPassword);
		Element isAdmin = doc.createElement("isAdmin");
		isAdmin.setTextContent(String.valueOf(inIsAdmin));
		
		//Add all the inputs to the element
		eNewUser.appendChild(username);
		eNewUser.appendChild(password);
		eNewUser.appendChild(isAdmin);
		
		//Add the element to the document
		doc.getDocumentElement().appendChild(eNewUser);
	}
	
	//Method that removes a station from the XML document/file
	protected boolean removeUserXML(int targetID)
	{
		NodeList nodes = doc.getElementsByTagName("user");	//Not creating a new NodeList, just a pointer
		
		for(int i=0; i<nodes.getLength(); i++)
		{
			Element user = (Element) nodes.item(i);
			if(Integer.parseInt(user.getAttribute("id")) == targetID)
			{
				doc.getDocumentElement().removeChild(user);
				return true;	//Return true if found and removed
			}
		}
		return false;	//Return false if not found
	}
}
