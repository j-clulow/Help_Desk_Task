// INPUT VALIDATION FOR A PROPOSED SOLUTION
function validateSuggestion(){
	var suggestion = document.getElementById("suggestionInput").value;
	
	var outputMessage = "";
	var valid = true;
	
	if(suggestion == ""){
		valid = false;
		outputMessage += "Suggestion cannot be blank."
	}

	if(!valid){
		alert(outputMessage);
	}

	return valid;
}

// INPUT VALIDATION FOR A COMMENT REPLY
function validateReply(id){
	var s = "replyTextIn" + id;
	var reply = document.getElementById(s).value;

	var outputMessage = "";
	var valid = true;

	if(reply == ""){
		valid = false;
		outputMessage += "Reply cannot be blank."
	}

	if(!valid){
		alert(outputMessage);
	}

	return valid;
}

// INPUT VALIDATION FOR A COMMENT
function validateComment(){
	var comment = document.getElementById("commentTextIn").value;

	var outputMessage = "";
	var valid = true;

	if(comment == ""){
		valid = false;
		outputMessage += "Comment cannot be blank."
	}

	if(!valid){
		alert(outputMessage);
	}

	return valid;
}

// INPUT VALIDATION FOR INITIAL CREDENTIALS (Login page)
function validateCreds(){
	var user = document.getElementById("username").value;
	var pass = document.getElementById("pass").value;

	var outputMessage = "";
	var valid = true;

	if(user == "" || pass == ""){
		valid = false;
		outputMessage += "Username/password cannot be blank."
	}

	if(!valid){
		alert(outputMessage);
	}

	return valid;
}

// INPUT VALIDATION FOR REPOSTING A NEW ISSUE
function validateReport(){
	var title = document.getElementById("issueTitle").value;
	var desc = document.getElementById("issueDesc").value;
	var cat = document.getElementById("categoryBox").value;
	var tags = document.getElementById("tagBox").value;
	
	var outputMessage = "";
	var valid = true;
	
	if(title == "" || desc == ""){
		valid = false;
		outputMessage += "Title/Description cannot be blank.\n"
	}
	
	if(cat == "default"){
		valid = false;
		outputMessage += "Issue requires a category."
	}

	if(!valid){
		alert(outputMessage);
	}

	return valid;
}

// INPUT VALIDATION FOR SEARCHING A KNOWLEDGE BASE ARTICLE
function validateKBSearch(){
	var search = document.getElementById("searchTerm").value;
	
	var outputMessage = "";
	var valid = true;

	if(search == ""){
		valid = false;
		outputMessage += "Search cannot be blank.\n"
	}

	if(!valid){
		alert(outputMessage);
	}

	return valid;
}

// INPUT VALIDATION FOR SEARCHING A KNOWLEDGE BASE ARTICLE
// This one is because a separate page can also be used to search
// the knowledge base and they have different <div> ids.
function validateKBSearch2(){
	var search = document.getElementById("KBSearchTerm").value;
	
	var outputMessage = "";
	var valid = true;

	if(search == ""){
		valid = false;
		outputMessage += "Search cannot be blank.\n"
	}

	if(!valid){
		alert(outputMessage);
	}

	return valid;
}

// INPUT VALIDATION FOR ENTERING A MAINTENANCE ITEM
function validateMaint(){
	var maint = document.getElementById("maintString").value;

	var outputMessage = "";
	var valid = true;

	if(maint == ""){
		valid = false;
		outputMessage += "Maintenance Message cannot be blank."
	}

	if(maint.length > 512){
		valid =false;
		outputMessage += "Maintenance message cannot be longer than 512 characters."
	}

	if(!valid){
		alert(outputMessage);
	}

	return valid;
}
