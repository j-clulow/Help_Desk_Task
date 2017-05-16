/*
	Issue.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
	ISSUE
		This Servlet retrieves an issue from the database
		adds it into an Issue object. This Issue object 
		is passed into the session so the web devs can
		use their JSP's to display it.
			
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

import java.sql.*;
import javax.sql.*;
import javax.naming.InitialContext;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;

@WebServlet(urlPatterns = {"/Issue"})
public class Issue extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		/* MAINTENANCE START */
		// get the ArrayList of maintenance issues
		ArrayList<String> maintList = getMaintenanceEntries(request);
		// Now add the populated ArrayList to the session object
		request.getSession().setAttribute("maintList", maintList);
		/* MAINTENANCE END */
		
		String requestIssueID = request.getParameter("id");
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		String dispatchPath = "/WEB-INF/jsp/Issue.jsp";
		ResultSet rsIssue = null;
		ResultSet rsComment = null;
		ResultSet rsSubComments = null;
		ResultSet rsTracking = null;
		ResultSet rsInKB = null;
		PreparedStatement stmntIssue = null;
		PreparedStatement stmntComments = null;
		PreparedStatement stmntSubComments = null;
		PreparedStatement stmntTracking = null;
		PreparedStatement stmntInKB = null;
		Connection connection = null;
		RequestDispatcher dispatcher = null;
		boolean trackedByThisUser = false;
		
		try
		{
			// Try to connect to the DB and get the issue the user asked for
			DataSource dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			connection = dataSource.getConnection();
			String myStatement = "SELECT * FROM help_desk_issues WHERE issue_id = ?";
			stmntIssue = connection.prepareStatement(myStatement);
			stmntIssue.setString(1,requestIssueID);
			rsIssue = stmntIssue.executeQuery();
			
			if (rsIssue.next())	// will return false if the SQL ResultSet was empty
			{
				// Now we check if user is indeed the owner of the issue they requested (don't want them seeing others' issues)
				boolean userOwnsTicket = rsIssue.getString("user_username").equals(currentUser.getUserName());
				boolean hasITAccess = currentUser.getRole().equals(helpdesk.Role.IT);
				if (!userOwnsTicket && !hasITAccess)
				{
					dispatchPath = "/Error?err=2";	// 2: Access Denied
					return;
				}
			}
			else	// there was nothing returned from the DB. Possibly because the user attempted to fudge the issueID within the URL.
			{
				dispatchPath = "/Error?err=3";	// 3: SQL Error
				return;
			}
			/* Okay, now we know that this issue's owner matches the person in the
			   request object (and therefore the person making the request) */
			
			
			/* Now grab all the Issue details and throw them in an Issue object
			   for the web devs to display it */
			helpdesk.Issue issue = new helpdesk.Issue();
			issue.setUserName(rsIssue.getString("user_username"));
			issue.setTitle(rsIssue.getString("issue_title"));
			issue.setIssueID(rsIssue.getString("issue_id"));
			
			// Issue can have different states. Find out which 
			// and set Issue object attribute accordingly
			switch(rsIssue.getInt("state_id"))
			{
				case 1:
					issue.setState(helpdesk.State.NEW);
					break;
				case 2:
					issue.setState(helpdesk.State.IN_PROGRESS);
					break;
				case 3:
					issue.setState(helpdesk.State.COMPLETED);
					issue.setResolution(rsIssue.getString("issue_resolution"));
					break;
				case 4:
					issue.setState(helpdesk.State.RESOLVED);
					issue.setResolution(rsIssue.getString("issue_resolution"));
					break;
			}
			
			// Issue can have different categories. Find out which 
			// and set Issue object attribute accordingly
			switch(rsIssue.getString("category_id"))
			{
				case "NETWORK":
					issue.setCategory(helpdesk.Category.NETWORK);
					break;
				case "SOFTWARE":
					issue.setCategory(helpdesk.Category.SOFTWARE);
					break;
				case "HARDWARE":
					issue.setCategory(helpdesk.Category.HARDWARE);
					break;
				case "EMAIL":
					issue.setCategory(helpdesk.Category.EMAIL);
					break;
				case "ACCOUNT":
					issue.setCategory(helpdesk.Category.ACCOUNT);
					break;
			}
			
			// Issue can have different sub-categories. Find out which 
			// and set Issue object attribute accordingly
			switch(rsIssue.getString("sub_category_id"))
			{
				case "CANT_CONNECT":
					issue.setSubCategory(helpdesk.SubCategory.CANT_CONNECT);
					break;
				case "SPEED":
					issue.setSubCategory(helpdesk.SubCategory.SPEED);
					break;
				case "DROPOUTS":
					issue.setSubCategory(helpdesk.SubCategory.DROPOUTS);
					break;
				case "LOAD_SLOW":
					issue.setSubCategory(helpdesk.SubCategory.LOAD_SLOW);
					break;
				case "NO_LOAD":
					issue.setSubCategory(helpdesk.SubCategory.NO_LOAD);
					break;
				case "WONT_BOOT":
					issue.setSubCategory(helpdesk.SubCategory.WONT_BOOT);
					break;
				case "BSOD":
					issue.setSubCategory(helpdesk.SubCategory.BSOD);
					break;
				case "HDD":
					issue.setSubCategory(helpdesk.SubCategory.HDD);
					break;
				case "PERIPHERAL":
					issue.setSubCategory(helpdesk.SubCategory.PERIPHERAL);
					break;
				case "CANT_SEND":
					issue.setSubCategory(helpdesk.SubCategory.CANT_SEND);
					break;
				case "CANT_RECEIVE":
					issue.setSubCategory(helpdesk.SubCategory.CANT_RECEIVE);
					break;
				case "SPAM":
					issue.setSubCategory(helpdesk.SubCategory.SPAM);
					break;
				case "RESET_PASSWORD":
					issue.setSubCategory(helpdesk.SubCategory.RESET_PASSWORD);
					break;
				case "WRONG_DETAILS":
					issue.setSubCategory(helpdesk.SubCategory.WRONG_DETAILS);
					break;
			}
			issue.setDescription(rsIssue.getString("issue_description"));
			issue.setTags(rsIssue.getString("issue_tags"));
			issue.setDateTimeReported(rsIssue.getTimestamp("issue_date_time_reported"));
			issue.setDateTimeResolved(rsIssue.getTimestamp("issue_date_time_resolved"));
			
			
			// Now populate the Issue object's comments (they're in an ArrayList<Comment> list)
			String statementComments = "SELECT * FROM help_desk_comments WHERE issue_id = ? AND parent_comment_id IS NULL ";
			stmntComments = connection.prepareStatement(statementComments);
			stmntComments.setString(1,requestIssueID);
			rsComment = stmntComments.executeQuery();
			ArrayList<helpdesk.Comment> issueComments = new ArrayList<helpdesk.Comment>();
			while(rsComment.next())
			{
				helpdesk.Comment comment = new helpdesk.Comment();
				comment.setCommentID(Integer.toString(rsComment.getInt("comment_id")));
				comment.setComment(rsComment.getString("comment_body"));
				comment.setUserName(rsComment.getString("user_username"));
				comment.setDateTime(rsComment.getTimestamp("comment_date_time"));
				
				// Get sub-comments and add them to the object
				String statementSubComments = "SELECT * FROM help_desk_comments WHERE parent_comment_id = ?";
				stmntSubComments = connection.prepareStatement(statementSubComments);
				stmntSubComments.setString(1,comment.getCommentID());
				rsSubComments = stmntSubComments.executeQuery();
				ArrayList<helpdesk.Comment> subcomments = new ArrayList<helpdesk.Comment>();
				while(rsSubComments.next())
				{
					helpdesk.Comment subcomment = new helpdesk.Comment();
					subcomment.setComment(rsSubComments.getString("comment_body"));
					subcomment.setUserName(rsSubComments.getString("user_username"));
					subcomment.setDateTime(rsSubComments.getTimestamp("comment_date_time"));
					subcomments.add(subcomment);
				}
				
				issueComments.add(comment);
				comment.setSubComments(subcomments);
			} // collection of comments complete
			
			// Add these comments into the Issue object
			issue.setComments(issueComments);
			
			// Now add Issue object into the session for the web devs
			request.getSession().setAttribute("issue", issue);
			
			// Check if user is currently tracking this ticket
			String statementTracking = "SELECT * FROM help_desk_watched_issues WHERE issue_id = ? AND user_username = ?";
			stmntTracking = connection.prepareStatement(statementTracking);
			stmntTracking.setString(1,requestIssueID);
			stmntTracking.setString(2,currentUser.getUserName());
			rsTracking = stmntTracking.executeQuery();
			if (rsTracking.next())
				trackedByThisUser = true;
			else
				trackedByThisUser = false;
			request.getSession().setAttribute("trackedByThisUser", trackedByThisUser);
			
			// Check if issue is also in KB
			boolean alreadyInKnowledgeBase = false;
			String statementInKB = "SELECT * FROM help_desk_knowledge_base WHERE knowledge_base_title = ? AND knowledge_base_date_time_reported = ? AND knowledge_base_date_time_resolved = ?";
			stmntInKB = connection.prepareStatement(statementInKB);
			stmntInKB.setString(1,rsIssue.getString("issue_title"));	// title
			stmntInKB.setString(2,rsIssue.getString("issue_date_time_reported"));	// time reported
			stmntInKB.setString(3,rsIssue.getString("issue_date_time_resolved"));	// time resolved
			rsInKB = stmntInKB.executeQuery();
			if (rsInKB.next())
				alreadyInKnowledgeBase = true;
			else
				alreadyInKnowledgeBase = false;
				
			// Now pass this into the session for the web devs
			request.getSession().setAttribute("alreadyInKnowledgeBase", alreadyInKnowledgeBase);
			
		}
		catch(Exception e)
		{
			dispatchPath = "/Error?code=4";	// error 4 = "Something went wrong" (generic error)
		}
		finally{	// try to close everything. We don't live in a tent now, do we?
			try{
				if(rsIssue != null){rsIssue.close();}
				if(rsComment != null){rsComment.close();}
				if(rsSubComments != null){rsSubComments.close();}
				if(rsTracking != null){rsTracking.close();}
				if(rsInKB != null){rsInKB.close();}
				if(stmntIssue != null){stmntIssue.close();}
				if(stmntComments != null){stmntComments.close();}
				if(stmntSubComments != null){stmntSubComments.close();}
				if(stmntTracking != null){stmntTracking.close();}
				if(stmntInKB != null){stmntInKB.close();}
				if(connection != null) connection.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			// Now let's get out of here :)
			try
			{
				dispatcher = getServletContext().getRequestDispatcher(dispatchPath);
				dispatcher.forward(request,response);
				return;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	
	/*
		This method retrieves all of the maintenance entries in the database
		and throws them in an ArrayList which is returned.
		This needs to happen on every servlet rather than once per session
		in case some other user adds maintenance at some point during this
		user's session, i.e., dynamic loading := retrieval on every page.
	*/
	private ArrayList<String> getMaintenanceEntries(HttpServletRequest request)
	{
		ArrayList<String> maintList = new ArrayList<>();
		
		// Try to connect to the DB and get the maintenance entries
		DataSource dataSource = null;
		Connection connection = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		try
		{
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			connection = dataSource.getConnection();
			String myStatement = "SELECT * FROM help_desk_maintenance";	// simple statement for this
			stmnt = connection.prepareStatement(myStatement);
			rs = stmnt.executeQuery();
		
			// Now populate the ArrayList with the maintenance strings from the db
			while (rs.next())	// loop through every maintenance entry
			{
				maintList.add(rs.getString("maintenance_message"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally{	// close everything.
			try{
				if(rs != null) rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
			try{
				if(stmnt != null) stmnt.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
			try{
				if(connection != null) connection.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return maintList;
	}
}
