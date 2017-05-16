/*
	Maintenance.java
	
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

@WebServlet(urlPatterns = {"/Maintenance"})
public class Maintenance extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		String dispatcherString = null;
		RequestDispatcher dispatcher = null;
		
		if (currentUser == null)
		{
			dispatcherString = "/WEB-INF/jsp/Login.jsp";
		}
		else if (currentUser.getRole().equals(helpdesk.Role.USER)) // user is USER
		{
			dispatcherString = "/WEB-INF/jsp/UserHome.jsp";
		}
		else	
		{
			dispatcherString = "/WEB-INF/jsp/Maintenance.jsp";
		}
		
		// get the ArrayList of maintenance issues
		ArrayList<String> maintList = getMaintenanceEntries(request);
		// Now add the populated ArrayList to the session object
		request.getSession().setAttribute("maintList", maintList);
		
		try
		{
			// Now go to the next screen
			dispatcher = getServletContext().getRequestDispatcher(dispatcherString);
			dispatcher.forward(request,response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		String redirectString = "/WEB-INF/jsp/Maintenance.jsp";
		String myStatement = null;;
		ResultSet rs = null;
		PreparedStatement stmnt = null;
		Connection connection = null;
		RequestDispatcher dispatcher = null;
		DataSource dataSource = null;
		
		try
		{
			// Try to connect to the DB and get the maintenance entries
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			connection = dataSource.getConnection();
			
			String action = request.getParameter("action"); // will be "delete" or "add"
			
			if (action.equals("delete"))
			{
				myStatement = "DELETE FROM help_desk_maintenance WHERE maintenance_message = ?";
			}
			else if (action.equals("add"))
			{
				myStatement = "INSERT INTO help_desk_maintenance (maintenance_message) VALUES (?)";
			}
			else
			{
				// error condition
			}
			
			String maintenanceString = request.getParameter("maintenanceString"); // will be used to identify which maintenance entry to delete
			stmnt = connection.prepareStatement(myStatement);
			stmnt.setString(1,maintenanceString);
			stmnt.executeUpdate();
			
			// Now go to the next screen
			response.sendRedirect("./Maintenance");
		}
		catch (Exception e)
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
