/*
	Login.java
	
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
import javax.naming.NamingException;

@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		// Flow control START
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
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		String userName = request.getParameter("username");														// Store the user name entered by the user from the login request.
		String pwd = request.getParameter("password");															// Store the password entered by the the user from the login request.
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement s = null;
		
		try
		{
			DataSource dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");		// Create the DataSource for accessing the database. NOTE THE CURRENT ADDRESS ISN'T FINAL/AN ACTUAL ADDRESS
			connection = dataSource.getConnection();														// Get a connection to the database.
			s = connection.prepareStatement("SELECT * FROM help_desk_users WHERE user_username=?");					// Prepare the query statement in a way to avoid that thing from the XKCD comic.
			s.setString(1,userName);																				// Set the first unknowwn value in the query to the user name entered by the user.
			rs = s.executeQuery();																		// Execute the query and get the resulting data set. Should only contain one row.
			
			if (rs.next() && rs.getString("user_username").equals(userName) && rs.getString("user_password").equals(pwd))			// If the username and passwword entered match the one retrieved from the database, successful login.
			{
				request.getSession().setAttribute("user", getUser(rs));												// Set the user details for the session.
				response.sendRedirect("./Home");
			}
			else																									// Otherwise unseuccessful login.
			{
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/Login.jsp?err=1");
				rd.forward(request, response);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// now we must close
		try
		{
			rs.close();																							// You weren't raised in a tent, were you?
			s.close();																							// At some point I will run out of different/funny ways
			connection.close();																					// To say that I am closing things because they should be closed.
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private helpdesk.User getUser(ResultSet rs) throws SQLException
	{
		helpdesk.User current = new helpdesk.User();
		current.setUserName(rs.getString("user_username"));
		current.setFirstName(rs.getString("user_firstname"));
		current.setLastName(rs.getString("user_lastname"));
		current.setPhoneNumber(rs.getString("user_phonenumber"));
		current.setEmailAddress(rs.getString("user_emailaddress"));
		switch(rs.getString("role_id"))
		{
			case "IT":
				current.setRole(helpdesk.Role.IT);
				break;
			case "USER":
				current.setRole(helpdesk.Role.USER);
				break;
			default:
				// Should be impossible.
				break;
		}
		return current;
	}
}
