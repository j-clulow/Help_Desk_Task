/*
	Category.java
	
	This enum pertains to a Category object
	within the Help Desk software project
	for SENG2050 assignment 3, 2016.
	
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

package helpdesk;

import java.io.Serializable;

public enum Category implements Serializable
{
	NETWORK,		// 'Network' category as per the specifications.
	SOFTWARE,		// 'Software' category as per the specifications.
	HARDWARE,		// 'Hardware' category as per the specifications.
	EMAIL,			// 'Email' category as per the specifications.
	ACCOUNT;		// 'Account' category as per the specifications.
}
