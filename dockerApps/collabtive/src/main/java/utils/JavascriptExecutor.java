package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavascriptExecutor {

    private org.openqa.selenium.JavascriptExecutor js;

    public JavascriptExecutor(WebDriver driver){
        this.js = (org.openqa.selenium.JavascriptExecutor) driver;
    }

    public void click(WebElement element) {
        this.js.executeScript("arguments[0].click()", element);
    }
}
