/*
	Error.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
	ERROR
		This Servlet handles the logic in reporting an error
		to the user. The errors are handled by error codes (ints)
		eg:	3 = SQL error, 2 = Access Denied, etc.
	
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/Error"})
public class Error extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		String redirectString = null;
		RequestDispatcher dispatcher = null;
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		
		/* FLOW CONTROL START */
		// Flow control case: No error code (someone typed "/Error" in the URL) or no one logged in and requesting Error?err=1 (invalid username/password)
		if (request.getParameter("err") == null || (currentUser == null && (Integer.parseInt(request.getParameter("err")) != 1)))	
		{
			redirectString = "/Home";
			dispatcher = getServletContext().getRequestDispatcher(redirectString);
			try{
				dispatcher.forward(request,response);
				return;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		/* FLOW CONTROL END */
		
		int code = Integer.parseInt(request.getParameter("err"));
		// Flow control case: code id present and some one's actually logged in
		switch(code)
		{
			case 1:
				if (request.getParameter("username") == null)
					redirectString = "/WEB-INF/jsp/Login.jsp?err=0";
				else
					redirectString = "/WEB-INF/jsp/Login.jsp?err=1";
				break;
			case 2:
				request.getSession().setAttribute("errMsg","Unauthorised access to requested resource for user type: " + currentUser.getRole());
				redirectString = "/WEB-INF/jsp/Error.jsp";
				break;
			case 3:
				request.getSession().setAttribute("errMsg","There seems to be a problem connecting to the database.");
				redirectString = "/WEB-INF/jsp/Error.jsp";
				break;
			case 4:
				request.getSession().setAttribute("errMsg","Something unexpected went wrong.");
				redirectString = "/WEB-INF/jsp/Error.jsp";
				break;
			case 5:
				request.getSession().setAttribute("errMsg","Requested resource doesn't exist.");
				redirectString = "/WEB-INF/jsp/Error.jsp";
				break;
			case 99:
				request.getSession().setAttribute("errMsg","What kind of URL is that? ...come on Yuqing, bro");
				redirectString = "/WEB-INF/jsp/Error.jsp";
				break;
			default:
				request.getSession().setAttribute("errMsg","Error ID who-what now? ..Why must you try and break me?");
				redirectString = "/WEB-INF/jsp/Error.jsp";
				break;
		}
		
		dispatcher = getServletContext().getRequestDispatcher(redirectString);
		try{
			dispatcher.forward(request,response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return;
	}
}
