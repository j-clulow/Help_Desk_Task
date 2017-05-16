<!DOCTYPE html>

<!-- 
KnowledgeBase jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying the knowledge base home page
-->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Knowledge Base Home</title>
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
		<c:forEach var="maintenance" items="${maintList}"> <!-- for each maintenance string print string -->
			<div class = "maintStringDisplay">
				<c:out value = "${maintenance}" />
			</div>
		</c:forEach>
	</div>
	<div id="knowledgeBasePage">
		<div id="knowledgeBaseTitle">
			<h2>Knowledge Base</h2>
		</div>
		<div id="knowledgeBaseSearch">
			<div id="searchText" class="searchFields">Search:</div>
			<form id = "kbSearchForm" class="searchFields" action = "KnowledgeBase" method="post" onsubmit="return validateKBSearch()">
				<input type="hidden" name="order" value="1" />
				<input type="text" name="searchTerm" placeholder="Search by title or by tag (single tag at a time)..." id = "searchTerm" class = "searchFields" />
				<input type="submit" value="Search" id = "searchButton" class = "searchFields" />
			</form>
			<form class="searchFields" action = "KnowledgeBase" method="post">
				<input type="submit" value="View All" id = "advancedSearchButton" class = "searchFields" />
			</form>
		</div>
		<h2 id="selectACat">Or Select a Category</h2>
		<div id = "catTableDiv">
			<table id="catTable">
				<tr>
					<td class="header"><a href="KnowledgeBase?filter=ACCOUNT"><h3>Account</h3></a></td>
					<td class="header"><a href="KnowledgeBase?filter=EMAIL"><h3>Email</h3></a></td>
					<td class="header"><a href="KnowledgeBase?filter=HARDWARE"><h3>Hardware</h3></a></td>
					<td class="header"><a href="KnowledgeBase?filter=NETWORK"><h3>Network</h3></a></td>
					<td class="header"><a href="KnowledgeBase?filter=SOFTWARE"><h3>Software</h3></a></td>
				</tr>
				<tr>
					<td><a href="KnowledgeBase?filter=ACCOUNT&subFilter=RESET_PASSWORD">Password Reset</a></td>
					<td><a href="KnowledgeBase?filter=EMAIL&subFilter=CANT_SEND">Can't Send</a></td>
					<td><a href="KnowledgeBase?filter=HARDWARE&subFilter=WONT_BOOT">Computer Won't Turn On</a></td>
					<td><a href="KnowledgeBase?filter=NETWORK&subFilter=CANT_CONNECT">Can't connect</a></td>
					<td><a href="KnowledgeBase?filter=SOFTWARE&subFilter=LOAD_SLOW">Slow to load</a></td>
				</tr>

				<tr>
					<td><a href="KnowledgeBase?filter=ACCOUNT&subFilter=WRONG_DETAILS">Wrong Details</a></td>
					<td><a href="KnowledgeBase?filter=EMAIL&subFilter=CANT_RECEIVE">Can't Receive</a></td>
					<td><a href="KnowledgeBase?filter=HARDWARE&subFilter=BSOD">Computer "blue screens"</a></td>
					<td><a href="KnowledgeBase?filter=NETWORK&subFilter=SPEED">Speed</a></td>
					<td><a href="KnowledgeBase?filter=SOFTWARE&subFilter=NO_LOAD">Won't load at all</a></td>
				</tr>

				<tr>
					<td></td>
					<td><a href="KnowledgeBase?filter=EMAIL&subFilter=SPAM">SPAM/Phishing</a></td>
					<td><a href="KnowledgeBase?filter=HARDWARE&subFilter=HDD">Disk Drive</a></td>
					<td><a href="KnowledgeBase?filter=NETWORK&subFilter=DROPOUTS">Constant Dropouts</a></td>
					<td></td>
				</tr>

				<tr>
					<td></td>
					<td></td>
					<td><a href="KnowledgeBase?filter=HARDWARE&subFilter=PERIPHERAL">Peripherals</a></td>
					<td></td>
					<td></td>
				</tr>

			</table>
		</div>
	</div>
</body>
</html>
