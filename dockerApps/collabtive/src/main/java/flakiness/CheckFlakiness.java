package flakiness;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.runner.Description;

import utils.Properties;
import utils.ResetAppState;

public class CheckFlakiness {

	public void check() {

		int numExecutionFlakyTestSuite = Integer
				.valueOf(Properties.getInstance().getProperty("num_execution_flaky_test_suite"));
		TestCaseExecutionListener testCaseExecutionListener = new TestCaseExecutionListener();
		TestCaseExecutor testCaseExecutor = new TestCaseExecutor();
		Map<Description, Set<String>> failures = new HashMap<>();
		boolean firstFailurePrinted = false;
		for (int i = 0; i < numExecutionFlakyTestSuite; i++) {
			int iteration = i;
			System.out.println("Iteration " + ++iteration + "/" + numExecutionFlakyTestSuite);
			ResetAppState.reset();
			testCaseExecutor.execute(testCaseExecutionListener);
			Optional<Description> failedTestDescriptionOptional = testCaseExecutionListener.getFailedTestDescription();
			if (failedTestDescriptionOptional.isPresent()) {
				Description failedTestDescription = failedTestDescriptionOptional.get();
				Set<String> tracesOfFailedTestCase = failures.get(failedTestDescription);
				if (tracesOfFailedTestCase == null) {
					tracesOfFailedTestCase = new HashSet<>();
				}
				Optional<String> failedTestTraceOptional = testCaseExecutionListener.getFailedTestTrace();
				if (failedTestTraceOptional.isPresent()) {
					if (!firstFailurePrinted) {
						firstFailurePrinted = true;
						System.out.println("First failure at iteration " + iteration);
					}
					tracesOfFailedTestCase.add(failedTestTraceOptional.get());
				} else {
					throw new IllegalStateException(
							"Test trace not present in test that failed " + failedTestDescription);
				}
				failures.put(failedTestDescription, tracesOfFailedTestCase);
			}
		}
		if (!failures.isEmpty()) {
			System.out.println(
					"Test suite is considered flaky. Below are the tests that failed with the relative stack trace");
			failures.keySet().forEach(key -> System.out.println("Description: " + key + "\n Traces: "
					+ failures.get(key).stream().collect(Collectors.joining("\n")) + "\n"));
		} else {
			System.out.println(
					"Test suite is considered not flaky because no test case failed while executing the test suite for "
							+ numExecutionFlakyTestSuite + " times");
		}
	}
}
