package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BasePageObject {

    public final Wait wait;
    public WebDriverWait waitFor;
    public Actions builder;
    protected WebDriver driver;
    protected int timeOut = 3;
    protected JavascriptActions jsActions;

    public BasePageObject(WebDriver driver) {
        this.driver = driver;
        this.waitFor = new WebDriverWait(driver, timeOut);
        this.builder = new Actions(driver);
        this.wait = new Wait(driver);
        this.jsActions = new JavascriptActions(driver);
    }

    public static String getTimeStamp() {
        Date time = new Date();
        long timeCurrent = time.getTime();
        return String.valueOf(timeCurrent);
    }

    /*--------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*@@@@@@@@@@@@@ ACTION @@@@@@@@@@@@@@*/
    // safely finding elements in actions -> for action I mean things performed on the page but after the preconditions are satisfied
    // if these actions are in the body of a PO method I assume that the locator is right and that the element the user is looking for exists in the page
    // if the element exists but the locator is wrong, in any case there is an exception thrown by Selenium

    public void sendKeys(By by, String input){
        WebElement element = this.findElement(by);
        this.jsActions.sendKeys(element, input);
    }

    public void typeJS(WebElement element, String input){
        this.jsActions.sendKeys(element, input);
    }

    public void typeJS(By locator, String input){
        WebElement element = this.findElementSafely(locator);
        this.jsActions.sendKeys(element,input);
    }

    public void type(WebElement element, String input){
        element.clear();
        element.sendKeys(input);
    }


    public void typeWithoutClear(By locator, String input){
        WebElement element = this.findElementSafely(locator);
        element.sendKeys(input);
    }

    public void typeWithoutClear(WebElement element, String input){
        element.sendKeys(input);
    }

    public void type(By locator, String input){
        WebElement element = this.findElementSafely(locator);
        element.clear();
        element.sendKeys(input);
    }

    public void pressKeyboardEnter(WebElement element){
        element.sendKeys(Keys.ENTER);
    }

    public void pressKeyboardEnter(By locator){
        this.findElement(locator).sendKeys(Keys.ENTER);
    }

    public void focusOn(By locator){
        WebElement element = this.findElementSafely(locator);
        this.jsActions.focusOnElement(element);
    }

    public void mouseOver(By locator){
        Actions builder = new Actions(this.driver);
		WebElement webElement = this.findElement(locator);
		builder.moveToElement(webElement).perform();
    }

    public void clickOnSelenium(By locator){
        WebElement element = this.findElementSafely(locator);
        element.click();
    }

    public void clickOnSelenium(WebElement element){
        element.click();
    }


    public void click(WebElement element){
        this.jsActions.click(element);
    }

    public void click(By locator){
        WebElement element = this.findElementSafely(locator);
        this.jsActions.click(element);
    }

    public void selectOptionInDropdown(By locatorDropdown, String optionToSelectByStringValue){
        WebElement dropdown = this.findElementSafely(locatorDropdown);
        this.jsActions.selectOptionInDropdown(dropdown, optionToSelectByStringValue);
    }

    public void selectOptionInDropdown(By locatorDropdown, String optionToSelectByStringValue, String xpathExpression){
        WebElement dropdown = this.findElementSafely(locatorDropdown);
        List<WebElement> links = this.findElementsJSByXPathStartingFrom(dropdown, xpathExpression);
        for(WebElement link: links){
            String linkText = this.getText(link);
            if(linkText.equals(optionToSelectByStringValue)){
                this.click(link);
                break;
            }
        }
    }

    public boolean isOptionPresentInDropdown(By locatorDropdown, String optionToSelectByStringValue){
        WebElement dropdown = this.findElement(locatorDropdown);
        return this.jsActions.isOptionPresentInDropdown(dropdown, optionToSelectByStringValue);
    }


    /**
     * Simple method for checking if element is on page or not. No Ajax involved.
     */
    public boolean isElementPresentOnPage(By by) {
        return this.findElements(by).size() > 0;
    }

    /**
     * If the element exists in the page it returns it (returns the first element more elements exist)
     * Otherwise it returns null
     * */
    public WebElement elementPresentOnPage(By by){
        List<WebElement> elements = this.findElements(by);
        if(elements.size() > 0) return elements.get(0);
        return null;
    }

    public String getWebElementText(By locator){
        return this.findElement(locator).getText();
    }

    public String getAttribute(WebElement element, String attributeName){
        return this.jsActions.getAttribute(element,attributeName);
    }

    public String getText(WebElement element){
        return this.jsActions.getText(element);
    }

    public String getValue(WebElement element){
        return this.jsActions.getValue(element);
    }

    /*@@@@@@@@@@@@@ LOCALIZATION @@@@@@@@@@@@@@*/

    /**
     *  this method should not be called by methods on preconditions
     * should be called only by methods inside the body of a PO
     * */
    public WebElement findElementSafely(By locator){
        return this.getElementOnPageAfterWait(locator);
    }

    public WebElement findElement(By locator){
        return this.driver.findElement(locator);
    }

    public WebElement findElementStartingFrom(WebElement element, By locator){
        return element.findElement(locator);
    }

    public List<WebElement> findElementsStartingFrom(WebElement element, By locator){
        return element.findElements(locator);
    }

    public WebElement findElementJSByXPathStartingFrom(WebElement element, String xpathExpression){
        return this.jsActions.findElementByXPath(element, xpathExpression);
    }

    public List<WebElement> findElementsJSByXPathStartingFrom(WebElement element, String xpathExpression){
        return this.jsActions.findElementsByXPath(element, xpathExpression);
    }

    public List<WebElement> findElements(By locator) {
        return this.driver.findElements(locator);
    }

    public WebElement findElementJS(String xpathExpression){
        return this.jsActions.findElementByXPath(xpathExpression);
    }

    public List<WebElement> findElementsJS(String xpathExpression){
        return this.jsActions.findElementsByXPath(xpathExpression);
    }

    /*@@@@@@@@@@@@@ WAIT @@@@@@@@@@@@@@*/

    /**
     * Method to check if WebElement is displayed on the page. The element is present but not visible.
     * It may turn visible according to some Ajax actions.
     *
     * @return true if element is displayed, otherwise return false
     */
    public boolean waitForElementBeingVisibleOnPage(By locator) {
        try {
            WebElement element = findElement(locator);
            wait.forElementBeingVisible(element);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Method to check until a certain WebElement disappears from the page.
     * The element may be present or not. It disappears after some time.
     *
     * @return true if element is present, otherwise return false
     */
    public boolean waitForElementBeingInvisibleOnPage(By locator){
        try{
            wait.forElementBeingInvisible(locator);
            return true;
        }catch(TimeoutException e){
            return false;
        }
    }

    /**
     * Method to check if WebElement is present on the page. The element is not present.
     * It may turn present according to some Ajax actions.
     *
     * @return true if element is present, otherwise return false
     */
    public boolean waitForElementBeingPresentOnPage(By locator){
        try{
            wait.forElementBeingPresentPageLoadingTimeout(locator);
            return true;
        }catch(TimeoutException e){
            return false;
        }
    }

    /**
     * Method to check if WebElement is present on the page. The element is not present.
     * It may turn present according to some Ajax actions.
     *
     * @return true if element is present, otherwise return false
     */
    public boolean waitForElementBeingPresentOnPage(By locator, long timeout, TimeUnit unit){
        try{
            wait.forElementBeingPresentCustomTimeout(locator, timeout, unit);
            return true;
        }catch(TimeoutException e){
            return false;
        }
    }

    /**
     * Method to check if WebElement is present on the page. The element is not present.
     * It may turn present according to some Ajax actions.
     *
     * @return true if element is present, otherwise return false
     */
    public boolean waitForElementBeingPresentOnPage(By locator, long timeout){
        try{
            wait.forElementBeingPresentCustomTimeout(locator, timeout, TimeUnit.MILLISECONDS);
            return true;
        }catch(TimeoutException e){
            return false;
        }
    }

    public boolean waitForElementWhosePropertyDisappears(WebElement webElement, String attributeName, String defaultValueAttribute) {
        try {
            wait.forElementWhosePropertyDisappears(webElement, attributeName, defaultValueAttribute);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementWhosePropertyDisappears(By locator, String attributeName, String defaultValueAttribute) {
        try {
            wait.forElementWhosePropertyDisappears(locator, attributeName, defaultValueAttribute);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementThatChangesProperty(WebElement webElement, String attributeName, String expectedValueAttribute) {
        try {
            wait.forElementThatChangesProperty(webElement, attributeName, expectedValueAttribute);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementThatChangesProperty(By locator, String attributeName, String expectedValueAttribute) {
        try {
            wait.forElementThatChangesProperty(locator, attributeName, expectedValueAttribute);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementThatChangesText(By locator, boolean textExpected, String text) {
        try {
            wait.forElementThatChangesText(locator, textExpected, text);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void waitForTimeoutExpires(long timeoutMillis){
        wait.forTimeoutExpires(timeoutMillis);
    }

    public WebElement getElementOnPageAfterWait(By locator){
        return wait.forElementBeingPresentDefaultTimeout(locator);
    }

    //to be tested in ES
    public boolean waitForElementBeingClickable(By locator) {
        try {
            wait.forElementBeingClickable(locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /*@@@@@@@@@@@@@ OTHER @@@@@@@@@@@@@@*/

    public WebDriver getDriver(){
        return this.driver;
    }

    public void acceptAlert(){
        this.driver.switchTo().alert().accept();
    }

    public void dismissAlert(){
        this.driver.switchTo().alert().dismiss();
    }
}
