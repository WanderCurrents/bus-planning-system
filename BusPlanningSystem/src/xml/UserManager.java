//  UserManager Class
//----------------------
//Description: class designed for managing middle-level operations between the application classes and low-level operations of the XML handling. Maintains the active list of users.
//Attributes:	handler : BusHandler
//				idIndex : int
//				list : List<User>
//				currentUser : User
//Methods:	UserManager()
//			getIDIndex() : int
//			convertElementToUser(e Element)
//			addUser(inUsername : String, inPassword : String, inIsAdmin : boolean) : void
//			addUser(ID : int, inUsername : String, inPassword : String, inIsAdmin : boolean
//			removeUser(targetID : int) : boolean
//			findUser(username : String)
//			getUser(targetID : int) : User
//			setCurrentUser(userID : int) : void
//			isCurrentUserAdmin() : boolean
//			getCurrentUsername() : String
////		printUserList() : void
//			subStringSearch(substring : String) : List<User>

package xml;

import model.User;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class UserManager 
{
	private UserHandler handler;					//Respective handler
	protected int idIndex;							//Next available ID
	protected List<User> list = new ArrayList<>();	//Maintained list of Users managed by the manager
	private User currentUser;						//Current logged-in user, useful for tracking if the current is an admin and preventing the current user from operating on itself
	
	//Main constructor, creates the respective handler and sets in motion the XML database setup when a manager is created
	public UserManager() throws Exception
	{
		handler = new UserHandler();	//Create the handler
		
		//Establish the list
		NodeList nodeBList = handler.doc.getElementsByTagName("user");
		for(int i=0; i<nodeBList.getLength(); i++)
		{
			Element e = (Element) nodeBList.item(i);
			list.add(convertElementToUser(e));
		}
		
		idIndex = getIDIndex();	//Set the ID indx
	}
	
	//Finds the next available ID index and sets it
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
	
	//Converts DOM information into a User object
	private User convertElementToUser(Element e)
	{
		int id = Integer.parseInt(e.getAttribute("id"));
		String username = e.getElementsByTagName("username").item(0).getTextContent();
		String password = e.getElementsByTagName("password").item(0).getTextContent();
		boolean isAdmin = Boolean.parseBoolean(e.getElementsByTagName("isAdmin").item(0).getTextContent());	//TODO: Make sure the cap case (isAdmin vs isadmin) is right
		
		return new User(id, username, password, isAdmin);
	}
	
	//Primary method for creating a user
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
	
	//Primary method for removing a user
	public boolean removeUser(int targetID) throws Exception
	{
		for(int i=0; i<list.size(); i++)
		{
			if(list.get(i).getID() == targetID)
			{
				list.remove(i);		//Remove from the working list
				handler.removeUserXML(targetID);	//Remove from XML DOM
				handler.saveXML();	//Save XML to file
				return true;	//Returns true if user was found and removed
			}
		}
		return false;	//Returns false if the user was not found
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
	
	//Returns a user object based on the targetID
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
	
	//Debug method for printing list of users
//	public void printUserList()
//	{
//		for(User u : list)
//		{
//			System.out.println("ID: " + u.getID() + "\n\tUser: " + u.getUsername() + "\n\tPass: " + u.getPassword() + "\n\tisAdmin? " + u.getIsAdmin());
//		}
//	}
	
	//Method for substring search and returns a list of matches (excluding current user)
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

