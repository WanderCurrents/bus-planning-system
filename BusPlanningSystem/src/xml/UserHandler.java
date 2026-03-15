//XML Handler for the Users
//---------------------------------------------------------------
//
//Methods
//	getList() = returns the list version (of elements) of the document

package xml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class UserHandler extends XMLHandler 
{
	
	public UserHandler() throws Exception
	{
		super("resources/users.xml");
	}
	
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
	
	protected boolean removeUserXML(int targetID)
	{
		NodeList nodes = doc.getElementsByTagName("user");	//Not creating a new NodeList, just a pointer
		
		for(int i=0; i<nodes.getLength(); i++)
		{
			Element user = (Element) nodes.item(i);
			if(Integer.parseInt(user.getAttribute("id")) == targetID)
			{
				doc.getDocumentElement().removeChild(user);
				return true;
			}
		}
		
		return false;
	}
}
