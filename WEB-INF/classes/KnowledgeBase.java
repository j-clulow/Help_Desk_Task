/*
	KnowledgeBase.java
	
	This class file pertains to the help desk
	software project for SENG2050 assignment 3, 2016.
	
	KNOWLEDGE BASE
		This Servlet handles the logic of the Knowledge Base.
		It is responsible for searching the DB for knowledge
		base articles as per the user's search terms.
		It retrieves all knowledge base articles, throws them
		in an ArrayList and puts that into a session attribute
		for printing via a JSP.
	
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
import java.util.Iterator;

@WebServlet(urlPatterns = {"/KnowledgeBase"})
public class KnowledgeBase extends HttpServlet
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
		helpdesk.User currentUser = (helpdesk.User) request.getSession().getAttribute("user");
		
		/* Setup which screen we ought to go to next. The
			user went directly to this servlet via the URL
			bar and therefore needs to be directed to one of
			three different possible pages:
		
		Two situations:
			1)	User isn't logged in at all, they need to go to Login page
			2)	User is logged in and need to go to KnowledgeBase.jsp
		*/
		
		if (currentUser == null)	// not logged in
		{
			redirectString = "/WEB-INF/jsp/Login.jsp";
		}
		else if (request.getParameter("filter") != null || (request.getParameter("all")!= null && request.getParameter("all").equals("true")))
		{
			doPost(request, response);
			return;
		}
		else	// user is logged in
		{
			redirectString = "/WEB-INF/jsp/KnowledgeBase.jsp";
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
		DataSource dataSource = null;
		Connection connection = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		RequestDispatcher dispatcher = null;
		String destination = "Error?err=4";
		boolean isAscending = true;
		ArrayList<ResultSet> results = new ArrayList<>();	// will holds the results that get stored into the session attribute for JSP delivery

		try{	// to get all search terms, and order preferences
			String searchTerm = request.getParameter("searchTerm");
			String filter = request.getParameter("filter");
			String sortBy = "knowledge_base_date_time_resolved";
			String subFilter = request.getParameter("subFilter");
			if(request.getParameter("sortBy") != null)
			{
				sortBy = request.getParameter("sortBy");
				switch(request.getParameter("sortBy"))
				{
					case "date":
						sortBy = "knowledge_base_date_time_reported";
						break;
					case "category":
						sortBy = "category_id";
						break;
				}
			}
			
			try	// to actually retrieve knowledge base articles from the db
			{
				// ascending = 1, else descending.
				isAscending = (Integer.parseInt(request.getParameter("order")) == 1);
			}
			catch (Exception e)	{}
			String order = null;
			if(isAscending)
				order = "ASC";
			else
				order = "DESC";
				
			// Now connect to DB
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/HELP_DESK_DB");
			connection = dataSource.getConnection();
			String statement = null;
			
			/* NO SEARCH TERM = DISPLAY ALL
			*******************************/
			if(searchTerm == null && filter == null)
			{
				statement = "SELECT * FROM help_desk_knowledge_base ORDER BY " + sortBy + " " + order;
				s = connection.prepareStatement(statement);
			}
			
			/* SEARCH VIA SEARCH TERMS WITH NO FILTER
			*****************************************/
			else if(searchTerm != null && filter == null)
			{
				String searchTerms[] = searchTerm.split(" ");
				statement = "SELECT * FROM help_desk_knowledge_base WHERE knowledge_base_tags LIKE ? OR knowledge_base_title REGEXP ? ORDER BY " + sortBy + " " + order;
				for(String search : searchTerms){
					PreparedStatement listItem = connection.prepareStatement(statement);
					listItem.setString(1,("%," + search + ",%"));
					listItem.setString(2,("(^| )[\\.\\(]*" + searchTerm + "[\\.\\,\\)]*( |$)"));
					results.add(listItem.executeQuery());
				}
			}
			
			/* RETRIEVE AND SORT VIA FILTER
			*******************************/
			else if(searchTerm == null && filter != null && subFilter == null)
			{
				statement = "SELECT * FROM help_desk_knowledge_base WHERE category_id = ? ORDER BY " + sortBy + " " + order;
				s = connection.prepareStatement(statement);
				s.setString(1,filter);
			}
			
			/* RETRIEVE AND SORT BY FILTER + SUB-FILTER
			********************************************/
			else if(searchTerm == null && filter != null && subFilter != null)
			{
				statement = "SELECT * FROM help_desk_knowledge_base WHERE category_id = ? AND sub_category_id = ? ORDER BY " + sortBy + " " + order;
				s = connection.prepareStatement(statement);
				s.setString(1,filter);
				s.setString(2,subFilter);
			}
			
			/* RETRIEVE AND SORT VIA TAGS
			*****************************/
			else
			{
				statement = "SELECT * FROM help_desk_knowledge_base WHERE knowledge_base_tags LIKE ? OR knowledge_base_title REGEXP ? AND category_id = ? AND sub_category_id = ? ORDER BY " + sortBy + " " + order;
				s = connection.prepareStatement(statement);
				s.setString(1,("%," + searchTerm + ",%"));
				s.setString(2,("(^| )[\\.\\(]*" + searchTerm + "[\\.\\,\\)]*( |$)"));
				s.setString(3,filter);
				s.setString(4,subFilter);
			}
			
			// Now throw results into an array list
			ArrayList<helpdesk.Issue> knowBaseList = new ArrayList<>();
			if(results.isEmpty()){
				rs = s.executeQuery();
				
				// Loop through restults, get each column value and add it to an Issue object
				while(rs.next())
				{
					helpdesk.Issue knowBaseArticle = new helpdesk.Issue();
					knowBaseArticle.setUserName(rs.getString("user_username"));
					knowBaseArticle.setTitle(rs.getString("knowledge_base_title"));
					switch(rs.getString("category_id"))
					{
						case "ACCOUNT":
							knowBaseArticle.setCategory(helpdesk.Category.ACCOUNT);
							break;
						case "EMAIL":
							knowBaseArticle.setCategory(helpdesk.Category.EMAIL);
							break;
						case "HARDWARE":
							knowBaseArticle.setCategory(helpdesk.Category.HARDWARE);
							break;
						case "NETWORK":
							knowBaseArticle.setCategory(helpdesk.Category.NETWORK);
							break;
						case "SOFTWARE":
							knowBaseArticle.setCategory(helpdesk.Category.SOFTWARE);
							break;
						default:
							// You done goofed Ay-ay-ron!
					}
					switch(rs.getString("sub_category_id"))
					{
						case "BSOD":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.BSOD);
							break;
						case "CANT_CONNECT":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.CANT_CONNECT);
							break;
						case "CANT_RECIEVE":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.CANT_RECEIVE);
							break;
						case "CANT_SEND":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.CANT_SEND);
							break;
						case "DROPOUTS":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.DROPOUTS);
							break;
						case "HDD":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.HDD);
							break;
						case "LOAD_SLOW":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.LOAD_SLOW);
							break;
						case "PERIPHERAL":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.PERIPHERAL);
							break;
						case "RESET_PASSWORD":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.RESET_PASSWORD);
							break;
						case "SPAM":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.SPAM);
							break;
						case "SPEED":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.SPEED);
							break;
						case "WONT_BOOT":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.WONT_BOOT);
							break;
						case "WRONG_DETAILS":
							knowBaseArticle.setSubCategory(helpdesk.SubCategory.WRONG_DETAILS);
							break;
						default:
							knowBaseArticle.setSubCategory(null);
							break;
					}
					knowBaseArticle.setTags(rs.getString("knowledge_base_tags"));
					knowBaseArticle.setDateTimeResolved(rs.getTimestamp("knowledge_base_date_time_resolved"));
					knowBaseArticle.setIssueID(rs.getString("knowledge_base_id"));
					knowBaseArticle.setState(helpdesk.State.RESOLVED);
					knowBaseArticle.isKnowledgeBaseArticle();
					knowBaseList.add(knowBaseArticle);
				}
			}
			else
			{
				for(ResultSet resultItem : results)
				{
					while(resultItem.next())
					{
						helpdesk.Issue knowBaseArticle = new helpdesk.Issue();
						knowBaseArticle.setUserName(resultItem.getString("user_username"));
						knowBaseArticle.setTitle(resultItem.getString("knowledge_base_title"));
						switch(resultItem.getString("category_id"))
						{
							case "ACCOUNT":
								knowBaseArticle.setCategory(helpdesk.Category.ACCOUNT);
								break;
							case "EMAIL":
								knowBaseArticle.setCategory(helpdesk.Category.EMAIL);
								break;
							case "HARDWARE":
								knowBaseArticle.setCategory(helpdesk.Category.HARDWARE);
								break;
							case "NETWORK":
								knowBaseArticle.setCategory(helpdesk.Category.NETWORK);
								break;
							case "SOFTWARE":
								knowBaseArticle.setCategory(helpdesk.Category.SOFTWARE);
								break;
							default:
								// You done goofed Ay-ay-ron!
						}
						switch(resultItem.getString("sub_category_id"))
						{
							case "BSOD":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.BSOD);
								break;
							case "CANT_CONNECT":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.CANT_CONNECT);
								break;
							case "CANT_RECIEVE":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.CANT_RECEIVE);
								break;
							case "CANT_SEND":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.CANT_SEND);
								break;
							case "DROPOUTS":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.DROPOUTS);
								break;
							case "HDD":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.HDD);
								break;
							case "LOAD_SLOW":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.LOAD_SLOW);
								break;
							case "PERIPHERAL":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.PERIPHERAL);
								break;
							case "RESET_PASSWORD":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.RESET_PASSWORD);
								break;
							case "SPAM":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.SPAM);
								break;
							case "SPEED":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.SPEED);
								break;
							case "WONT_BOOT":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.WONT_BOOT);
								break;
							case "WRONG_DETAILS":
								knowBaseArticle.setSubCategory(helpdesk.SubCategory.WRONG_DETAILS);
								break;
							default:
								knowBaseArticle.setSubCategory(null);
								break;
						}
						knowBaseArticle.setTags(resultItem.getString("knowledge_base_tags"));
						knowBaseArticle.setDateTimeResolved(resultItem.getTimestamp("knowledge_base_date_time_resolved"));
						knowBaseArticle.setIssueID(resultItem.getString("knowledge_base_id"));
						knowBaseArticle.setState(helpdesk.State.RESOLVED);
						knowBaseArticle.isKnowledgeBaseArticle();
						boolean inList = false;
						for(helpdesk.Issue iss : knowBaseList){
							if(iss.getIssueID().equals(knowBaseArticle.getIssueID())){
								inList = true;
								break;
							}
						}
						if(!inList){
							knowBaseList.add(knowBaseArticle);
						}
					}
				}
			}
			
			/* FORWARD TO APPROPRIATE PAGE
			*****************************/
			request.getSession().setAttribute("articleList",knowBaseList);
			destination = "/WEB-INF/jsp/KnowledgeBaseSearch.jsp";
		}
		catch(SQLException e)
		{
			destination = "/Error?err=3";
			e.printStackTrace();
		}
		catch(Exception e)
		{
			destination = "/Error?err=4";
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null) rs.close();
				if(s != null) s.close();
				if(connection != null) connection.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			try{
				dispatcher = getServletContext().getRequestDispatcher(destination);
				dispatcher.forward(request,response);
				return;
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
