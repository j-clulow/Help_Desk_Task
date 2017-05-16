/*
	ReportIssue.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
	REPORT ISSUE:
		This servlet handles the logic when a user adds
		an issue into the system.
	
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import javax.sql.*;
import javax.naming.InitialContext;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;

@WebServlet(urlPatterns = {"/ReportIssue"})
public class ReportIssue extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		/* MAINTENANCE START */
		// get the ArrayList of maintenance issues
		ArrayList<String> maintList = getMaintenanceEntries(request);
		// Now add the populated ArrayList to the session object
		request.getSession().setAttribute("maintList", maintList);
		/* MAINTENANCE END */
		
		/* FLOW CONTROL START */
		String redirectString = null;
		boolean flowControlRequired = false;
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		RequestDispatcher dispatcher = null;
		
		/* Three situations:
			1) User isn't logged in at all, they ought to goto Login page 
			2) User is of type USER and allowed to report an issue
			3) User is of type IT ADMIN and NOT allowed to report an issue  */
		if (currentUser == null)	// User isn't logged in at all. They should go to login page
		{
			redirectString = "/WEB-INF/jsp/Login.jsp";
		}
		else if (currentUser.getRole().equals(helpdesk.Role.USER)) // User is USER type. They get to report issues.
		{
			redirectString = "/WEB-INF/jsp/Report.jsp";
		}
		else	// User is IT type. They do NOT get to report issues.
		{
			redirectString = "/Error?err=2";
		}
		
		try{
			dispatcher = getServletContext().getRequestDispatcher(redirectString);
			dispatcher.forward(request,response);
			return;
		}
		catch(IOException e){
			e.printStackTrace();
		}
		/* FLOW CONTROL END */
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		/*	We first grab all the relevant information from the page,
			This information pertains to a brand new ticket, i.e., problem title,
			description, category, etc.
			
			Once we have the data, we INSERT INTO the sql database.
			
			Finally the user is taken to the ReportAck page which is a friendly
			confirmation message that their issue was indeed saved.
		*/
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		String redirectString = null;
		RequestDispatcher dispatcher = null;
		DataSource dataSource = null;
		Connection connection = null;
		PreparedStatement stmnt = null;
		boolean success = true;
		
		try
		{
			// grab all the page-data
			String empty = "";
			int state = 1;	// 1 = NEW
			String user = currentUser.getUserName();
			
			/* Below we have an array which will have either 1 or 2 entries.
				We split on hyphen in the following way: category-subcategory */
			String cats[] = request.getParameter("category").split("-");
			if(cats.length == 0 || cats[0] == "")
			{
				throw new Exception("No category was entered.");
			}
			String category = cats[0];
			String subcategory = "";
			if(cats.length > 1)
			{
				subcategory = cats[1];
			}
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String tags = request.getParameter("tags");
			String tagArr[] = tags.split(",");
			String tagString = ",";
			for(int i = 0; i < tagArr.length; i++)
			{
				tagString = tagString + (tagArr[i].toLowerCase().trim() + ",");
			}
			Date currentDateTime = new Date();
			java.sql.Timestamp slqTimestamp = new java.sql.Timestamp(currentDateTime.getTime());
			
			// now we have the data. Let's connect to db and INSERT it.
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			connection = dataSource.getConnection();
			String statement = "INSERT INTO help_desk_issues (state_id, user_username, category_id, sub_category_id, issue_title, issue_description, issue_tags, issue_date_time_reported, issue_anonymous_user)" +
				"VALUES (?,?,?,?,?,?,?,?,?)";
			stmnt = connection.prepareStatement(statement);
			stmnt.setInt(1,state);
			stmnt.setString(2,user);
			stmnt.setString(3,category);
			stmnt.setString(4,subcategory);
			stmnt.setString(5,title);
			stmnt.setString(6,description);
			stmnt.setString(7,tagString);
			stmnt.setTimestamp(8,slqTimestamp);
			
			/* Here we check if user wishes to remain anonymous should the 
				issue make it in the knowledge base. We do this because their
				username should not show if true */
			if(request.getParameter("anonymous") != null)
			{
				stmnt.setBoolean(9,true);
			}
			else
			{
				stmnt.setBoolean(9,false);
			}
			stmnt.executeUpdate();
		}
		catch(SQLException e)
		{
			success = false;
			redirectString = "/Error?err=3";
		}
		catch(Exception e)
		{
			success = false;
			redirectString = "/Error?err=4";
		}
		
		if (success)
			redirectString = "/WEB-INF/jsp/ReportAck.jsp";	// a friendly "your issue was saved", page
		
		try{
			stmnt.close();
			connection.close();
			dispatcher = getServletContext().getRequestDispatcher(redirectString);
			dispatcher.forward(request,response);
			return;
		}
		catch(Exception e){
			e.printStackTrace();
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
