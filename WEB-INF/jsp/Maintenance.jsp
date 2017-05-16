<!DOCTYPE html>

<!-- 
Maintenance jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying the page that allows it users to edit the maintenance messages
-->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
	<title>Maintenance</title>
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
	<h1>Edit Maintenance</h1>
	<div id="maintBox">
		<table id = "maintTable">
			<tr><td class="header maint">Message</td><td class="header delete">Delete</td></tr>
			<!-- print all maintenance items -->
			<c:forEach var="maintenance" items = "${maintList}">
				<tr><td class="maint"><c:out value="${maintenance}" /></td>
				<td class="delete">
					<form action="Maintenance" method="post">
						<input type="hidden" name="action" value="delete" />
						<input type="hidden" name="maintenanceString" value="${maintenance}" />
						<input class="delButton" type="submit" value = "Delete" />
					</form></td></tr>
			</c:forEach>
		</table>
		<div class = "maintAddBox">
			<form action = "Maintenance" method = "post" onsubmit="return validateMaint()">
				<input type="hidden" name="action" value="add" />
				<input type="text" id="maintString" name="maintenanceString" placeholder="Add a message..." class="replyText" />
				<input type="submit" value="Add Message" class="replyButton" />
			</form>
		</div>
	</div>
</body>
</html>
