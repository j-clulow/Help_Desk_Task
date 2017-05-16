/*
	Comment.java
	
	This class file pertains to a comment
	object within the Help Desk software project
	for SENG2050 assignment 3, 2016.
	
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

package helpdesk;

import java.util.Date;
import java.util.ArrayList;
import java.io.Serializable;

public class Comment implements Serializable
{
	private String userName;				// Name of the user that made the comment.
	private String comment;					// The body of the comment.
	private String commentID;				// The unique ID of the comment for use in the database.
	private String parentCommentID;			// The ID of the parent comment if the comment is a subcomment.
	private String issueID;					// The ID of the issue that this is comment on.
	private Date dateTime;					// The Unix Timestamp that stores the time that the comment was made.
	private ArrayList<Comment> subComments;	// Any subcomments that this comment 
	
	/************************
	*		CONSTRUCTOR		*
	************************/
	public Comment()
	{
		userName = null;
		comment = null;
		commentID = null;
		parentCommentID = null;
		issueID = null;
		dateTime = null;
		subComments = null;
	}
	
	/********************
	*		GETTERS		*
	*********************/
	public String getUserName()
	{
		return userName;
	}
	
	public String getComment()
	{
		return comment;
	}
	
	public String getCommentID()
	{
		return commentID;
	}
	
	public String getParentCommentID()
	{
		return parentCommentID;
	}
	
	public String getIssueID()
	{
		return issueID;
	}
	
	public Date getDateTime()
	{
		return dateTime;
	}
	
	public ArrayList<Comment> getSubComments()
	{
		return subComments;
	}
	
	/********************
	*		SETTERS		*
	********************/
	public void setUserName(String s)
	{
		userName = s;
	}
	
	public void setComment(String s)
	{
		comment = s;
	}
	
	public void setCommentID(String s)
	{
		commentID = s;
	}
	
	public void setParentCommentID(String s)
	{
		parentCommentID = s;
	}
	
	public void setIssueID(String s)
	{
		issueID = s;
	}
	
	public void setDateTime(Date d)
	{
		dateTime = d;
	}
	
	public void setSubComments(ArrayList<Comment> arrayList)
	{
		subComments = arrayList;
	}
}
