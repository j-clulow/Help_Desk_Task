/*
	ViewAllIssues.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
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

@WebServlet(urlPatterns = {"/ViewAllIssues"})
public class ViewAllIssues extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		// get the ArrayList of maintenance issues
		ArrayList<String> maintList = getMaintenanceEntries(request);
		// Now add the populated ArrayList to the session object
		request.getSession().setAttribute("maintList", maintList);
		
		RequestDispatcher dispatcher = null;
		String dispatcherString = null;
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		boolean redirectNeeded = false;
		
		// Flow control START
		/* Two situations:
			1) User isn't logged in at all, they ought to goto Login page 
			2) User is of type USER and NOT allowed to see all the tickets */
		if (currentUser == null)
		{
			dispatcherString = "/WEB-INF/jsp/Login.jsp";
			redirectNeeded = true;
		}
		else if (currentUser.getRole().equals(helpdesk.Role.USER)) // user is USER
		{
			dispatcherString = "/Error?err=2";
			redirectNeeded = true;
		}
		
		if (redirectNeeded) {
		try{
			dispatcher = getServletContext().getRequestDispatcher(dispatcherString);
			dispatcher.forward(request,response);
			return;
		}
		catch(IOException e){
			e.printStackTrace();
		  } // FLOW CONTROL END
		}
		
		// doPost performs the compilation of issue's into an ArrayList that gets passed to
		// a JSP to display
		doPost(request, response);
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		RequestDispatcher dispatcher = null;
		DataSource dataSource = null;
		Connection connection = null;
		ResultSet rs = null;
		String statement = null;
		String dispatcherString = null;
		String sortBy = "issue_date_time_reported";	// default value
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		PreparedStatement stmnt = null;
		
		try
		{
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			connection = dataSource.getConnection();
			
			// get the flag from the JSP is we are to sort by ASC or DESC
			boolean isAscending = true;
			
			try	{ //will throw an exception when this is called from doGet, as parm will not be present
				isAscending = Integer.parseInt(request.getParameter("order")) == 1; // 1 = ASC, 0 = DESC
			} catch (NumberFormatException e){}
			
			if (request.getParameter("sortBy") != null)
			{
				switch(request.getParameter("sortBy"))
				{
					case "date":
						sortBy = "issue_date_time_reported";
						break;
					case "status":
						sortBy = "state_id";
						break;
				}
			}
			
			if (isAscending)
				statement = "SELECT * FROM help_desk_issues ORDER BY " + sortBy + " ASC";
			else // is descending
				statement = "SELECT * FROM help_desk_issues ORDER BY " + sortBy + " DESC";
			
			stmnt = connection.prepareStatement(statement);
			rs = stmnt.executeQuery();
			ArrayList<helpdesk.Issue> issues = new ArrayList<>();
			while(rs.next())
			{
				helpdesk.Issue issue = new helpdesk.Issue();
				issue.setUserName(rs.getString("user_username"));
				issue.setTitle(rs.getString("issue_title"));
				issue.setIssueID(rs.getString("issue_id"));
				issue.setState(helpdesk.State.NEW);
				switch(rs.getString("category_id"))
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
				
				switch(rs.getInt("state_id"))
				{
					case 1:
						issue.setState(helpdesk.State.NEW);
						break;
					case 2:
						issue.setState(helpdesk.State.IN_PROGRESS);
						break;
					case 3:
						issue.setState(helpdesk.State.COMPLETED);
						break;
					case 4:
						issue.setState(helpdesk.State.RESOLVED);
						break;
				}
				
				switch(rs.getString("sub_category_id"))
				{
					case "BSOD":
						issue.setSubCategory(helpdesk.SubCategory.BSOD);
						break;
					case "CANT_CONNECT":
						issue.setSubCategory(helpdesk.SubCategory.CANT_CONNECT);
						break;
					case "CANT_RECIEVE":
						issue.setSubCategory(helpdesk.SubCategory.CANT_RECEIVE);
						break;
					case "CANT_SEND":
						issue.setSubCategory(helpdesk.SubCategory.CANT_SEND);
						break;
					case "DROPOUTS":
						issue.setSubCategory(helpdesk.SubCategory.DROPOUTS);
						break;
					case "HDD":
						issue.setSubCategory(helpdesk.SubCategory.HDD);
						break;
					case "LOAD_SLOW":
						issue.setSubCategory(helpdesk.SubCategory.LOAD_SLOW);
						break;
					case "PERIPHERAL":
						issue.setSubCategory(helpdesk.SubCategory.PERIPHERAL);
						break;
					case "RESET_PASSWORD":
						issue.setSubCategory(helpdesk.SubCategory.RESET_PASSWORD);
						break;
					case "SPAM":
						issue.setSubCategory(helpdesk.SubCategory.SPAM);
						break;
					case "SPEED":
						issue.setSubCategory(helpdesk.SubCategory.SPEED);
						break;
					case "WONT_BOOT":
						issue.setSubCategory(helpdesk.SubCategory.WONT_BOOT);
						break;
					case "WRONG_DETAILS":
						issue.setSubCategory(helpdesk.SubCategory.WRONG_DETAILS);
						break;
					default:
						issue.setSubCategory(null);
						break;
				}
				issue.setTags(rs.getString("issue_tags"));
				issue.setDateTimeReported(rs.getTimestamp("issue_date_time_reported"));
				issue.setDateTimeResolved(rs.getTimestamp("issue_date_time_resolved"));
				issues.add(issue);
			}
			request.getSession().setAttribute("issuesArrayList",issues);
			dispatcherString = "/WEB-INF/jsp/ViewAllIssues.jsp";
		}
		catch(Exception e)
		{
			dispatcher = getServletContext().getRequestDispatcher("/Error?err=3");
			e.printStackTrace();
		}
		
		try
		{
			if (rs!=null){rs.close();}
			if (stmnt!=null){stmnt.close();}
			if (connection!=null){connection.close();}
			dispatcher = getServletContext().getRequestDispatcher(dispatcherString);
			dispatcher.forward(request,response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> getMaintenanceEntries(HttpServletRequest request)
	{
		ArrayList<String> maintList = new ArrayList<>();
		
		// Try to connect to the DB and get the maintenance entries
		try
		{
			DataSource dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			Connection connection = dataSource.getConnection();
			String myStatement = "SELECT * FROM help_desk_maintenance";
			PreparedStatement stmnt = connection.prepareStatement(myStatement);
			ResultSet rs = stmnt.executeQuery();
		
			// Now populate the ArrayList with the maintenance strings from the db
			while (rs.next())
			{
				maintList.add(rs.getString("maintenance_message"));
			}
			
			// Now close things
			rs.close();
			stmnt.close();
			connection.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return maintList;
	}
}
