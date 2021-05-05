package flakiness;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.Optional;

public class TestCaseExecutionListener extends RunListener {

    private Description failedTestDescription;
    private String failedTestTrace;
    private boolean testFailed = false;

    public TestCaseExecutionListener(){
    }

    @Override
    public void testStarted(Description description) {
    }

    @Override
    public void testFailure(Failure failure) {
        if(!testFailed){
            testFailed = true;
            this.failedTestDescription = failure.getDescription();
            this.failedTestTrace = failure.getTrace();
        }
    }

    public Optional<Description> getFailedTestDescription() {
        if(this.failedTestDescription != null)
            return Optional.of(this.failedTestDescription);
        return Optional.empty();
    }

    public Optional<String> getFailedTestTrace() {
        if(this.failedTestTrace != null)
            return Optional.of(this.failedTestTrace);
        return Optional.empty();
    }
}
