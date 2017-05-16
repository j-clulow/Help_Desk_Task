/*
	UpdateIssueArticle.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
	UPDATE ISSUE ARTICLE:
		This servlet handles the logic for the following operations/cases:
			ISSUE:
				accept = User accepts a solution
				reject = User rejects a solution
				solutionIn = proposes a solution
				comment = User or IT add a comment (could be a comment-reply also)
				delete = IT deletes a comment
				add = IT adds an issue to the KB
			KNOWLEDGE BASE ARTICLE:
				comment = IT or Usser adds a comment to a kb article
				delete = IT delete a comment belonging to a kb article
			ISSUE TRACKING:
				IT associates/tracks themsevles to an open issue
	
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
import java.util.Date;

@WebServlet(urlPatterns = {"/Update"})
public class UpdateIssueArticle extends HttpServlet
{
	DataSource dataSource = null;
	Connection connection = null;
	PreparedStatement s = null;
	ResultSet rs = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		/* MAINTENANCE START */
		// get the ArrayList of maintenance issues
		ArrayList<String> maintList = getMaintenanceEntries(request);
		// Now add the populated ArrayList to the session object
		request.getSession().setAttribute("maintList", maintList);
		/* MAINTENANCE END */
		
		String destination = "/Error?code=4";
		
		/* IF UPDATING AN ISSUE
		********************/
		if(request.getParameter("updated").equals("issue"))
		{
			try{
				// Connect to db and prep vars for sl statement
				dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
				connection = dataSource.getConnection();
				String statement="";
				Timestamp date = null;
				int id;
				
				/* Now switch on what action to take.
				   Action cases:
					accept = User accepts a solution
					reject = User rejects a solution
					solutionIn = proposes a solution
					comment = User or IT add a comment (could be a comment-reply also)
					delete = IT deletes a comment
					add = IT adds an issue to the KB
				*/
				switch(request.getParameter("action"))
				{
					/* ACCEPT PROPOSED SOLUTION
					***************************/
					case "accept":	// User accepts a solution
						// We modify the table entry to reflect the time resolved
						id = Integer.parseInt(request.getParameter("issueID"));
						date = new Timestamp(new Date().getTime());
						statement = "UPDATE help_desk_issues SET state_id=4, issue_date_time_resolved=? WHERE issue_id=?";
						s = connection.prepareStatement(statement);
						s.setTimestamp(1,date);
						s.setInt(2,id);
						
						// actually update db
						s.executeUpdate();
						
						// This is the exit point. Exit back to the Issue view screen
						destination = "/Issue?id=" + Integer.toString(id);
						break;
						
					case "reject":	//User rejects a solution
						id = Integer.parseInt(request.getParameter("issueID"));
						statement = "UPDATE help_desk_issues SET state_id=2, issue_resolution=? WHERE issue_id=?";
						s = connection.prepareStatement(statement);
						s.setNull(1,Types.VARCHAR);
						s.setInt(2,id);
						s.executeUpdate();
						destination = "/Issue?id=" + Integer.toString(id);
						break;
					case "solutionIn":				// Propose Solution
						// We change the issue resolution to "completed" and also include the proposed resolution information
						id = Integer.parseInt(request.getParameter("issueID"));
						String solution = request.getParameter("solutionFromIT");
						
						// Issue ID and Reoslution info got from jsp
						statement = "UPDATE help_desk_issues SET issue_resolution=?, state_id=3 WHERE issue_id=?";
						s = connection.prepareStatement(statement);
						s.setString(1,solution);
						s.setInt(2,id);
						
						// actually update db
						s.executeUpdate();
						destination = "/Issue?id=" + Integer.toString(id);
						break;
					case "comment":	//User or IT add a comment (could be a comment-reply also)
						// We add the comment into the comment table
						id = Integer.parseInt(request.getParameter("issueID"));
						String comment = request.getParameter("comment");
						date = new Timestamp(new Date().getTime());
						int parent = Integer.parseInt(request.getParameter("parent"));
						
						// IS A SUB-COMMENT OR NOT?
						if(parent == -1)	// NOT a reply/subcomment
						{
							statement = "INSERT INTO help_desk_comments (issue_id,user_username,comment_body,comment_date_time)"
												+ "VALUES (?,?,?,?)";
							s = connection.prepareStatement(statement);
							s.setInt(1,id);
							s.setString(2,((helpdesk.User)request.getSession().getAttribute("user")).getUserName());
							s.setString(3,comment);
							s.setTimestamp(4,date);
							s.executeUpdate();
						}
						else	// comment IS a reply/subcomment
						{
							statement = "INSERT INTO help_desk_comments (parent_comment_id,issue_id,user_username,comment_body,comment_date_time)"
												+ "VALUES (?,?,?,?,?)";
							s = connection.prepareStatement(statement);
							s.setInt(1,parent);
							s.setInt(2,id);
							s.setString(3,((helpdesk.User)request.getSession().getAttribute("user")).getUserName());
							s.setString(4,comment);
							s.setTimestamp(5,date);
							s.executeUpdate();
						}
						destination = "/Issue?id=" + Integer.toString(id);
						break;
					case "delete":	//IT deletes a comment
						
						int commentID = Integer.parseInt(request.getParameter("commentID"));
						statement = "DELETE FROM help_desk_comments WHERE comment_id=? OR parent_comment_id=?";
						s = connection.prepareStatement(statement);
						s.setInt(1,commentID);
						s.setInt(2,commentID);
						s.executeUpdate();
						destination = "/Issue?id=" + request.getParameter("issueID");
						break;
					case "add":		// IT adds an issue to the KB
						
						/* ADDING ISSUE OVER TO KB
						**************************/
						
						// first retrieve the issue from the SQL database
						id = Integer.parseInt(request.getParameter("issueID"));	//used for SQL statement
						PreparedStatement getIssue = connection.prepareStatement("SELECT * FROM help_desk_issues WHERE issue_id=?");
						getIssue.setInt(1,id);
						ResultSet issueRS = getIssue.executeQuery();
						// Issue retieved and inside ResultSet rs
						
						//Now insert into KB table in db
						issueRS.next();
						statement = "INSERT INTO help_desk_knowledge_base (user_username, state_id, category_id, sub_category_id, knowledge_base_title, " +
											"knowledge_base_description, knowledge_base_resolution, knowledge_base_tags, knowledge_base_date_time_reported, knowledge_base_date_time_resolved) " +
											"VALUES (?,?,?,?,?,?,?,?,?,?)";
						s = connection.prepareStatement(statement);
						
						// We change the username of the KB article if the user has requested so
						if(issueRS.getBoolean("issue_anonymous_user"))
							s.setString(1,"Anonymous");
						else
							s.setString(1,issueRS.getString("user_username"));
						
						s.setInt(2,issueRS.getInt("state_id"));
						s.setString(3,issueRS.getString("category_id"));
						if(issueRS.getString("sub_category_id") != null)
							s.setString(4,issueRS.getString("sub_category_id"));
						else
							s.setNull(4,Types.VARCHAR);
						s.setString(5,issueRS.getString("issue_title"));
						s.setString(6,issueRS.getString("issue_description"));
						s.setString(7,issueRS.getString("issue_resolution"));
						s.setString(8,issueRS.getString("issue_tags"));
						s.setTimestamp(9,issueRS.getTimestamp("issue_date_time_reported"));
						s.setTimestamp(10,issueRS.getTimestamp("issue_date_time_resolved"));
						// Statement ready. Now actually insert into DB:
						s.executeUpdate();
						
						
						/* ADDING COMMENTS OVER TO KB
						******************************/
						
						// To do this we must update the comments with the kb article ID.
						// To do that we must first retieve the id of the kb article just commited to the kb table
						s = connection.prepareStatement("SELECT knowledge_base_id FROM help_desk_knowledge_base WHERE knowledge_base_title=? AND knowledge_base_description=? " +
															"AND knowledge_base_date_time_reported=? AND knowledge_base_date_time_resolved=?");
						s.setString(1,issueRS.getString("issue_title"));
						s.setString(2,issueRS.getString("issue_description"));
						s.setTimestamp(3,issueRS.getTimestamp("issue_date_time_reported"));
						s.setTimestamp(4,issueRS.getTimestamp("issue_date_time_resolved"));
						rs = s.executeQuery();
						rs.next();
						
						// here we grab the kb ID
						int knowledge_base_id = Integer.parseInt(rs.getString("knowledge_base_id"));
						
						// now we have the ID, we update the appropriate comments to include it
						s = connection.prepareStatement("UPDATE help_desk_comments SET knowledge_base_id=? AND issue_id=? WHERE issue_id=?");
						s.setInt(1,knowledge_base_id);
						s.setNull(2,Types.INTEGER);
						s.setInt(3,issueRS.getInt("issue_id"));
						s.executeUpdate();
						issueRS.close();
						getIssue.close();
						
						/* DELETING THE ISSUE AS IT'S NOW A KB ARTICLE
						**********************************************/
						// First remove from Issues table:
						s = connection.prepareStatement("DELETE FROM help_desk_issues WHERE issue_id=?");
						s.setInt(1,id);	// id already set above, it's still the Issue_ID
						s.executeUpdate();	// done! Issue deleted.
						// And remove from TrackedIssues table
						s = connection.prepareStatement("DELETE FROM help_desk_watched_issues WHERE issue_id=?");
						s.setInt(1,id);	// id already set above, it's still the Issue_ID
						s.executeUpdate();	// done! Issue deleted.
						
						// This is the exit point. Exit to ViewAllIssues
						destination = "/ViewAllIssues";
						break;
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				destination = "/Error?err=3";
			}
			catch(Exception e)
			{
				e.printStackTrace();
				destination = "/Error?err=4";
			}
		}
		
		/* ELSE UPDATING A KB ARTICLE
		*****************************/
		else if(request.getParameter("updated").equals("article"))
		{
			try{
				dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
				connection = dataSource.getConnection();
				String statement="";
				Timestamp date = null;
				int id;
				switch(request.getParameter("action"))
				{
					case "comment":	// Adding a comment to a KB article
						id = Integer.parseInt(request.getParameter("issueID"));
						String comment = request.getParameter("comment");
						date = new Timestamp(new Date().getTime());
						int parent = Integer.parseInt(request.getParameter("parent"));
						
						// IS A SUB-COMMENT OR NOT?
						if(parent == -1)	// NOT a reply/subcomment
						{
							statement = "INSERT INTO help_desk_comments (knowledge_base_id,user_username,comment_body,comment_date_time)"
												+ "VALUES (?,?,?,?)";
							s = connection.prepareStatement(statement);
							s.setInt(1,id);
							s.setString(2,((helpdesk.User)request.getSession().getAttribute("user")).getUserName());
							s.setString(3,comment);
							s.setTimestamp(4,date);
							s.executeUpdate();
						}
						else	// IS a reply/subcomment
						{
							// The difference here from a non-subcomment is that the
							// parent_id field is populated
							statement = "INSERT INTO help_desk_comments (parent_comment_id,knowledge_base_id,user_username,comment_body,comment_date_time)"
												+ "VALUES (?,?,?,?,?)";
							s = connection.prepareStatement(statement);
							s.setInt(1,parent);
							s.setInt(2,id);
							s.setString(3,((helpdesk.User)request.getSession().getAttribute("user")).getUserName());
							s.setString(4,comment);
							s.setTimestamp(5,date);
							s.executeUpdate();
						}
						
						// exit back to viewing the kb article
						destination = "/Article?id=" + Integer.toString(id);
						break;
					case "delete":	// IT deleted a KB comment
						int commentID = Integer.parseInt(request.getParameter("commentID"));
						statement = "DELETE FROM help_desk_comments WHERE comment_id=? OR parent_comment_id=?";
						s = connection.prepareStatement(statement);
						s.setInt(1,commentID);
						s.setInt(1,commentID);
						s.executeUpdate();
						
						// Comment deleted, back to viewing the article
						destination = "/Article?id=" + request.getParameter("issueID");
						break;
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				destination = "/Error?err=3";
			}
			catch(Exception e)
			{
				e.printStackTrace();
				destination = "/Error?err=4";
			}
		}
		
		/* ELSE IT-USER TRACKS/ASSOCIATES THEMSELVES
			WITH AN ISSUE
		*******************************************/
		else
		{
			try{
				// Try getting the issue_id and the username and adding them to the help_desk_watched_issues
				// table. This is how tracked issues are stored.
				dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
				connection = dataSource.getConnection();
				if(request.getParameter("action").equals("track"))
				{
					int id = Integer.parseInt(request.getParameter("issueID"));
					String userName = ((helpdesk.User)request.getSession().getAttribute("user")).getUserName();
					String statement = "INSERT INTO help_desk_watched_issues (user_username, issue_id) VALUES (?,?)";
					s = connection.prepareStatement(statement);
					s.setString(1,userName);
					s.setInt(2,id);
					s.executeUpdate();
					destination = "/Issue?id=" + Integer.toString(id);
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				destination = "/Error?err=3";
			}
			catch(Exception e)
			{
				e.printStackTrace();
				destination = "/Error?err=4";
			}
		}
		try{ // to close everyting.
			// We don't live in a tent now, do we?
			if(rs != null) rs.close();
			if(s != null) s.close();
			if(connection != null) connection.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try{
			response.sendRedirect(("."+destination));
			return;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return;
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
