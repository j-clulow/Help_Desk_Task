<!DOCTYPE html>

<!-- 
MyIssues jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying a variety of issue lists including non-IT user's asked issues,
all tracked issues for it staff and knowledge base article searches.
-->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
	<title>My Open Tickets</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sitestyle.css">
	<link href="https://fonts.googleapis.com/css?family=Calibri" rel="stylesheet">
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
	  		<a class="dropbtn" href = "KnowledgeBase" >Knowledge Base</a>
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
		<c:forEach var="maintenance" items="${maintList}">
			<div class = "maintStringDisplay">
				<c:out value = "${maintenance}" />
			</div>
		</c:forEach>
	</div>
	<div id = "ticketsPage">
		<h2 id = "openTicketsTitle">
			<c:choose>
			<c:when test="${user.IT}" >
				My Open Tickets
			</c:when>
			<c:otherwise>
				My Open Issues
			</c:otherwise>
			</c:choose>
		</h2>
		<div id = "sortButton">
			<form action = "MyIssues" method="post">
				<select class = "catTag" id="categorySortBox" name = "sortBy">
					<option class = "categoryOption" value = "date" selected="selected">date</option>
					<option class = "categoryOption" value = "status">status</option>
				</select>
				<select class = "catTag" id="categorySortAscDesc" name = "order">
					<option class = "categoryOption" value = "1" selected="selected">asc</option>
					<option class = "categoryOption" value = "0">desc</option>
				</select>
				<input id="sortInputButton" type="submit" value ="Sort" />
			</form>
		</div>
		<div id = "issueList">
			<table id = "issuesTable">
				<tr><td class="header">Title</td><td class="header">Date</td><td class="header">Status</td><td class="header">Category</td><td class="header">Sub-Category</td></tr>
				<c:forEach var="issue" items = "${issuesArrayList}">
					<tr><td><a href = "Issue?id=${issue.issueID}"> ${issue.title} </a></td><td><fmt:formatDate pattern="dd/MM/yy HH:mm:ss" value = "${issue.dateTimeReported}" /></td><td>${issue.state}</td><td>${issue.category}</td><td>${issue.subCategory}</td></tr>
				 </c:forEach>
			</table>
		</div>
	</div>
</body>
</html>
