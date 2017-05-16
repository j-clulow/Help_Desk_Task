<!DOCTYPE html>

<!-- 
Issue jsp for SENG2050 Assignment 3
Progammers: Jacob Clulow | 3164461, George Edwards | 3167656, Chris O'Donnell | 3165338
Date Completed: 6/11/16
This jsp is responsible for displaying all variations on an issue, including new, in-progress, complete, resolved and knowledge
base article for both users and IT
-->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
	<c:choose>
		<c:when  test = "${not issue.knowledgeBaseArticle}">
			<title>View Issue</title>
		</c:when>
		<c:otherwise>
			<title>View Article</title>
		</c:otherwise>
	</c:choose>
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
		<!-- user test for nav bar display -->
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
	  		<a class="dropbtn" href = "KnowledgeBase">Knowledge Base</a>
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
	<div id="maintenance">
		<c:forEach var="maintenance" items="${maintList}">	<!-- loops through the maintenance objects and prints each one -->
			<div class = "maintStringDisplay">
				<c:out value = "${maintenance}" />
			</div>
		</c:forEach>
	</div>
	<div id = "viewIssueDiv">
		<div id = "viewIssueTitleDate">
<!-- 			if it's a knowledge base article we want the div id to be knowledge title -->
			<c:if test="${issue.knowledgeBaseArticle}">
				<div id = "viewKnowledgeTitle"><h2><c:out value = "${issue.title}" /></h2></div>
			</c:if>
			<!-- if it's not a knowledge base article, different id-->
			<c:if test="${not issue.knowledgeBaseArticle}">
				<div id = "viewKnowledgeTitle"><h2><c:out value = "${issue.title}" /></h2></div>
			</c:if>
			<!-- if it is a knowledge base article, display date received and resolved, if not just date received-->
			<c:if test="${issue.knowledgeBaseArticle}">
				<div id = "viewIssueDate">Date Asked: <fmt:formatDate pattern="dd/MM/yy HH:mm:ss" value = "${issue.dateTimeReported}" /></div>
				<div id = "viewIssueDateResolved">Date Resolved: <fmt:formatDate pattern="dd/MM/yy HH:mm:ss" value = "${issue.dateTimeResolved}" /></div>
			</c:if>
			<c:if test="${issue.state == 'Resolved' && not issue.knowledgeBaseArticle}">
				<div id = "viewIssueDate">Date Asked: <fmt:formatDate pattern="dd/MM/yy HH:mm:ss" value = "${issue.dateTimeReported}" /></div>
				<div id = "viewIssueDateResolved">Date Resolved: <fmt:formatDate pattern="dd/MM/yy HH:mm:ss" value = "${issue.dateTimeResolved}" /></div>
			</c:if>
			<c:if test="${not issue.knowledgeBaseArticle && issue.state ne 'Resolved'}">
				<div id = "viewIssueDate">Date Asked: <fmt:formatDate pattern="dd/MM/yy HH:mm:ss" value = "${issue.dateTimeReported}" /></div>
			</c:if>
			<!-- not a kb article userNotKB div id, user div if if is in kb-->
			<c:if test="${not issue.knowledgeBaseArticle}">
				<div id = "userNotKB">User: ${issue.userName}</div>
			</c:if>
			<c:if test="${issue.knowledgeBaseArticle}">
				<div id = "user">User: ${issue.userName}</div>
			</c:if>
		</div>

		<div id = "viewIssueDescStat">
			<div id = "viewIssueDesc"><c:out value = "${issue.description}" /></div>
			<!-- if it's not a knowledge base article we need to display status -->
			<c:if test ="${not issue.knowledgeBaseArticle}"><div id = "viewIssueStatus">Status: <c:out value="${issue.state}" /></div></c:if>
		</div>
		
		<!--	Test: user is it and it's not a kb article and it's not tracked by this user and it hasn't been resolved  then we need a track button-->
		<c:if test="${user.IT && not issue.knowledgeBaseArticle && not trackedByThisUser && issue.state ne 'Resolved'}">
			<div id = "trackForm">
				<form action="Update" method="post">
					<input type="hidden" name="action" value = "track" />
					<input type="hidden" name="issueID" value = "${issue.issueID}" />
					<input type="hidden" name="updated" value = "user" />
					<input id = "trackButton" type="submit" name="track" value="Track this Issue" />
				</form>
			</div>
		</c:if>
		
		<!--	Test: user is it and not a kb article and it is tracked by this user and it's not resolved, track button is greyed out-->
		<c:if test="${user.IT && not issue.knowledgeBaseArticle && trackedByThisUser && issue.state ne 'Resolved'}">
			<div id = "trackForm">
				<form action="Update" method="post">
					<input type="hidden" name="action" value = "track" />
					<input type="hidden" name="issueID" value = "${issue.issueID}" />
					<input type="hidden" name="updated" value = "user" />
					<input id = "trackButton" type="submit" disabled="disabled" name="track" value="tracking" />
				</form>
			</div>
		</c:if>
		
		<!--	Test: user is it issue is not kb article issue is resolved need a track button-->
		<c:if test="${user.IT && not issue.knowledgeBaseArticle && issue.state == 'Resolved'}">
			<div id = "trackForm">
				<form action="Update" method="post">
					<input type="hidden" name="action" value = "add" />
					<input type="hidden" name="issueID" value = "${issue.issueID}" />
					<input type="hidden" name="updated" value = "issue" />
					<input id = "trackButton" type="submit" name="track" value="Add to KB" />
				</form>
			</div>
		</c:if>
		
		<!--	Test: user-it, is kb article state is resolved disabled track button-->
		<c:if test="${user.IT && issue.knowledgeBaseArticle && issue.state == 'Resolved'}">
			<div id = "trackForm">
				<form action="Update" method="post">
					<input type="hidden" name="action" value = "add" />
					<input type="hidden" name="issueID" value = "${issue.issueID}" />
					<input type="hidden" name="updated" value = "issue" />
					<input id = "trackButton" type="submit" disabled="disabled" name="track" value="Issue in KB" />
				</form>
			</div>
		</c:if>
		
		<!-- if user is it and solution not yet suggested make a box to make a suggestion in-->
		<c:if test="${user.IT}">
			<c:if test="${empty issue.resolution}">
				<div id = "solutionInput">
					<div id="solutionInputForm">
						<form action = "Update" method="post" onsubmit="return validateSuggestion()"> <!-- suggestion must not be empty -->
							<input type="hidden" name="action" value="solutionIn" />
							<input type="hidden" name="issueID" value="${issue.issueID}" />
							<input type="hidden" name="updated" value="issue" />
							<input id="suggestionInput" type="text" name="solutionFromIT" placeholder="Suggest a solution..." />
							<input id="suggestionButton" type="submit" value="Suggest" />
						</form>
					</div>
				</div>
			</c:if>
		</c:if>
		<!-- user is not it, issue is not resolved and solution has been suggested need two boxes one for accept and one for reject-->
		<c:if test="${issue.state ne 'Resolved' && not empty issue.resolution}">
					<div id="overallSolDiv">
						<div id = "solutionSuggested">
							<div id="solutionTitle">Suggested Solution:</div>
							<div id="solutionText"><c:out value = "${issue.resolution}" /></div>
						</div>
					</div>
						<c:if test="${not user.IT}">
							<div id ="solutionAccRej">
								<form action="Update" method="post">
									<input type="hidden" name="action" value="accept" />
									<input type="hidden" name="issueID" value="${issue.issueID}" />
									<input type="hidden" name="updated" value="issue" />
									<input class = "accRej" type="submit" name="accept" value="Accept" />
								</form>
								<form action="Update" method="post">
									<input type="hidden" name="action" value="reject" />
									<input type="hidden" name="issueID" value="${issue.issueID}" />
									<input type="hidden" name="updated" value="issue" />
									<input  class = "accRej" type="submit" name="accept" value="Reject" />
								</form>
							</div>
						</c:if>

		</c:if>
		<!-- if the issue is resolved display the solution -->
		<c:if test="${issue.state == 'Resolved'}">
			<div id = "solution">
				<div id="solutionTitle">Solution:</div>
				<div id="solutionText"><c:out value ="${issue.resolution}" /></div>
			</div>
		</c:if>
		<div id = "viewIssueComments">
			<div id = "viewIssueCommentHeader"><h3>Comments</h3></div>
			<!-- for each comment in the comment list print the comment and a reply box -->
				<c:forEach var = "comment" items = "${issue.comments}">
					<div class = "comment">
						<div class = "commentUserTime">
							<div class = "commentUser">User <c:out value = "${comment.userName}" /> posted:</div>
							<div class = "commentTime">Date Posted: <fmt:formatDate pattern="dd/MM/yy HH:mm:ss" value = "${comment.dateTime}" /></div> 
						</div> 
						<div class = "commentTextBox">
							<div class = "commentText"><c:out value="${comment.comment}" /></div>
						</div>
						<div class = "replyTextButton">
							<form action = "Update" method="post" onsubmit="return validateReply(${comment.commentID})"> <!-- reply must not be empty -->
								<input type="hidden" name="action" value="comment" />
								<input type="hidden" name="issueID" value="${issue.issueID}" />
								<input type="hidden" name="userName" value="${user.userName}" />
								<input type="hidden" name="parent" value="${comment.commentID}" />
								<c:if test = "${issue.knowledgeBaseArticle}">
									<input type="hidden" name="updated" value="article" />
								</c:if>
								<c:if test = "${not issue.knowledgeBaseArticle}">
									<input type="hidden" name="updated" value="issue" />
								</c:if>							
								<input type="text" id="replyTextIn${comment.commentID}" name="comment" placeholder="Reply..." class="replyText" />
								<input type="submit" value="Reply" class="replyButton" />
							</form>
						</div>
						<!-- if the user is it they are allowed to delete comments -->
						<c:if test="${user.IT}">
							<div class = "deleteTextButton">
								<form action = "Update" method="post">
									<input type="hidden" name="action" value="delete" />
									<input type="hidden" name="issueID" value="${issue.issueID}" />
									<input type="hidden" name="commentID" value="${comment.commentID}" />
									<input type="hidden" name="subComment" value="0" />
									<!-- this is just to tell the servlet if a kb article or issue was updated -->
									<c:if test = "${issue.knowledgeBaseArticle}">
										<input type="hidden" name="updated" value="article" />
									</c:if>
									<c:if test = "${not issue.knowledgeBaseArticle}">
										<input type="hidden" name="updated" value="issue" />
									</c:if>
									<input type="submit" value="Delete" class="deleteButton" />
								</form>
							</div>
						</c:if>
					</div>
					<!-- for each sub comment in the subcomments list for each comment, print the comment -->
					<c:forEach var = "subComment" items = "${comment.subComments}">
						<div class = "commentReply">
							<div class = "commentReplyUserTime">
								<div class = "commentReplyUser">User  <c:out value = "${subComment.userName}" /> replied:</div>
								<div class = "commentReplyTime">Date Posted: <fmt:formatDate pattern="dd/MM/yy HH:mm:ss" value = "${subComment.dateTime}" /></div>
							</div>
							<div class = "commentReplyTextBox">
								<div class = "commentReplyText">
									<c:out value="${subComment.comment}" />
								</div>
							</div>
							<!-- it users are allowed to delete sub comments -->
							<c:if test="${user.IT}">
								<div class = "deleteTextButton">
									<form action = "Update" method="post">
										<input type="hidden" name="action" value="delete" />
										<input type="hidden" name="issueID" value="${issue.issueID}" />
										<input type="hidden" name="commentID" value="${subComment.commentID}" />
										<input type="hidden" name="subComment" value="0" />
										<c:if test = "${issue.knowledgeBaseArticle}">
											<input type="hidden" name="updated" value="article" />
										</c:if>
										<c:if test = "${not issue.knowledgeBaseArticle}">
											<input type="hidden" name="updated" value="issue" />
										</c:if>
										<input type="submit" value="Delete" class="deleteButton" />
									</form>
								</div>
							</c:if>
						</div>
					</c:forEach>
				</c:forEach>
				<div class = "replyTextButton">
					<form action = "Update" method = "post" onsubmit="return validateComment()"> <!-- comment must not be empty -->
						<input type="hidden" name="action" value="comment" />
						<input type="hidden" name="issueID" value="${issue.issueID}" />
						<input type="hidden" name="userName" value="${user.userName}" />
						<input type="hidden" name="parent" value="-1" />
						<c:if test = "${issue.knowledgeBaseArticle}">
							<input type="hidden" name="updated" value="article" />
						</c:if>
						<c:if test = "${not issue.knowledgeBaseArticle}">
							<input type="hidden" name="updated" value="issue" />
						</c:if>							
						<input type="text" id="commentTextIn" name="comment" placeholder="Add a comment..." class="replyText" />
						<input type="submit" value="Add Comment" class="replyButton" />
					</form>
				</div>
		</div>
	</div>
</body>
</html>
