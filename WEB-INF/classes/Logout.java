/*
	Logout.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
	LOGOUT
		This Servlet destroys the session (if
		one exists), then takes user to the 
		login page.
			
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/Logout"})
public class Logout extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		RequestDispatcher dispatcher = null;
		dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/Login.jsp");
		request.getSession().invalidate();	// kills the session
		
		// Now go to the next screen
		try{
			dispatcher.forward(request,response);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
