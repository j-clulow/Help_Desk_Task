<!DOCTYPE html>

<!-- 
Login jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying the login page
-->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>IT HelpDesk Login</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sitestyle.css">
	<link href="https://fonts.googleapis.com/css?family=Calibri" rel="stylesheet">
	<script src="js/validate.js"></script>
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico"> 
</head>
<body>
	<img id = "loginLogo" src = "${pageContext.request.contextPath}/img/logo.png" alt="logo" />	
	<form id = "loginForm" action = "Login" method = "post" onsubmit="return validateCreds()">
		<h2 id = "loginHeader">Login</h2>
		<!-- if theres an error code -->
		<c:if test = "${param.err == 1}">
			<div id = "loginError">Invalid username/password combination</div>
		</c:if>
		<label>Username:</label><input id = "username" type = "text" name = "username" autofocus />
		<div class = "formDiv"><label>Password:</label><input id = "pass" type = "password" name = "password" /></div>
		<div class = "formDiv"><label><input id = "login" type = "submit" value = "Login" /></label></div>
	</form>
</body>
</html>
