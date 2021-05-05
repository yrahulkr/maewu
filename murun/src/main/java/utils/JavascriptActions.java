package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Set of commonly used actions invoked by executing JavaScript on a web page
 */
public class JavascriptActions {

    private final JavascriptExecutor js;

    public JavascriptActions(WebDriver driver) {
        this.js = (JavascriptExecutor) driver;
    }

    public WebElement findElementByXPath(String xpathExpression){
        WebElement el = (WebElement) js.executeScript("return document" +
                ".evaluate('" + xpathExpression + "',document, null, XPathResult.FIRST_ORDERED_NODE_TYPE,null).singleNodeValue");
        return el;
    }

    public WebElement findElementByXPath(WebElement element, String xpathExpression){
        WebElement el = (WebElement) js.executeScript("return document" +
                ".evaluate('" + xpathExpression + "',arguments[0], null, XPathResult.FIRST_ORDERED_NODE_TYPE,null).singleNodeValue", element);
        return el;
    }

    public List<WebElement> findElementsByXPath(String xpathExpression){
        Long numberOfMatches = (Long) js.executeScript("return document" +
                ".evaluate('count(" + xpathExpression + ")', document, null, XPathResult.NUMBER_TYPE,null).numberValue");
        List<WebElement> elements = new ArrayList<WebElement>();
        for (int i = 0; i < numberOfMatches; i++) {
            elements.add((WebElement) js.executeScript("return document" +
                    ".evaluate('" + xpathExpression + "',document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null).snapshotItem(" + i + ")"));
        }
        return elements;
    }

    public List<WebElement> findElementsByXPath(WebElement element, String xpathExpression){
        Long numberOfMatches = (Long) js.executeScript("return document" +
                ".evaluate('count(" + xpathExpression + ")', arguments[0], null, XPathResult.NUMBER_TYPE,null).numberValue", element);
        List<WebElement> elements = new ArrayList<WebElement>();
        for (int i = 0; i < numberOfMatches; i++) {
            elements.add((WebElement) js.executeScript("return document" +
                    ".evaluate('" + xpathExpression + "', arguments[0], null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null).snapshotItem(" + i + ")", element));
        }
        return elements;
    }

    public void click(WebElement element) {
        js.executeScript("arguments[0].click()", element);
    }

    public void sendKeys(WebElement element, String text){
        js.executeScript("arguments[0].value='" + text + "'", element);
    }

    public void sendKeys(WebElement element, CharSequence text){
        js.executeScript("arguments[0].value='" + text.subSequence(0, text.length()) + "'", element);
    }

    public void focusOnElement(WebElement element){
        js.executeScript("arguments[0].focus();", element);
    }

    public String getAttribute(WebElement element, String attributeName){
        String attribute = (String) js.executeScript("return arguments[0].getAttribute('" + attributeName + "')",element);
        return attribute.trim();
    }

    public String getText(WebElement element){
        String text = (String) js.executeScript("return arguments[0].textContent", element);
        return text.trim();
    }

    public String getValue(WebElement element){
        String text = (String) js.executeScript("return arguments[0].value", element);
        return text.trim();
    }

    public void selectOptionInDropdown(WebElement dropdown, String optionToSelectByStringValue){
        List<WebElement> options = (List<WebElement>) js.executeScript("return arguments[0].options", dropdown);
        for(WebElement option: options){
            String optionText = this.getText(option);
            if(optionText.equals(optionToSelectByStringValue)){
                option.click();
                break;
            }
        }
    }

    public boolean isOptionPresentInDropdown(WebElement dropdown, String optionToSelectByStringValue){
        List<WebElement> options = (List<WebElement>) js.executeScript("return arguments[0].options", dropdown);
        for(WebElement option: options){
            if(option.getText().equals(optionToSelectByStringValue)){
                return true;
            }
        }
        return false;
    }

    /**
     *  initKeyEvent for Firefox
     * initKeyboardEvent for IE9+, Chrome and Safari
     * Send key '13' (= enter)
     * For details: https://stackoverflow.com/questions/3276794/jquery-or-pure-js-simulate-enter-key-pressed-for-testing
     * */
    public void pressKeyEnterOnElement(WebElement element){
        js.executeScript("var ev = document.createEvent('KeyboardEvent'); " +
                "ev.initKeyboardEvent(\'keydown\', true, true, window, false, false, false, false, 13, 0);",element);
    }


}
