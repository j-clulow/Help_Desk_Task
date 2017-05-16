/*
	KnowledgeBaseArticle.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
	KNOWLEDGE BASE ARTICLE
		This servlet handles the retrieval of a single
		Knowledge Base Article form the db, copies
		all of its information into an Issue object 
		which itself gets added to the session as an
		atribute for later delivery via a jsp
	
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

@WebServlet(urlPatterns = {"/Article"})
public class KnowledgeBaseArticle extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		/* MAINTENANCE START */
		// get the ArrayList of maintenance issues
		ArrayList<String> maintList = getMaintenanceEntries(request);
		// Now add the populated ArrayList to the session object
		request.getSession().setAttribute("maintList", maintList);
		/* MAINTENANCE END */
		
		// Flow control START
		String redirectString = null;
		RequestDispatcher dispatcher = null;
		boolean flowControlRequired = false;
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		
		/* Setup which screen we ought to go to next. The
			user went directly to this servlet via the URL
			bar and therefore needs to be directed to one of
			three different possible pages:
		
		Two situations:
			1)	User isn't logged in at all, they ought to goto Login page
			2)	User is logged in and ought to goto KnowledgeBase
		*/
		
		if (currentUser == null)
		{
			flowControlRequired = true;
			redirectString = "/WEB-INF/jsp/Login.jsp";
		}
		else if (request.getParameter("id") == null) // logged in and no articleID (eg: there's no /Article?id=2)
		{
			flowControlRequired = true;
			redirectString = "/WEB-INF/jsp/KnowledgeBase.jsp";
		}
		else
		{
			flowControlRequired = false;
		}
		
		// Now go to the next screen
		if (flowControlRequired)
		{
			dispatcher = getServletContext().getRequestDispatcher(redirectString);
			try{
				dispatcher.forward(request,response);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		} // FLOW CONTROL END
		
		
		DataSource dataSource = null;
		Connection connection = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		PreparedStatement s = null;
		PreparedStatement s2 = null;
		String id = request.getParameter("id");
		String destination = "";
		
		try{	// connecting to the db
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			connection = dataSource.getConnection();
			String statement = "SELECT * FROM help_desk_knowledge_base WHERE knowledge_base_id = ?";
			s = connection.prepareStatement(statement);
			s.setString(1,id);
			rs = s.executeQuery();
			rs.next();	// move to the actual result (ResultSet starts before the first result)
						
			helpdesk.Issue article = new helpdesk.Issue();	// this will be the Issue object that gets stored as a session attribute
			article.setUserName(rs.getString("user_username"));
			article.setTitle(rs.getString("knowledge_base_title"));
			
			// Get category
			switch(rs.getString("category_id"))
			{
				case "NETWORK":
					article.setCategory(helpdesk.Category.NETWORK);
					break;
				case "SOFTWARE":
					article.setCategory(helpdesk.Category.SOFTWARE);
					break;
				case "HARDWARE":
					article.setCategory(helpdesk.Category.HARDWARE);
					break;
				case "EMAIL":
					article.setCategory(helpdesk.Category.EMAIL);
					break;
				case "ACCOUNT":
					article.setCategory(helpdesk.Category.ACCOUNT);
					break;
			}
			
			// Get sub-category
			switch(rs.getString("sub_category_id"))
			{
				case "BSOD":
					article.setSubCategory(helpdesk.SubCategory.BSOD);
					break;
				case "CANT_CONNECT":
					article.setSubCategory(helpdesk.SubCategory.CANT_CONNECT);
					break;
				case "CANT_RECIEVE":
					article.setSubCategory(helpdesk.SubCategory.CANT_RECEIVE);
					break;
				case "CANT_SEND":
					article.setSubCategory(helpdesk.SubCategory.CANT_SEND);
					break;
				case "DROPOUTS":
					article.setSubCategory(helpdesk.SubCategory.DROPOUTS);
					break;
				case "HDD":
					article.setSubCategory(helpdesk.SubCategory.HDD);
					break;
				case "LOAD_SLOW":
					article.setSubCategory(helpdesk.SubCategory.LOAD_SLOW);
					break;
				case "PERIPHERAL":
					article.setSubCategory(helpdesk.SubCategory.PERIPHERAL);
					break;
				case "RESET_PASSWORD":
					article.setSubCategory(helpdesk.SubCategory.RESET_PASSWORD);
					break;
				case "SPAM":
					article.setSubCategory(helpdesk.SubCategory.SPAM);
					break;
				case "SPEED":
					article.setSubCategory(helpdesk.SubCategory.SPEED);
					break;
				case "WONT_BOOT":
					article.setSubCategory(helpdesk.SubCategory.WONT_BOOT);
					break;
				case "WRONG_DETAILS":
					article.setSubCategory(helpdesk.SubCategory.WRONG_DETAILS);
					break;
				default:
					article.setSubCategory(null);
					break;
			}
			
			// Now set attributes
			article.setDescription(rs.getString("knowledge_base_description"));
			article.setResolution(rs.getString("knowledge_base_resolution"));
			article.setTags(rs.getString("knowledge_base_tags"));
			article.setDateTimeReported(rs.getTimestamp("knowledge_base_date_time_reported"));
			article.setDateTimeResolved(rs.getTimestamp("knowledge_base_date_time_resolved"));
			article.setKnowledgeBaseArticle();
			statement = "SELECT * FROM help_desk_comments WHERE knowledge_base_id = ? AND parent_comment_id IS NULL";
			s = connection.prepareStatement(statement);
			s.setString(1,id);
			rs = s.executeQuery();
			ArrayList<helpdesk.Comment> articleComments = new ArrayList<>();
			while(rs.next())
			{
				String commID = rs.getString("comment_id");
				helpdesk.Comment comment = new helpdesk.Comment();
				comment.setCommentID(commID);
				comment.setComment(rs.getString("comment_body"));
				comment.setUserName(rs.getString("user_username"));
				comment.setDateTime(rs.getTimestamp("comment_date_time"));
				String statement2 = "SELECT * FROM help_desk_comments WHERE parent_comment_id = ?";
				s2 = connection.prepareStatement(statement2);
				s2.setString(1,commID);
				rs2 = s2.executeQuery();
				ArrayList<helpdesk.Comment> subcomments = new ArrayList<>();
				while(rs2.next())
				{
					helpdesk.Comment subcomment = new helpdesk.Comment();
					subcomment.setComment(rs2.getString("comment_body"));
					subcomment.setUserName(rs2.getString("user_username"));
					subcomment.setDateTime(rs2.getTimestamp("comment_date_time"));
					subcomments.add(subcomment);
				}
				comment.setSubComments(subcomments);
				articleComments.add(comment);
				rs2.close();
				s2.close();
			}
			article.setComments(articleComments);
			
			// Now add to the session
			request.getSession().setAttribute("issue",article);
			
			// And move on
			destination = "/WEB-INF/jsp/Issue.jsp";
		}
		catch(SQLException e)
		{
			destination = "Error?err=3";																				
			e.printStackTrace();
		}
		catch(Exception e)
		{
			destination = "Error?err=4";
		}
		finally{
			try{
				if(rs != null) rs.close();
				if(rs2 != null) rs2.close();
				if(s != null) s.close();
				if(s2 != null) s2.close();
				if(connection != null) connection.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			try{
			dispatcher = getServletContext().getRequestDispatcher(destination);
			dispatcher.forward(request,response);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return;
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

