package com.saltlab.murun.aspects;

import java.io.File;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.saltlab.murun.runner.MutationRunner;
import com.saltlab.murun.runner.TraceSession;
import com.saltlab.murun.utils.Settings;
import com.saltlab.murun.utils.UtilsAspect;
import com.saltlab.murun.utils.UtilsComputerVision;
import com.saltlab.murun.utils.UtilsParser;

@Aspect
public class AspectInstrumenter {

	static WebDriver d;
	static String testFolder;
	static String mainPage;

	/* statement information. */
	static String statementName;
	static int line;

	/* visual information. */
	static String screenshotBefore;
	static String annotatedScreenshot;
	static String visualLocator;

	/* DOM information. */
	static String htmlPath;
	static String domInfoJsonFile;
	private boolean mutationTest;

	/* OpenCV bindings. */
//	static {
//		nu.pattern.OpenCV.loadShared();
//		nu.pattern.OpenCV.loadLocally();
//	}

	/* Pointcuts definition. */

	/* intercept the calls to findElement methods. */
	@Pointcut("call(* org.openqa.selenium.WebDriver.findElement(..))")
	public void logFindElementCalls(JoinPoint jp) {
	}

	
	
	/* intercept the executions of findElement methods. */
	@Pointcut("execution(* org.openqa.selenium.WebDriver.findElement(..))")
	public void catchFindElementExecutions(JoinPoint jp) {
		
	}

	@Pointcut("call(* org.openqa.selenium.WebDriver.get(..))")
	public void logNavigationCommands(JoinPoint jp) {
	}
	
	/* intercept the calls to WebElement methods. */
	@Pointcut("call(* org.openqa.selenium.WebElement.click()) " 
//				+ "call(* org.openqa.selenium.WebDriver.get(..)) ||"
//				+ "call(* org.openqa.selenium.Alert.accept())"
)
//	|| "
//			+ "call(* org.openqa.selenium.WebElement.sendKeys(..)) || "
//			+ "call(* org.openqa.selenium.WebElement.getText()) || "
//			+ "call(* org.openqa.selenium.WebElement.clear()) || "
//			+ "call(* org.openqa.selenium.support.ui.Select.selectByVisibleText(..))")
	public void logSeleniumCommands(JoinPoint jp) {
	}

	/* create output folders before calling the method. */
	@Before("logNavigationCommands(JoinPoint)")
	public void loggingAdvice(JoinPoint jp) {

		if (Settings.aspectActive) {

			/*
			 * IMPORTANT: it is NOT possible to capture web element in this aspect, lead to
			 * infinite recursive calls.
			 */
			mutationTest = false;
			d = (WebDriver) jp.getTarget();
			
			String withinType = jp.getStaticPart().getSourceLocation().getWithinType().toString();
			String testSuiteName = UtilsParser.getTestSuiteNameFromWithinType(withinType);
			
			String testSuiteFolder = new File(Settings.outputDir, testSuiteName).getAbsolutePath(); 
			UtilsAspect.createTestFolder(testSuiteFolder);

			testFolder = new File(testSuiteFolder, 
					 jp.getStaticPart().getSourceLocation().getFileName().replace(Settings.JAVA_EXT, "")).getAbsolutePath();

			if(!Settings.mutationRun)
				UtilsAspect.createTestFolder(testFolder);

		}

	}
	
	/* save DOM and visual information before executing the method. */
	@After("logNavigationCommands(JoinPoint)")
	public void afterNav(JoinPoint joinPoint) {
		if(Settings.aspectActive) {

			statementName = UtilsAspect.getStatementNameFromJoinPoint(joinPoint);
			
			if(alertPresent(d)) {
				System.out.println("Ignoring statement because of alert : " + statementName);
				return;
			}
			
			if(Settings.mutationRun) {
				if(MutationRunner.getInstance().shouldMutate(statementName)) {
					mutationTest = true;
					MutationRunner.getInstance().mutateBrowserState(d, statementName);
					UtilsAspect.createTestFolder(testFolder);
				}
				if(!mutationTest) // Peformance : No need to capture this state
					return;
			}
			
			line = UtilsAspect.getStatementLineFromJoinPoint(joinPoint);
			
			screenshotBefore = testFolder + Settings.sep + line + Settings.PNG_EXT;
			UtilsComputerVision.saveScreenshot(d, screenshotBefore);

			htmlPath = testFolder + Settings.sep + line + Settings.HTML_EXT;
			UtilsAspect.saveDOM(d, htmlPath);

			if(Settings.USE_CRAWLJAX) {
				UtilsAspect.registerNav(statementName);
			}
			
			if (Settings.VERBOSE)
				System.out.println("[LOG]\t " + statementName);

		
		}
	}
	
	/* save DOM and visual information before executing the method. */
	@Before ("logSeleniumCommands(JoinPoint)")
	public void beforeEvent(JoinPoint joinPoint) {
		if (Settings.aspectActive) {
			
			WebElement we = null;
			Select sel = null;

			if (joinPoint.getTarget() instanceof WebElement) {
				we = (WebElement) joinPoint.getTarget();
			} else if (joinPoint.getTarget() instanceof Select) {
				sel = (Select) joinPoint.getTarget();
				we = (WebElement) sel.getOptions().get(0);
			}
			if(Settings.USE_CRAWLJAX) {
				UtilsAspect.registerEventBefore(UtilsAspect.getStatementNameFromJoinPoint(joinPoint), we);
			}
		}
		
	}
	
	@After ("logFindElementCalls(JoinPoint)")
	public void findElementCalled(JoinPoint joinPoint) {
		
		if(Settings.aspectActive) {
			
			Object[] args = joinPoint.getArgs();
			System.out.println(args[0]);
			/*
			switch(args[0].getClass().getSimpleName().toLowerCase()) {
			case "byid":
				System.out.println("id");
				break;
			case "byname":
				System.out.println("name");
				break;
			case "byxpath":
				System.out.println("xpath");
				break;
			default:
				System.out.println("unhandled : " + args[0].getClass().getSimpleName());
			}
			*/
//			Object a = joinPoint.getThis();
//			System.out.println(target);
//			System.out.println(a);
			
			if(Settings.USE_CRAWLJAX) {
				TraceSession.getInstance().recordFindElement(UtilsAspect.getStatementNameFromJoinPoint(joinPoint), args[0].toString());
			}
		}
	}
	

	/* save DOM and visual information before executing the method. */
	@After ("logSeleniumCommands(JoinPoint)")
	public void afterEvent(JoinPoint joinPoint) {

		if (Settings.aspectActive) {
			
			/*WebElement we = null;
			Select sel = null;

			if (joinPoint.getTarget() instanceof WebElement) {
				we = (WebElement) joinPoint.getTarget();
			} else if (joinPoint.getTarget() instanceof Select) {
				sel = (Select) joinPoint.getTarget();
				we = (WebElement) sel.getOptions().get(0);
			}
			
			*/

			statementName = UtilsAspect.getStatementNameFromJoinPoint(joinPoint);
			
			if(alertPresent(d)) {
				System.out.println("Ignoring statement because of alert : " + statementName);
				return;
			}
			
			if(Settings.mutationRun) {
				if(MutationRunner.getInstance().shouldMutate(statementName)) {
					mutationTest = true;
					MutationRunner.getInstance().mutateBrowserState(d, statementName);
					UtilsAspect.createTestFolder(testFolder);
				}
				if(!mutationTest) // Peformance : No need to capture this state
					return;
			}
			
			line = UtilsAspect.getStatementLineFromJoinPoint(joinPoint);
			
			screenshotBefore = testFolder + Settings.sep + line + Settings.PNG_EXT;

			htmlPath = testFolder + Settings.sep + line + Settings.HTML_EXT;

			if(Settings.USE_CRAWLJAX) {
				UtilsAspect.registerEventAfter(d, statementName, screenshotBefore, htmlPath);
			}else {
				UtilsComputerVision.saveScreenshot(d, screenshotBefore);
				UtilsAspect.saveDOM(d, htmlPath);
			}
			
			if (Settings.VERBOSE)
				System.out.println("[LOG]\t " + statementName);

		}
	}
	
	

	private boolean alertPresent(WebDriver d2) {
		try {
			d2.switchTo().alert();
			return true;
		}catch(Exception ex) {
			return false;
		}
	}

	// @After("logSeleniumCommands(JoinPoint)")
	// public void afterEvent(JoinPoint joinPoint) {
	//
	// if (Settings.aspectActive) {
	//
	// /* for each statement, get a unique name and the line number. */
	// String statementName = UtilsAspect.getStatementNameFromJoinPoint(joinPoint);
	// int line = UtilsAspect.getStatementLineFromJoinPoint(joinPoint);
	//
	// if (Settings.VERBOSE)
	// System.out.println("[LOG]\t@After " + statementName);
	//
	// /* save the screenshot before the execution of the event. */
	// String screenshotBefore = testFolderName + Settings.separator + line +
	// "-2after-" + statementName
	// + Settings.PNG_EXTENSION;
	//
	// /* save the HTML page. */
	// String htmlPath = testFolderName + Settings.separator + line + "-2after-" +
	// statementName;
	//
	// if (UtilsComputerVision.isAlertPresent(d)) {
	// return;
	// } else {
	//
	// try {
	// UtilsAspect.saveHTMLPage(d.getCurrentUrl(), htmlPath);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// UtilsComputerVision.saveScreenshot(d, screenshotBefore);
	// }
	//
	// }
	//
	// }
	//
	// @AfterThrowing(pointcut = "logFindElementCalls(JoinPoint)", throwing =
	// "exception")
	// public void logAfterThrowing(Exception exception, JoinPoint joinPoint) {
	//
	// if (Settings.aspectActive) {
	//
	// /* for each statement, get a unique name. */
	// String statementName = UtilsAspect.getStatementNameFromJoinPoint(joinPoint);
	//
	// /* for each statement, get the line number. */
	// int line = UtilsAspect.getStatementLineFromJoinPoint(joinPoint);
	//
	// /*
	// * get screenshot of the page before the action is executed, but after the
	// * exception has been raised.
	// */
	// String screenshotBefore = testFolderName + Settings.separator + line +
	// "-Annotated-" + statementName
	// + Settings.PNG_EXTENSION;
	// UtilsComputerVision.saveScreenshot(d, screenshotBefore);
	//
	// /* save the HTML page. */
	// String htmlPath = testFolderName + Settings.separator + line + "-2after-" +
	// statementName;
	// try {
	// UtilsAspect.saveHTMLPage(d.getCurrentUrl(), htmlPath);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// if (Settings.VERBOSE)
	// System.out.println("[LOG]\t@AfterThrowing " + statementName);
	//
	// }
	// }

}
