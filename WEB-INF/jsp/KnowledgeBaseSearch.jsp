<!DOCTYPE html>

<!-- 
KnowledgeBaseSearch jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying the knowledge base search page
-->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
	<title>Knowledge Base Search</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sitestyle.css">
	<link href="https://fonts.googleapis.com/css?family=Calibri" rel="stylesheet">
	<script type="text/javascript" src="js/validate.js"></script>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico"> 
</head>
<body>
	<div id = "logoBar">
		<div id = "logo"><a href = "Home"><img id = "logoImg" src = "${pageContext.request.contextPath}/img/logo.png" alt = "logo image" /></a></div>
		<div id = "logout">
			<a href = "Logout"><span id = "logoutSpan">Logout</span></a>
		</div>
	</div>
	<div id = "navBar">
		<a class = "linkId" href = "Home"><div class = "navBarIn" id = "homeDiv">Home</div></a>
		<!-- user or it display of nav bar -->
		<c:choose>
			<c:when test="${user.IT}">
				<a class = "linkId" href = "ViewAllIssues"><div class = "navBarIn">All Issues</div></a>
				<a class = "linkId" href = "MyIssues"><div class = "navBarIn">My Tracked Issues</div></a>
			</c:when>
			<c:otherwise>
				<a class = "linkId" href = "ReportIssue"><div class = "navBarIn">Report Issue</div></a>
				<a class = "linkId" href = "MyIssues"><div class = "navBarIn">My Issues</div></a>
			</c:otherwise>
		</c:choose>
		<div class = "navBarIn" id = "kbDiv"><div class="dropdown">
	  		<div id="kbText" ><a class="dropbtn" href = "KnowledgeBase">Knowledge Base</a></div>
	  		<div class="dropdown-content">
    			<a class = "dropdown-button" href="KnowledgeBase?all=true">All</a>
    			<a class = "dropdown-button" href="KnowledgeBase?filter=network">Network</a>
    			<a class = "dropdown-button" href="KnowledgeBase?filter=software">Software</a>
    			<a class = "dropdown-button" href="KnowledgeBase?filter=hardware">Hardware</a>
    			<a class = "dropdown-button" href="KnowledgeBase?filter=email">Email</a>
    			<a class = "dropdown-button" href="KnowledgeBase?filter=account">Account</a>
  			</div></div>
  			</div>
	</div>
	<div id = "maintenance">
		<c:forEach var="maintenance" items="${maintList}"> <!-- maint list print -->
			<div class = "maintStringDisplay">
				<c:out value = "${maintenance}" />
			</div>
		</c:forEach>
	</div>
	<div id = "knowledgeBasePage">
		<h2 id = "KBArticlesTitle">
			Knowledge Base Articles
		</h2>
		<div id = "sortButtonKB">
			<form action = "KnowledgeBase" method="post">
				<select class = "catTag" id="categorySortBox" name = "sortBy">
					<option class = "categoryOption" value = "date" selected="selected">date</option>
					<option class = "categoryOption" value = "category">category</option>
				</select>
				<select class = "catTag" id="categorySortAscDesc" name = "order">
					<option class = "categoryOption" value = "1" selected="selected">asc</option>
					<option class = "categoryOption" value = "0">desc</option>
				</select>
				<input id="sortInputButton" type="submit" value ="Sort" />
			</form>
		</div>
		<div id="filterSearchBox">
			<div id="filterSearchTitle">
				<h3>Search or Filter</h3>
			</div>
			<div id="knowledgeBaseSearch">
				<form id = "kbSearchForm" class="searchFields" action = "KnowledgeBase" method="post" onsubmit="return validateKBSearch2()">
					<input type="text" name="searchTerm" placeholder="Search by title or single tag..." id = "KBSearchTerm" class = "searchFields" />
					<input type="submit" value="Search" id = "KBSearchButton" class = "searchFields" />
				</form>
			</div>
			<div id="categoryHeader">
				<h3>Categories:</h3>
			</div>
			<div id="catLinks">
				<table>
					<tr><td><a href="KnowledgeBase?all=true">All Articles</a></td></tr>
					<tr><td><a href="KnowledgeBase?filter=HARDWARE">Hardware</a></td></tr>
					<tr><td><table class = "sideBarTables">
						<tr><td><a href="KnowledgeBase?filter=HARDWARE&subFilter=WONT_BOOT">Computer won't turn on</a></td></tr>
						<tr><td><a href="KnowledgeBase?filter=HARDWARE&subFilter=BSOD">Computer "blue screens"</a></td></tr>
						<tr><td><a href="KnowledgeBase?filter=HARDWARE&subFilter=HDD">Disk Drive</a></td></tr>
						<tr><td><a href="KnowledgeBase?filter=HARDWARE&subFilter=PERIPHERAL">Peripherals</a></td></tr>
					</table></td></tr>
					<tr><td><a href="KnowledgeBase?filter=SOFTWARE">Software</a></td></tr>
					<tr><td><table class = "sideBarTables">
						<tr><td><a href="KnowledgeBase?filter=SOFTWARE&subFilter=LOAD_SLOW">Slow to load</a></td></tr>
						<tr><td><a href="KnowledgeBase?filter=SOFTWARE&subFilter=NO_LOAD">Won't load at all</a></td></tr>
					</table></td></tr>
					<tr><td><a href="KnowledgeBase?filter=NETWORK">Network</a></td></tr>
					<tr><td><table class = "sideBarTables">
						<tr><td><a href="KnowledgeBase?filter=NETWORK&subFilter=CANT_CONNECT">Can't connect</a></td></tr>
						<tr><td><a href="KnowledgeBase?filter=NETWORK&subFilter=SPEED">Speed</a></td></tr>
						<tr><td><a href="KnowledgeBase?filter=NETWORK&subFilter=DROPOUTS">Contstant Dropouts</a></td></tr>
					</table></td></tr>
					<tr><td><a href="KnowledgeBase?filter=EMAIL">Email</a></td></tr>
					<tr><td><table class = "sideBarTables">
						<tr><td><a href="KnowledgeBase?filter=EMAIL&subFilter=CANT_SEND">Can't Send</a></td></tr>
						<tr><td><a href="KnowledgeBase?filter=EMAIL&subFilter=CANT_RECEIVE">Can't Receive</a></td></tr>
						<tr><td><a href="KnowledgeBase?filter=EMAIL&subFilter=SPAM">SPAM/Phishing</a></td></tr>
					</table></td></tr>
					<tr><td><a href="KnowledgeBase?filter=ACCOUNT">Account</a></td></tr>
					<tr><td><table class = "sideBarTables">
						<tr><td><a href="KnowledgeBase?filter=ACCOUNT&subFilter=RESET_PASSWORD">Password</a></td></tr>
						<tr><td><a href="KnowledgeBase?filter=ACCOUNT&subFilter=WRONG_DETAILS">Wrong Details</a></td></tr>
					</table></td></tr>
				</table>
			</div>
		</div>
		<div id = "KBList">
			<table id = "KBTable">
				<tr><td class="header">Title</td><td class="header">Date</td><td class="header">Category</td><td class="header">Sub-Category</td></tr>
				<c:forEach var="issue" items = "${articleList}"> <!-- printing the articles in the article arraylist -->
					<tr><td><a href = "Article?id=${issue.issueID}">${issue.title}</a></td><td><fmt:formatDate pattern="dd/MM/yy HH:mm:ss" value = "${issue.dateTimeResolved}" /></td><td>${issue.category}</td><td>${issue.subCategory}</td></tr>
				</c:forEach>
			</table>
		</div>
	</div>
</body>
</html>
