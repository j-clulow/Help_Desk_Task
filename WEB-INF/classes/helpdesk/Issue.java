/*
	Issue.java
	
	This class file pertains to an Issue
	object within the Help Desk software project
	for SENG2050 assignment 3, 2016.
	
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

package helpdesk;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;

public class Issue implements Serializable
{
	private String issueID;					// Will be unique in the database.
	private String userName;				// Owner of this issue.
	private String title;					// Issue title.
	private State state;					// Current state that the issue is in.
	private Category category;				// Category of the issue, will never be null.
	private SubCategory subCategory;		// SubCategory of the issue, can be null.
	private String description;				// Issue description.
	private String resolution;				// Issue resolution.
	private ArrayList<Comment> comments;	// The list of all comments that users have made on this issue.
	private String tags;					// A string containing comma separated tags.
	private Date dateTimeReported;			// Date object from the Unix Timestamp to comply with SQL Timestamp. 
	private Date dateTimeResolved;			// Date object from the Unix Timestamp to comply with SQL Timestamp. 
	private boolean isKnowledgeBaseArticle;	// Boolean that indicates whether the current issue is an article, not stored in database.
	private boolean isUserAnon;				// Boolean that indicates that the related 
	
	
	/************************
	*		CONSTRUCTOR		*
	*************************/
	public Issue()
	{
		userName = null;
		title = null;
		state = null;
		category = null;
		subCategory = null;
		description = null;
		resolution = null;
		comments = null;
		tags = null;
		dateTimeReported = null;
		dateTimeResolved = null;
		isKnowledgeBaseArticle = false;
		isUserAnon = false;
	}
	
	/********************
	*		GETTERS		*
	*********************/
	
	public String getIssueID()
	{
		return issueID;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	/*
	 * Returns the String representation for the State Enum required for printing.
	 */
	public String getState()
	{
		if(state == State.NEW){
			return "New";
		}
		else if(state == State.IN_PROGRESS){
			return "In Progress";
		}
		else if(state == State.COMPLETED){
			return "Completed";
		}
		else{
			return "Resolved";
		}
	}
	
	/*
	 * Returns the String representation for the Category Enum required for printing.
	 */
	public String getCategory()
	{
		if(category == Category.NETWORK){
			return "Network";
		}
		else if(category == Category.SOFTWARE){
			return "Software";
		}
		else if(category == Category.HARDWARE){
			return "Hardware";
		}
		else if(category == Category.EMAIL){
			return "Email";
		}
		else if(category == Category.ACCOUNT){
			return "Account";
		}
		else{
			return "None";
		}
	}

	/*
	 * Returns the String representation for the SubCategory Enum required for printing.
	 */
	public String getSubCategory()
	{
		if(subCategory == SubCategory.CANT_CONNECT){
			return "Can't Connect";
		}
		else if(subCategory == SubCategory.SPEED){
			return "Speed";
		}
		else if(subCategory == SubCategory.DROPOUTS){
			return "Dropouts";
		}
		else if(subCategory == SubCategory.LOAD_SLOW){
			return "Slow to load";
		}
		else if(subCategory == SubCategory.NO_LOAD){
			return "Won't load";
		}
		else if(subCategory == SubCategory.WONT_BOOT){
			return "Won't turn on";
		}
		else if(subCategory == SubCategory.BSOD){
			return "Blue Screen";
		}
		else if(subCategory == SubCategory.HDD){
			return "Disk Drive";
		}
		else if(subCategory == SubCategory.PERIPHERAL){
			return "Peripherals";
		}
		else if(subCategory == SubCategory.CANT_SEND){
			return "Can't Send";
		}
		else if(subCategory == SubCategory.CANT_RECEIVE){
			return "Can't receive";
		}
		else if(subCategory == SubCategory.SPAM){
			return "SPAM/Phishing";
		}
		else if(subCategory == SubCategory.RESET_PASSWORD){
			return "Reset password";
		}
		else if(subCategory == SubCategory.WRONG_DETAILS){
			return "Wrong details";
		}
		else{
			return "None";
		}
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getResolution()
	{
		return resolution;
	}
	
	public ArrayList<Comment> getComments()
	{
		return comments;
	}
	
	public String getTags()
	{
		return tags;
	}
	
	public Date getDateTimeReported()
	{
		return dateTimeReported;
	}
	
	public Date getDateTimeResolved()
	{
		return dateTimeResolved;
	}

	public boolean isKnowledgeBaseArticle(){
		return isKnowledgeBaseArticle;
	}

	public boolean isAnonymous()
	{
		return isUserAnon;
	}
	
	/********************
	*		SETTERS		*
	********************/
	
	public void setIssueID(String s)
	{
		issueID = s;
	}
	
	public void setUserName(String s)
	{
		userName = s;
	}
	
	public void setTitle(String s)
	{
		title = s;
	}
	
	public void setState(State s)
	{
		state = s;
	}
	
	public void setCategory(Category c)
	{
		category = c;
	}
	
	public void setSubCategory(SubCategory sc)
	{
		subCategory = sc;
	}
	
	public void setDescription(String s)
	{
		description = s;
	}
	
	public void setResolution(String s)
	{
		resolution = s;
	}
	
	public void setComments(ArrayList<Comment> arrayList)
	{
		comments = arrayList;
	}
	
	public void setTags(String s)
	{
		tags = s;
	}
	
	public void setDateTimeReported(Date d)
	{
		dateTimeReported = d;
	}
	
	public void setDateTimeResolved(Date d)
	{
		dateTimeResolved = d;
	}

	public void setKnowledgeBaseArticle()
	{
		isKnowledgeBaseArticle = true;
	}

	public void setAnonymous()
	{
		isUserAnon = true;
	}
}
