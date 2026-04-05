package model;

public class User 
{
	int id;
	String username;
	String password;
	boolean isAdmin;
	
	public User(int id, String username, String password, boolean isAdmin)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.isAdmin = isAdmin;
	}
	
	//Getters
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
