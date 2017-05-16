/*
	User.java
	
	This class file pertains to a User
	object within the Help Desk software project
	for SENG2050 assignment 3, 2016.
	
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

package helpdesk;

import java.util.ArrayList;
import java.io.Serializable;

public class User implements Serializable
{
	// user/pass:
	private String userName;				// The username for the user in the database.
	private String password;				// The password for the user in the database.
	
	// attributes meta:
	private String firstName;				// The first name of the user. Not used in the application, but stored in the system.
	private String lastName;				// The last name of the user. Not used in the application, but stored in the system.
	private String emailAddress;			// The email address of the user. Not used in the application, but stored in the system.
	private String phoneNumber;				// The phone number of the user. Not used in the application, but stored in the system.
	
	// attributes other:
	private Role role;						// The role that the user has in the system, controls access to resources.
	private ArrayList<Issue> activeIssues;	// The list of active issues that the user is associated with.
	
	
	/*********************
	*		CONSTRUCTOR		*
	*********************/
	public User()
	{
		userName = null;
		password = null;
		firstName = null;
		lastName = null;
		emailAddress = null;
		phoneNumber = null;
		role = null;
		activeIssues = null;
	}
	
	
	/******************
	*		GETTERS		*
	*******************/
	public String getUserName()
	{
		return userName;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public String getEmailAddress()
	{
		return emailAddress;
	}
	
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	
	public Role getRole()
	{
		return role;
	}
	
	public ArrayList<Issue> getActiveIssues()
	{
		return activeIssues;
	}
	
	public boolean isIT()
	{
		if(role == Role.IT){
			return true;
		}
		else{
			return false;
		}
	}
	
	/********************
	*		SETTERS		*
	*********************/
	public void setUserName(String s)
	{
		userName = s;
	}
	
	public void setPassword(String s)
	{
		password = s;
	}
	
	public void setFirstName(String s)
	{
		firstName = s;
	}
	
	public void setLastName(String s)
	{
		lastName = s;
	}
	
	public void setEmailAddress(String s)
	{
		emailAddress = s;
	}
	
	public void setPhoneNumber(String s)
	{
		phoneNumber = s;
	}
	
	public void setRole(Role r)
	{
		role = r;
	}
	
	public void setActiveIssues(ArrayList<Issue> arrayList)
	{
		activeIssues = arrayList;
	}
	
}
