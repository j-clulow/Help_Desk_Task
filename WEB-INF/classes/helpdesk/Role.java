/*
	Role.java
	
	This enum pertains to a Role object
	within the Help Desk software project
	for SENG2050 assignment 3, 2016.
	
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

package helpdesk;

import java.io.Serializable;

public enum Role implements Serializable
{
	USER,	// The general user role, for all users that are not IT.
	IT;		// The IT staff user role, for all IT Staff members.
}
