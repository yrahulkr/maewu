package flakiness;

import main.TestSuite;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

public class TestCaseExecutor {

    public void execute(RunListener runListener){
        JUnitCore core = new JUnitCore();
        core.addListener(runListener);
        core.run(TestSuite.class);
    }
}
