<!DOCTYPE html>

<!-- 
ITHome jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying the home page of the IT role users
-->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Home</title>
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
		<a class = "linkId" href = "ViewAllIssues"><div class = "navBarIn">All Issues</div></a>
		<a class = "linkId" href = "MyIssues"><div class = "navBarIn">My Tracked Issues</div></a>
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
		<c:forEach var="maintenance" items="${maintList}"> <!-- for each maintenance item -->
			<div class = "maintStringDisplay">
				<c:out value = "${maintenance}" />
			</div>
		</c:forEach>
	</div>
	<div>
		<table id = "tiles">
			<tr>
				<td class = "ITtd"><a href = "ViewAllIssues"><img class = "ITimgButton" src = "${pageContext.request.contextPath}/img/allissues.png" alt="open issues image" /></a></td>
				<td class = "ITtd"><a href = "MyIssues"><img class = "ITimgButton" src = "${pageContext.request.contextPath}/img/mytrackedissues.png" alt="My tracked issues image" /></a></td>
			</tr>
			<tr>
				<td class = "ITtd"><a href = "KnowledgeBase"><img class = "ITimgButton" src = "${pageContext.request.contextPath}/img/knowledgebase.png" alt="Knowledge Base image" /></a></td>
				<td class = "ITtd"><a href = "Maintenance"><img class = "ITimgButton" src = "${pageContext.request.contextPath}/img/maintenancetile.png" alt="Maintenance button" /></a></td>
			</tr>
		</table>
	</div>
</body>
</html>
