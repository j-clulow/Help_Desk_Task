/*
	Home.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
	HOME
		This servlet handles the logic for displaying
		the correct Home screen. These are different
		depending on the current user's role in the 
		system, i.e., IT staff have a different Home Page 
		than regular old users.
	
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


@WebServlet(urlPatterns = {"/Home"})
public class Home extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		/* MAINTENANCE START */
		// get the ArrayList of maintenance issues
		ArrayList<String> maintList = getMaintenanceEntries(request);
		// Now add the populated ArrayList to the session object
		request.getSession().setAttribute("maintList", maintList);
		/* MAINTENANCE END */
		
		String redirectString = null;
		RequestDispatcher dispatcher = null;
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		
		/* Setup which screen we ought to go to next. The
			user went directly to this servlet via the URL
			bar and therefore needs to be directed to one of
			three different possible pages:
		
		Three situations:
			1)	User isn't logged in at al;, i.e.,  is no User in session object.
			2)	User's role is 'USER' and should go to their appropriate home page.
			3)	User's role is 'IT' and should go to their appropriate page.
		*/
		
		if (currentUser == null)
		{
			redirectString = "/WEB-INF/jsp/Login.jsp";
		}
		else
		{
			switch(currentUser.getRole())
			{
				case IT:
					redirectString = "/WEB-INF/jsp/ITHome.jsp";
					break;
				case USER:
					redirectString = "/WEB-INF/jsp/UserHome.jsp";
					break;
				default:
					break;
			}
		}
		
		// Now go to the next screen
		dispatcher = getServletContext().getRequestDispatcher(redirectString);
		try{
			dispatcher.forward(request,response);
		}
		catch(IOException e){
			e.printStackTrace();
		} // Flow control END
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		String redirectString = "";
		RequestDispatcher dispatcher = null;
		boolean err = false;
		helpdesk.User currentUser = (helpdesk.User)request.getSession().getAttribute("user");
		
		/* setup which screen we ought to go to next. The
			user went directly to this servlet via the URL
			bar and therefore needs to be directed to one of
			three different possible pages:
		
		Three situations:
			1)	User's role is 'IT' and should go to their appropriate page.
			2)	User's role is 'USER' and should go to their appropriate home page.
			3)	Bad username or password
		*/
		switch(currentUser.getRole())
		{
			case IT:
				redirectString = "/WEB-INF/jsp/ITHome.jsp";
				break;
			case USER:
				redirectString = "/WEB-INF/jsp/UserHome.jsp";
				break;
			default:
				redirectString = "/Error?code=1";
				err = true;
				break;
		}
		dispatcher = getServletContext().getRequestDispatcher(redirectString);
		try{
			dispatcher.forward(request,response);
		}
		catch(IOException e){
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
