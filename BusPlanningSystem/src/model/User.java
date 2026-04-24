//    User Class
//----------------------
//Description: model class that is instantiated to represent a user object
//Attributes:	id : int
//				username : String
//				password : String
//				isAdmin : boolean
//Methods: 	User(id : int, username : String, password : String, isAdmin : boolean)
//			getID() : int
//			getUsername() : String
//			getPassword() : String
//			getIsAdmin() : boolean

package model;


public class User 
{
	int id;				//User's ID number in the XML database
	String username;	//User's username expressed as a String
	String password;	//User's plaintext password expressed as a String
	boolean isAdmin;	//Boolean flag that determines if the user has admin privileges
	
	//Main constructor
	public User(int id, String username, String password, boolean isAdmin)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.isAdmin = isAdmin;
	}
	
	//----------------------GETTERS----------------------
	public int getID()
	{
		return id;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public boolean getIsAdmin()
	{
		return isAdmin;
	}
}
