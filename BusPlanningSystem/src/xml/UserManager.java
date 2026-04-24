package xml;

import model.Station;
import model.User;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class UserManager 
{
	private UserHandler handler;
	protected int idIndex;
	protected List<User> list = new ArrayList<>();
	private User currentUser;
	
	public UserManager() throws Exception
	{
		handler = new UserHandler();
		
		//Establish the list
		NodeList nodeBList = handler.doc.getElementsByTagName("user");
		for(int i=0; i<nodeBList.getLength(); i++)
		{
			Element e = (Element) nodeBList.item(i);
			list.add(convertElementToUser(e));
		}
		
		idIndex = getIDIndex();
	}
	
	protected int getIDIndex()
	{
		int maxID = -1;
		//Loop through the XML file and find the highest ID
		for(int i=0; i<list.size(); i++)
		{
			User checkUser = list.get(i);
			int listID = checkUser.getID();
			if(listID > maxID)
				maxID = listID;
		}
		return maxID+1;	//Returns the next available ID value that can be used
	}
	
	private User convertElementToUser(Element e)
	{
		int id = Integer.parseInt(e.getAttribute("id"));
		String username = e.getElementsByTagName("username").item(0).getTextContent();
		String password = e.getElementsByTagName("password").item(0).getTextContent();
		boolean isAdmin = Boolean.parseBoolean(e.getElementsByTagName("isAdmin").item(0).getTextContent());	//TODO: Make sure the cap case (isAdmin vs isadmin) is right
		
		return new User(id, username, password, isAdmin);
	}
	
	
	public void addUser(String inUsername, String inPassword, boolean inIsAdmin) throws Exception
	{
		//Create the new user addition
		User newUser = new User(idIndex, inUsername, inPassword, inIsAdmin);
		
		//Deal with DOM stuff
		handler.addUserXML(idIndex, inUsername, inPassword, inIsAdmin);
		
		//Update the list
		list.add(newUser);
		
		//Save changes to XML file
		handler.saveXML();
		
		idIndex++;	//Update the index counter
	}
	
	//Overloaded method for creating Users, passed in ID, no need to update ID, meant for user modification
	public void addUser(int ID, String inUsername, String inPassword, boolean inIsAdmin) throws Exception
	{
		//Create the new user addition
		User newUser = new User(ID, inUsername, inPassword, inIsAdmin);
		
		//Deal with DOM stuff
		handler.addUserXML(ID, inUsername, inPassword, inIsAdmin);
		
		//Update the list
		list.add(newUser);
		
		//Save changes to XML file
		handler.saveXML();
		
		//DO NOT UPDATE ID, NO NEED
	}
	
	public boolean removeUser(int targetID) throws Exception
	{
		for(int i=0; i<list.size(); i++)
		{
			if(list.get(i).getID() == targetID)
			{
				list.remove(i);		//Remove from the working list
				handler.removeUserXML(targetID);	//Remove from XML DOM
				handler.saveXML();	//Save XML to file
				return true;
			}
		}
		return false;	//Not found
	}
	
	//Returns the userID for a user based on username, only returns if exact match is found for security
	public int findUser(String username)
	{
		for(User u : list)
		{
			if(u.getUsername().equals(username))
			{
				return u.getID();
			}
		}
		return -99;	//Just a number to signal user not found
	}
	
	//Returns u
	public User getUser(int targetID)
	{
		for(User u : list)
		{
			if(u.getID() == targetID)
			{
				return u;
			}
		}
		return null;
	}
	
	//Sets the current user of the program, used during login process
	public void setCurrentUser(int userID)
	{
		currentUser = getUser(userID);
	}
	//Returns boolean value reflecting if the current user is of Admin status
	public boolean isCurrentUserAdmin()
	{
		if(currentUser == null)	//Cases where the currentUser isnt set, like upon first login
		{
			return false;	//Defaults to non-Admin
		}
		else
		{
			return currentUser.getIsAdmin();	//Otherwise return the admin status of currentUser
		}
	}
	//Returns the current user's username
	public String getCurrentUsername()
	{
		return currentUser.getUsername();
	}
	
	//Prints out a list of users, mainly for debug purposes
	public void printUserList()
	{
		for(User u : list)
		{
			System.out.println("ID: " + u.getID() + "\n\tUser: " + u.getUsername() + "\n\tPass: " + u.getPassword() + "\n\tisAdmin? " + u.getIsAdmin());
		}
	}
	
	//SubString Search for users and excludes current user
	public List<User> subStringSearch(String substring)
	{
		List<User> matches = new ArrayList<>();
		String lower = substring.toLowerCase();
		for (User u : list)
		{
			if(u.getID() == currentUser.getID())
			{
				continue;
			}
			if(lower.isBlank())	//If just enter is entered, this is blank, return all
			{
				matches.add(u);
				continue;
			}
			if(u.getUsername().toLowerCase().contains(substring))
			{
				matches.add(u);
			}
		}
		return matches;
	}
}

//"You only get to die for having lived. Most people who could ever exist, will never even be born." -Neil deGrasse Tyson