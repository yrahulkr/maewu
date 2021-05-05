package utils;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;

/**
 * A specialization of {@link FluentWait} that uses WebDriver instances.
 */
public class CustomWebDriverWait extends FluentWait<WebDriver> {

    public final static long DEFAULT_SLEEP_TIMEOUT = 500;
    private final WebDriver driver;

    /**
     * Wait will ignore instances of NotFoundException that are encountered (thrown) by default in
     * the 'until' condition, and immediately propagate all others.  You can add more to the ignore
     * list by calling ignoring(exceptions to add).
     *
     * @param driver The WebDriver instance to pass to the expected conditions
     * @param timeOutInSeconds The timeout in seconds when an expectation is called
     * @see CustomWebDriverWait#ignoring(java.lang.Class)
     */
    public CustomWebDriverWait(WebDriver driver, long timeOutInSeconds) {
        this(driver, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER, timeOutInSeconds, DEFAULT_SLEEP_TIMEOUT);
    }

    /**
     * Wait will ignore instances of NotFoundException that are encountered (thrown) by default in
     * the 'until' condition, and immediately propagate all others.  You can add more to the ignore
     * list by calling ignoring(exceptions to add).
     *
     * @param driver The WebDriver instance to pass to the expected conditions
     * @param timeOut The timeout when an expectation is called
     * @param unit Units of the timeout: ex. SECONDS, MILLISECONDS
     * @see CustomWebDriverWait#ignoring(java.lang.Class)
     */
    public CustomWebDriverWait(WebDriver driver, long timeOut, TimeUnit unit) {
        this(driver, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER, timeOut, unit, DEFAULT_SLEEP_TIMEOUT);
    }

    /**
     * Wait will ignore instances of NotFoundException that are encountered (thrown) by default in
     * the 'until' condition, and immediately propagate all others.  You can add more to the ignore
     * list by calling ignoring(exceptions to add).
     *
     * @param driver The WebDriver instance to pass to the expected conditions
     * @param timeOutInSeconds The timeout in seconds when an expectation is called
     * @param sleepInMillis The duration in milliseconds to sleep between polls.
     * @see CustomWebDriverWait#ignoring(java.lang.Class)
     */
    public CustomWebDriverWait(WebDriver driver, long timeOutInSeconds, long sleepInMillis) {
        this(driver, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER, timeOutInSeconds, sleepInMillis);
    }

    /**
     * Wait will ignore instances of NotFoundException that are encountered (thrown) by default in
     * the 'until' condition, and immediately propagate all others.  You can add more to the ignore
     * list by calling ignoring(exceptions to add).
     *
     * @param driver The WebDriver instance to pass to the expected conditions
     * @param timeOut The timeout when an expectation is called
     * @param unit Units of the timeout: ex. SECONDS, MILLISECONDS
     * @param sleepInMillis The duration in milliseconds to sleep between polls.
     * @see CustomWebDriverWait#ignoring(java.lang.Class)
     */
    public CustomWebDriverWait(WebDriver driver, long timeOut, TimeUnit unit, long sleepInMillis) {
        this(driver, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER, timeOut, unit, sleepInMillis);
    }

    /**
     * @param driver The WebDriver instance to pass to the expected conditions
     * @param clock The clock to use when measuring the timeout
     * @param sleeper Object used to make the current thread go to sleep.
     * @param timeOutInSeconds The timeout in seconds when an expectation is
     * @param sleepTimeOut The timeout used whilst sleeping. Defaults to 500ms called.
     */
    public CustomWebDriverWait(WebDriver driver, Clock clock, Sleeper sleeper, long timeOutInSeconds,
                               long sleepTimeOut) {
        super(driver, clock, sleeper);
//        withTimeout(timeOutInSeconds, TimeUnit.SECONDS);
//        pollingEvery(sleepTimeOut, TimeUnit.MILLISECONDS);
        withTimeout(Duration.ofSeconds(timeOutInSeconds));
        pollingEvery(Duration.ofMillis(sleepTimeOut));
        ignoring(NotFoundException.class);
        this.driver = driver;
    }

    /**
     * @param driver The WebDriver instance to pass to the expected conditions
     * @param clock The clock to use when measuring the timeout
     * @param sleeper Object used to make the current thread go to sleep.
     * @param timeOut The timeout when an expectation is called
     * @param unit Units of the timeout: ex. SECONDS, MILLISECONDS
     * @param sleepTimeOut The timeout used whilst sleeping. Defaults to 500ms called.
     */
    public CustomWebDriverWait(WebDriver driver, Clock clock, Sleeper sleeper, long timeOut, TimeUnit unit,
                               long sleepTimeOut) {
        super(driver, clock, sleeper);
        withTimeout(Duration.ofMillis(timeOut));
        pollingEvery(Duration.ofMillis(sleepTimeOut));
        ignoring(NotFoundException.class);
        this.driver = driver;
    }

    @Override
    protected RuntimeException timeoutException(String message, Throwable lastException) {
        TimeoutException ex = new TimeoutException(message, lastException);
        ex.addInfo(WebDriverException.DRIVER_INFO, driver.getClass().getName());
        if (driver instanceof RemoteWebDriver) {
            RemoteWebDriver remote = (RemoteWebDriver) driver;
            if (remote.getSessionId() != null) {
                ex.addInfo(WebDriverException.SESSION_ID, remote.getSessionId().toString());
            }
            if (remote.getCapabilities() != null) {
                ex.addInfo("Capabilities", remote.getCapabilities().toString());
            }
        }
        throw ex;
    }
}