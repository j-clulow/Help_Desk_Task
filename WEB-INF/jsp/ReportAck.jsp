<!DOCTYPE html>

<!-- 
Error jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying the report received page to users after they've reported an issue.
-->

<html>
<head>
	<title>We're working on it!</title>
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
		<a class = "linkId" href = "ReportIssue"><div class = "navBarIn">Report Issue</div></a>
		<a class = "linkId" href = "MyIssues"><div class = "navBarIn">My Issues</div></a>
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
	<h1>Message Recieved!</h1>
	<div id="ackText">Your problem has been added to our system and our staff will get back to you A.S.A.P.!</div>
	<div id="homeLink"><a href = "Home">Go Home</a></div>
</body>
</html>
