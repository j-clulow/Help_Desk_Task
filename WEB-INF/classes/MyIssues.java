/*
	MyIssues.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
	MY ISSUES
		This servlet handles the logic behind
		two main tasks; getting the list of Issues
		belonging to a regular User, and getting the
		list of Issues currently tracked by an IT Staff member.
		
		The Issues are thrown into an ArrayList and added
		to the session object as an attribute for later
		deliery via a JSP.
	
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

@WebServlet(urlPatterns = {"/MyIssues"})
public class MyIssues extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		/* MAINTENANCE START */
		// get the ArrayList of maintenance issues
		ArrayList<String> maintList = getMaintenanceEntries(request);
		// Now add the populated ArrayList to the session object
		request.getSession().setAttribute("maintList", maintList);
		/* MAINTENANCE END */
		
		DataSource dataSource = null;
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement s = null;
		RequestDispatcher dispatcher = null;
		
		/* FLOW CONTROL START */
		String redirectString = null;
		boolean flowControlRequired = false;
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		
		/* One situation:
			1)	User isn't logged in at all, they ought to goto Login page */
		if (currentUser == null)
		{
			redirectString = "/WEB-INF/jsp/Login.jsp";
			dispatcher = getServletContext().getRequestDispatcher(redirectString);
			try{
				dispatcher.forward(request,response);
				return;
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		/* FLOW CONTROL END */
		
		
		try	// to connect to DB
		{
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			connection = dataSource.getConnection();
			String statement = "";
			
			// IF USER = list of their own issues
			if(((helpdesk.User)request.getSession().getAttribute("user")).getRole().equals(helpdesk.Role.USER))
			{
				statement = "SELECT * FROM help_desk_issues WHERE user_username = ? ORDER BY issue_date_time_reported ASC";
			}
			
			// ELSE IF IT STAFF = list of tracked issues
			else
			{
				statement = "SELECT * FROM help_desk_issues LEFT JOIN help_desk_watched_issues ON help_desk_watched_issues.issue_id"+
								"=help_desk_issues.issue_id WHERE help_desk_watched_issues.user_username = ? ORDER BY issue_date_time_reported ASC";	
			}
			s = connection.prepareStatement(statement);
			s.setString(1,currentUser.getUserName());
			rs = s.executeQuery();
			ArrayList<helpdesk.Issue> myIssues = new ArrayList<>();
			
			/* Got db results, now loop through
				adding their column	values to an
				Issue object and throwing them into
				an ArrayList */
			while(rs.next())
			{
				helpdesk.Issue currIssue = new helpdesk.Issue();
				currIssue.setTitle(rs.getString("issue_title"));
				currIssue.setUserName(rs.getString("user_username"));
				currIssue.setDescription(rs.getString("issue_description"));
				currIssue.setIssueID(rs.getString("issue_id"));
				currIssue.setDateTimeReported(rs.getTimestamp("issue_date_time_reported"));
				currIssue.setDateTimeResolved(rs.getTimestamp("issue_date_time_resolved"));

				switch(rs.getString("category_id"))
				{
					case "NETWORK":
						 currIssue.setCategory(helpdesk.Category.NETWORK);
						break;
					case "SOFTWARE":
						 currIssue.setCategory(helpdesk.Category.SOFTWARE);
						break;
					case "HARDWARE":
						 currIssue.setCategory(helpdesk.Category.HARDWARE);
						break;
					case "EMAIL":
						 currIssue.setCategory(helpdesk.Category.EMAIL);
						break;
					case "ACCOUNT":
						 currIssue.setCategory(helpdesk.Category.ACCOUNT);
						break;
				}

				switch(rs.getInt("state_id"))
				{
					case 1:
						currIssue.setState(helpdesk.State.NEW);
						break;
					case 2:
						currIssue.setState(helpdesk.State.IN_PROGRESS);
						break;
					case 3:
						currIssue.setState(helpdesk.State.COMPLETED);
						break;
					case 4:
						currIssue.setState(helpdesk.State.RESOLVED);
						break;
				}

				switch(rs.getString("sub_category_id"))
				{
					case "CANT_CONNECT":
						 currIssue.setSubCategory(helpdesk.SubCategory.CANT_CONNECT);
						break;
					case "SPEED":
						 currIssue.setSubCategory(helpdesk.SubCategory.SPEED);
						break;
					case "DROPOUTS":
						 currIssue.setSubCategory(helpdesk.SubCategory.DROPOUTS);
						break;
					case "LOAD_SLOW":
						 currIssue.setSubCategory(helpdesk.SubCategory.LOAD_SLOW);
						break;
					case "NO_LOAD":
						 currIssue.setSubCategory(helpdesk.SubCategory.NO_LOAD);
						break;
					case "WONT_BOOT":
						 currIssue.setSubCategory(helpdesk.SubCategory.WONT_BOOT);
						break;
					case "BSOD":
						 currIssue.setSubCategory(helpdesk.SubCategory.BSOD);
						break;
					case "HDD":
						 currIssue.setSubCategory(helpdesk.SubCategory.HDD);
						break;
					case "PERIPHERAL":
						 currIssue.setSubCategory(helpdesk.SubCategory.PERIPHERAL);
						break;
					case "CANT_SEND":
						 currIssue.setSubCategory(helpdesk.SubCategory.CANT_SEND);
						break;
					case "CANT_RECEIVE":
						 currIssue.setSubCategory(helpdesk.SubCategory.CANT_RECEIVE);
						break;
					case "SPAM":
						 currIssue.setSubCategory(helpdesk.SubCategory.SPAM);
						break;
					case "RESET_PASSWORD":
						 currIssue.setSubCategory(helpdesk.SubCategory.RESET_PASSWORD);
						break;
					case "WRONG_DETAILS":
						 currIssue.setSubCategory(helpdesk.SubCategory.WRONG_DETAILS);
						break;
				}
				
				currIssue.setTags(rs.getString("issue_tags"));
				myIssues.add(currIssue);
			}
			
			// Throw the array list into the actual session object.
			request.getSession().setAttribute("issuesArrayList",myIssues);
			rs.close();
			connection.close();
			s.close();
			dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/MyIssues.jsp");
			dispatcher.forward(request,response);
			return;
		}
		catch(Exception e)
		{
			dispatcher = getServletContext().getRequestDispatcher("/Error?err=3");
			e.printStackTrace();
		}
		
		try
		{
			rs.close();
			s.close();
			connection.close();
			dispatcher.forward(request, response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		/* MAINTENANCE START */
		// get the ArrayList of maintenance issues
		ArrayList<String> maintList = getMaintenanceEntries(request);
		// Now add the populated ArrayList to the session object
		request.getSession().setAttribute("maintList", maintList);
		/* MAINTENANCE END */
		
		DataSource dataSource = null;
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement s = null;
		RequestDispatcher dispatcher = null;
		try
		{
			String sortBy = null;
			sortBy = request.getParameter("sortBy");
			switch(request.getParameter("sortBy"))
			{
				case "date":
					sortBy = "issue_date_time_reported";
					break;
				case "status":
					sortBy = "state_id";
					break;
			}
			boolean isAscending = (Integer.parseInt(request.getParameter("order")) == 1); // 1 = ASC, 0 = DESC
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			connection = dataSource.getConnection();	
			String statement;
			
			
			// IF USER = Get user's own issues
			if(((helpdesk.User)request.getSession().getAttribute("user")).getRole().equals(helpdesk.Role.USER))
			{
				if(isAscending)
				{
					statement = "SELECT * FROM help_desk_issues WHERE user_username = ? ORDER BY  " + sortBy + " ASC";			
				}
				else
				{
					statement = "SELECT * FROM help_desk_issues WHERE user_username = ? ORDER BY  " + sortBy + " DESC";
				}
				s = connection.prepareStatement(statement);
				helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
				s.setString(1,currentUser.getUserName());
			}
			else	// IF IT STAFF = Get issues tracked by this IT staffer.
			{
				if(isAscending)
				{
					statement = "SELECT * FROM help_desk_issues LEFT JOIN help_desk_watched_issues ON help_desk_watched_issues.issue_id"+
								"=help_desk_issues.issue_id WHERE help_desk_watched_issues.user_username = ? ORDER BY  " + sortBy + " ASC";			
				}
				else
				{
					statement = "SELECT * FROM help_desk_issues LEFT JOIN help_desk_watched_issues ON help_desk_watched_issues.issue_id"+
								"=help_desk_issues.issue_id WHERE help_desk_watched_issues.user_username = ? ORDER BY  " + sortBy + " DESC";
				}
				s = connection.prepareStatement(statement);
				helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
				s.setString(1,currentUser.getUserName());
			}
			
			/* Got db results, now loop through
				adding their column	values to an
				Issue object and throwing them into
				an ArrayList */
			rs = s.executeQuery();
			ArrayList<helpdesk.Issue> myIssues = new ArrayList<>();
			while(rs.next())
			{
				helpdesk.Issue currIssue = new helpdesk.Issue();
				currIssue.setTitle(rs.getString("issue_title"));
				currIssue.setIssueID(rs.getString("issue_id"));
				currIssue.setDateTimeReported(rs.getTimestamp("issue_date_time_reported"));
				currIssue.setDateTimeResolved(rs.getTimestamp("issue_date_time_resolved"));
				
				// Now get category
				switch(rs.getString("category_id"))
				{
					case "NETWORK":
						 currIssue.setCategory(helpdesk.Category.NETWORK);
						break;
					case "SOFTWARE":
						 currIssue.setCategory(helpdesk.Category.SOFTWARE);
						break;
					case "HARDWARE":
						 currIssue.setCategory(helpdesk.Category.HARDWARE);
						break;
					case "EMAIL":
						 currIssue.setCategory(helpdesk.Category.EMAIL);
						break;
					case "ACCOUNT":
						 currIssue.setCategory(helpdesk.Category.ACCOUNT);
						break;
				}
				
				// Now get sub-category
				switch(rs.getString("sub_category_id"))
				{
					case "CANT_CONNECT":
						 currIssue.setSubCategory(helpdesk.SubCategory.CANT_CONNECT);
						break;
					case "SPEED":
						 currIssue.setSubCategory(helpdesk.SubCategory.SPEED);
						break;
					case "DROPOUTS":
						 currIssue.setSubCategory(helpdesk.SubCategory.DROPOUTS);
						break;
					case "LOAD_SLOW":
						 currIssue.setSubCategory(helpdesk.SubCategory.LOAD_SLOW);
						break;
					case "NO_LOAD":
						 currIssue.setSubCategory(helpdesk.SubCategory.NO_LOAD);
						break;
					case "WONT_BOOT":
						 currIssue.setSubCategory(helpdesk.SubCategory.WONT_BOOT);
						break;
					case "BSOD":
						 currIssue.setSubCategory(helpdesk.SubCategory.BSOD);
						break;
					case "HDD":
						 currIssue.setSubCategory(helpdesk.SubCategory.HDD);
						break;
					case "PERIPHERAL":
						 currIssue.setSubCategory(helpdesk.SubCategory.PERIPHERAL);
						break;
					case "CANT_SEND":
						 currIssue.setSubCategory(helpdesk.SubCategory.CANT_SEND);
						break;
					case "CANT_RECEIVE":
						 currIssue.setSubCategory(helpdesk.SubCategory.CANT_RECEIVE);
						break;
					case "SPAM":
						 currIssue.setSubCategory(helpdesk.SubCategory.SPAM);
						break;
					case "RESET_PASSWORD":
						 currIssue.setSubCategory(helpdesk.SubCategory.RESET_PASSWORD);
						break;
					case "WRONG_DETAILS":
						 currIssue.setSubCategory(helpdesk.SubCategory.WRONG_DETAILS);
						break;
				}
				
				// Now get state
				switch(rs.getInt("state_id"))
				{
					case 1:
						currIssue.setState(helpdesk.State.NEW);
						break;
					case 2:
						currIssue.setState(helpdesk.State.IN_PROGRESS);
						break;
					case 3:
						currIssue.setState(helpdesk.State.COMPLETED);
						break;
					case 4:
						currIssue.setState(helpdesk.State.RESOLVED);
						break;
				}
				
				// tags
				currIssue.setTags(rs.getString("issue_tags"));
				myIssues.add(currIssue);	// add this issue to ArrayList
			}
			
			// Actually add ArrayList to session object.
			request.getSession().setAttribute("issuesArrayList",myIssues);
			rs.close();
			s.close();
			connection.close();
			dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/MyIssues.jsp");
			dispatcher.forward(request,response);
			return;
		}
		catch(Exception e)
		{
			dispatcher = getServletContext().getRequestDispatcher("/Error?err=3");
		}
		
		try
		{
			if (rs != null){rs.close();}
			if (s!=null){s.close();}
			if (connection!=null){connection.close();}
			dispatcher.forward(request, response);
		}
		catch(Exception e)
		{
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

