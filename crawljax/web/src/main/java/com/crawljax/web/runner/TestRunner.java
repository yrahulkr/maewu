package com.crawljax.web.runner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.slf4j.MDC;

import com.crawljax.web.LogWebSocketServlet;
import com.crawljax.web.model.TestRecord;
import com.crawljax.web.model.TestRecord.TestStatusType;
import com.crawljax.web.model.TestRecords;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javaxtools.compiler.CharSequenceCompiler;
import javaxtools.compiler.CharSequenceCompilerException;

@Singleton
public class TestRunner {
	private static final int WORKERS = 2;
	private final TestRecords testRecords;
	private final ObjectMapper mapper;
	private final ExecutorService pool;
	
	// for secure package name
    private static final Random random = new Random();
	private static final String PACKAGE_NAME = "generated";
	
	// for unique class names
    private int classNameSuffix = 0;
    
	// Create a CharSequenceCompiler instance which is used to compile
    // expressions into Java classes which are then used to create the XY plots.
    // The -target 1.5 options are simply an example of how to pass javac
    // compiler
    // options (the generated source in this example is Java 1.5 compatible.)
    private final CharSequenceCompiler<Object> compiler = new CharSequenceCompiler<Object>(
            getClass().getClassLoader(), new ArrayList<String>());

	@Inject
	public TestRunner(TestRecords testRecords,
	        ObjectMapper mapper) {
		this.testRecords = testRecords;
		this.mapper = mapper;
		this.pool = Executors.newFixedThreadPool(WORKERS);
	}

	public void queue(TestRecord record) {
		int id = record.getId();
		String json = null;
		try {
			record.setTestStatus(TestStatusType.queued);
			json = mapper.writeValueAsString(record);
			LogWebSocketServlet.sendToAll("queue-" + json);
			pool.submit(new TestExecution(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class TestExecution implements Runnable {
		private final int testId;

		public TestExecution(int id) {
			this.testId = id;
		}

		@Override
		public void run() {
			Date timestamp = null;
			TestRecord record = testRecords.findByID(testId);
			MDC.put("test_record", Integer.toString(testId));
			File resourceDir =
			        new File(record.getOutputFolder() + File.separatorChar + "resources");
			resourceDir.mkdirs();
			try {
				record.setTestStatus(TestStatusType.initializing);
				LogWebSocketServlet.sendToAll("init-" + Integer.toString(testId));

				// Build the JUnit runner
				String source = new String(Files.readAllBytes(Paths.get(record.getTestSrcPath())));
				Class<Object> testClass = newGeneratedTestClass(source);

				// Set Timestamps
				timestamp = new Date();
				record.setStartTime(timestamp);
				record.setTestStatus(TestStatusType.running);
				testRecords.update(record);
				LogWebSocketServlet.sendToAll("run-" + Integer.toString(testId));

				// Run tests
				JUnitCore junit = new JUnitCore();
				Result result = junit.run(testClass);
				
				// Force reload the test record to take changes maded by TestSuiteHelper
				record = testRecords.reloadTestRecord(record);

				// set duration
				long duration = (new Date()).getTime() - timestamp.getTime();

				record.setDuration(duration);
				
				if(result.wasSuccessful())
					record.setTestStatus(TestStatusType.success);
				else
					record.setTestStatus(TestStatusType.failure);

				testRecords.update(record);

				LogWebSocketServlet.sendToAll("success-" + Integer.toString(testId));
			} catch (Exception e) {
				e.printStackTrace();
				record.setTestStatus(TestStatusType.failure);
				testRecords.update(record);
				LogWebSocketServlet.sendToAll("fail-" + Integer.toString(testId));
			} finally {
				MDC.remove("test_record");
			}
		}

	}
	
    /**
     * Generate Java source for a Function which computes f(x)=expr
     *
     * @param expr String representation of Java expression that returns a double
     *             value for an input value x. The class in which this expression
     *             is embedded uses static import for all the members of the
     *             java.lang.Math class so they can be accessed without
     *             qualification.
     * @return an object which computes the function denoted by expr
     */
    Class<Object> newGeneratedTestClass(final String source) {

        try {
            // generate semi-secure unique package and class names
            final String packageName = PACKAGE_NAME + digits();
            final String className = "Generated_" + (classNameSuffix++) + digits();
            final String qName = packageName + '.' + className;

            // compile the generated Java source
            final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();
            Class<Object> compiledTest = compiler.compile("generated.GeneratedTests", source, errs, Object.class);

            return compiledTest;

        } catch (CharSequenceCompilerException e) {
            e.printStackTrace();
        }

        return null;

    }	
    
    /**
     * @return random hex digits with a '_' prefix
     */
    private String digits() {
        return '_' + Long.toHexString(random.nextLong());
    }

}
