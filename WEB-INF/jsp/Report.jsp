<!DOCTYPE html>

<!-- 
Report jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying the page that allows a user to report an issue
-->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Report an Issue</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sitestyle.css">
	<link href="https://fonts.googleapis.com/css?family=Calibri" rel="stylesheet">
	<script src="js/validate.js"></script>
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
	<form id = "issueForm" action = "ReportIssue" method="post" onsubmit="return validateReport()">
		<h2>Enter report details:</h2>
		<label>Issue Title:</label><input id="issueTitle" type = "text" name = "title" autofocus />
		<label>Issue Description:</label><textarea id = "issueDesc" name = "description" cols = "40" rows = "5"></textarea>
		<div class="catTag" ><label>Category: </label></div>
		<select class = "catTag" id="categoryBox" name = "category">
			<option class = "categoryOption" value = "default" selected="selected">--SELECT A CATEGORY--</option>
			<option class = "categoryOption" value = "NETWORK">Network</option>
			<option class = "categoryOption" value = "NETWORK-CANT_CONNECT">--Can't Connect</option>
			<option class = "categoryOption" value = "NETWORK-SPEED">--Speed</option>
			<option class = "categoryOption" value = "NETWORK-DROPOUTS">--Dropouts</option>
			<option class = "categoryOption" value = "SOFTWARE">Software</option>
			<option class = "categoryOption" value = "SOFTWARE-LOAD_SLOW">--Slow to Load</option>
			<option class = "categoryOption" value = "SOFTWARE-NO_LOAD">--Won't Load</option>
			<option class = "categoryOption" value = "HARDWARE">Hardware</option>
			<option class = "categoryOption" value = "HARDWARE-WONT_BOOT">--Won't turn on</option>
			<option class = "categoryOption" value = "HARDWARE-BSOD">--Blue Screen</option>
			<option class = "categoryOption" value = "HARDWARE-HDD">--Disk Drive</option>
			<option class = "categoryOption" value = "HARDWARE-PERIPHERAL">--Peripherals</option>
			<option class = "categoryOption" value = "EMAIL">Email</option>
			<option class = "categoryOption" value = "EMAIL-CANT_SEND">--Can't send</option>
			<option class = "categoryOption" value = "EMAIL-CANT_RECEIVE">--Can't receive</option>
			<option class = "categoryOption" value = "EMAIL-SPAM">--SPAM/Phishing</option>
			<option class = "categoryOption" value = "ACCOUNT">Account</option>
			<option class = "categoryOption" value = "ACCOUNT-RESET_PASSWORD">--Password Reset</option>
			<option class = "categoryOption" value = "ACCOUNT-WRONG_DETAILS">--Wrong Details</option>
		</select>
		<div class="catTag" ><label>Tags (comma separated): </label></div><input class = "catTag" id = "tagBox" type="text" name="tags" />
		<div id = "anonCheckBox"><input type="checkbox" name="anonymous" id="anonymous" value = "1" /><label for="anonymous" id="anonymousFlavour">If this issue becomes a knowledge base article, I wish to remain anonymous.</label></div>
		<div><input class = "subclear" type="submit" name="submit" value="Submit" />
		<input class = "subclear" type="reset" name="clear" value="Clear" /></div>
	</form>
</body>
</html>
