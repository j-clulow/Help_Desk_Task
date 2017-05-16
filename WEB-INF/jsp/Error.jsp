<!DOCTYPE html>

<!-- 
Error jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying the error message passed to it by various servlets
-->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Error</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sitestyle.css"> <!-- el statement to get the root directory of the application -->
	<link href="https://fonts.googleapis.com/css?family=Calibri" rel="stylesheet">

<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico"> 
</head>
<body>
	<div id = "logoBar">
		<div id = "logo"><a href = "${pageContext.request.contextPath}/Home"><img id = "logoImg" src = "${pageContext.request.contextPath}/img/logo.png" alt = "logo image" /></a></div>
		<div id = "logout">
			<a href = "Logout"><span id = "logoutSpan">Logout</span></a>
		</div>
	</div>
	<div id = "navBar">
		<a class = "linkId" href = "${pageContext.request.contextPath}/Home"><div class = "navBarIn" id = "homeDiv">Home</div></a>
<!-- 		test to see if a user is it or not and display the appropriate nav bar -->
		<c:choose>
			<c:when test="${user.IT}">
				<a class = "linkId" href = "${pageContext.request.contextPath}/ViewAllIssues"><div class = "navBarIn">All Issues</div></a>
				<a class = "linkId" href = "${pageContext.request.contextPath}/MyIssues"><div class = "navBarIn">My Tracked Issues</div></a>
			</c:when>
			<c:otherwise>
				<a class = "linkId" href = "${pageContext.request.contextPath}/ReportIssue"><div class = "navBarIn">Report Issue</div></a>
				<a class = "linkId" href = "${pageContext.request.contextPath}/MyIssues"><div class = "navBarIn">My Issues</div></a>
			</c:otherwise>
		</c:choose>
		<div class = "navBarIn" id = "kbDiv"><div class="dropdown">
	  		<a class="dropbtn" href = "${pageContext.request.contextPath}/KnowledgeBase" >Knowledge Base</button></a>
	  		<div class="dropdown-content">
    			<a class = "dropdown-button" href="${pageContext.request.contextPath}/KnowledgeBase?filter=network">Network</a>
    			<a class = "dropdown-button" href="${pageContext.request.contextPath}/KnowledgeBase?filter=software">Software</a>
    			<a class = "dropdown-button" href="${pageContext.request.contextPath}/KnowledgeBase?filter=hardware">Hardware</a>
    			<a class = "dropdown-button" href="${pageContext.request.contextPath}/KnowledgeBase?filter=email">Email</a>
    			<a class = "dropdown-button" href="${pageContext.request.contextPath}/KnowledgeBase?filter=account">Account</a>
  			</div></div>
  			</div>
	</div>
	<h1>Error</h1>
	<div id="errTxt"><c:out value="${errMsg}" /></div>			<!-- el statement to get the error message -->
	<div id="homeLink"><a href = "${pageContext.request.contextPath}/Home">Go Home</a></div>
</body>
</html>
