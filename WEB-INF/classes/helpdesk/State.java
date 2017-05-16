/*
	State.java
	
	This enum pertains to a State object
	within the Help Desk software project
	for SENG2050 assignment 3, 2016.
	
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

package helpdesk;

import java.io.Serializable;

public enum State implements Serializable
{
	NEW,			// The 'New' State, for issues that have yet to have staff interaction.
	IN_PROGRESS,	// The 'In Progress' State, for issues that are currently being worked on 
	COMPLETED,		// The 'Completed' State, for issues where a solution has been suggested but not yet accepted.
	RESOLVED;		// The 'Resolved' State, for issues where the solution has been accepted by the user.
}
