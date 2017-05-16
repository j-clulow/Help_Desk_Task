/*
	SubCategory.java
	
	This enum pertains to a SubCategory object
	within the Help Desk software project
	for SENG2050 assignment 3, 2016.
	As specified in the Specifications Appendix 6.2
	
	Students responsible for this code are:
	Christopher O'Donnell:	3165328
	Jacob Clulow:			3164461
	George Edwards:			3167656
*/

package helpdesk;

import java.io.Serializable;

public enum SubCategory implements Serializable
{
	CANT_CONNECT,		// 'Can't Connect' subcategory within the 'Network' category. 
	SPEED,				// 'Speed' subcategory within the 'Network' category.
	DROPOUTS,			// 'Constant Dropouts' subcategory within the 'Network' category.
	LOAD_SLOW,			// 'Slow to Load' subcategory within the 'Software' category.
	NO_LOAD,			// 'Won't Load at All' subcategory within the 'Software' category.
	WONT_BOOT,			// 'Computer Won't Turn On subcategory within the 'Hardware' category.
	BSOD,				// 'Computer "Blue Screens"' subcategory within the 'Hardware' category.
	HDD,				// 'Disk Drive' subcategory within the 'Hardware' category.
	PERIPHERAL,			// 'Peripherals' subcategory within the 'Hardware' category.
	CANT_SEND,			// 'Can't Send subcategory within the 'Email' category.
	CANT_RECEIVE,		// 'Can't Recieve subcategory within the 'Email' category.
	SPAM,				// 'Spam/Phishing' subcategory within the 'Email' category.
	RESET_PASSWORD,		// 'Password Reset' subcategory within the 'Account' category.
	WRONG_DETAILS;		// 'Wrong details' subcategory within the 'Account' category.
}
