package com.saltlab.murun.utils;

import java.util.List;

/**
 * https://dl.acm.org/doi/pdf/10.1145/1831708.1831720
 * https://web.eecs.umich.edu/~weimerw/students/kinga-phd.pdf
 * Modeling Consumer-Perceived Web Application Fault Severities for Testing
 * @author rahulkrishna
 *
 */
public class BugLabels {
	private  boolean wrongPage = false; //Wrong Page/No Redirect. An unexpected page isloaded
	private  boolean missingInfo = false;; //Missing Information. Any part of a webpage that is missing, not including images
	private  boolean permission = false;; //Permission. Any errors occurring with respect to user permissions in an application, such as access being incorrectly denied to a user.
	private  boolean session = false;; //Session. An unexpected session timeout or other sessionrelated issues.
	private  boolean missingPhoto = false;; //Missing Image. A missing image. 
	private  boolean codeError = false;; //Form Error. Missing, malformed, or extra buttons, form fields, drop-down menus, etc, including incorrectly validating forms.
	// Also include incorrect event handling for generic elements.
	private  boolean cosmetic = false;; //Cosmetic. An error that does not affect the functionality of the website, such as a typo, small formatting issues, bits of visible HTML code, etc.
	private  boolean mathCalc = false;; //Arithmetic Calculation Error. Generally for shopping cart based applications, any error in calculating the amount paid, shipping, taxes, discount applied,quantities ordered, etc.
	private  boolean functionalDisplay = false;; //Blank Page. An empty page containing no information or text.
	private  boolean database = false;; //Database. Any errors associated with accessing or querying a database, including visible SQL code beingdisplayed.
	private  boolean codeDump = false;; //Code on the Screen. Any error that results in programming language code appear on screen, including any error referring to a line number (with the exception of visible HTML code).
	private  boolean authentication = false;; //Authentication. Any errors that occur during login
	private  boolean error404 = false;; //• 404 Error. An error experienced when the URL is notfound; the words “404” or “not found” must appear somewhere on the page.
	private  boolean search = false;; //Errors occurring during searching, such as incorrectly printing out results. 
	private  boolean upload = false;; //Failed Upload. An error during the upload of an item
	private  boolean css = false;; //Cascading Stylesheet Error. An error in loading the stylesheet between the current and next pages
	private  boolean language = false;; //• Language Error. An inability to encode or correctly convert characters between languages, usually resulting in incorrect characters on the page

	// Our categories
	private boolean missingElements, wrongElements, wrongElementState, brokenLink, brokenEventHandling, unExpectedElements, wrongSize, wrongColor, wrongPosition; 


	//Other Error/Error Message. Either any error message, or any error that cannot be classified in any other category
	public  double getSeverity()
	{
		double score = -1;

		if (wrongPage && missingInfo) {
			score = 2.5;
		} else if (permission && !session) {
			score = 2.5;
		}

		else if (missingPhoto && !codeError) {
			if (cosmetic)
				score = 0.5;
			else
				score = 2.5;
		} else if (mathCalc && codeError) {
			score = 2.5;
		} else if (functionalDisplay && codeError) {
			score = 2.5;
		} else if (database) {
			score = 2.5;
		}

		else if (codeError) {
			score = 2.5;
		}

		else if (codeDump) {
			score = 2.5;
		}

		else if (authentication) {
			if (permission)
				score = 3;
			else
				score = 2.5;
		} else if (error404) {
			score = 2.5;
		}

		else if (wrongPage) {
			score = 2;
		} else if (session) {
			score = 2;
		}

		else if (search && !cosmetic) {
			score = 2;
		}

		else if (missingInfo && !cosmetic) {
			score = 2;
		} else if (upload) {
			score = 1.5;
		}

		else if (missingPhoto && codeError) {
			score = 1.5;
		}

		else if (mathCalc && !cosmetic) {
			score = 1.5;
		}

		else if (functionalDisplay && !cosmetic) {
			score = 1.5;
		}

		else if (css) {
			score = 1.5;
		}

		else if (mathCalc && cosmetic) {
			score = 1;
		}

		else if (language) {
			score = 1;
		}

		else if (functionalDisplay && cosmetic) {
			score = 1;
		}

		else if (database && cosmetic) {
			score = 0.5;
		}

		else if (missingInfo && cosmetic) {
			score = 0.5;
		}

		else if (search && cosmetic) {
			score = 0.5;
		}

		else { // if no fault was seen
			score = 0.5;
		}

		if (cosmetic && score > 0.5) {
			score = 1;
		}
		return score;
	}
	
	public BugLabels(List<String> bugLabels) {
		if(bugLabels!=null) {
			for(String label: bugLabels) {
				switch(label.toLowerCase()) {
				case "none":
					break;
				case "wrongpage":
					wrongPage=true;
				case "missinginfo":
					missingInfo=true;
					break;
				case "permission": 
					permission=true;
					break;
				case "session" :
					session=true;
					break;
				case "missingphoto":
					missingPhoto = true;
					break;
				case "codeerror": 
					codeError = true;
					break;
				case "cosmetic":
					cosmetic = true;
					break;
				case "mathcalc": 
					mathCalc = true;
					break;
				case "blankpage":
					functionalDisplay = true;
					break;
				case "database":
					database =true;
					break;
				case "codedump":
					codeDump = true;
					break;
				case "authentication":
					authentication = true;
					break;
				case "error404":
					error404 = true;
					break;
				case "search":
					search = true;
					break;
				case "upload":
					upload =true;
					break;
				case "css":
					css = true;
					break;
				case "language":
					language = true;
					break;
			    default:
			    	break;
				}
			}
		}
	}
	
}
