package com.saltlab.murun.runner;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBackedEmbeddedBrowser;
import com.saltlab.murun.runner.MutantGenerator.GENMODE;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.MutationUtils;
import com.saltlab.murun.utils.Settings;
import com.saltlab.murun.utils.TraceUtils;

import utils.DriverProvider;
import utils.TestCaseExecutor;

public class RerunTraceForCandidates {

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("please provide subject  "
					+ "available : (addressbook, mantisbt, mrbs, ppma, collabtive, claroline)");
			System.exit(-1);
		}
		SUBJECT subject = Stub.getSubject(args[0]);
		// start docker
		TestCaseExecutor.restartDocker(subject, true);

		TraceSession session = TraceUtils.collectTrace(true, subject);

		// List<MutationCandidateEx> mutationRecords = null;
		if (session != null) {
			boolean loadAdaptedRecords = false;
			List<MutationCandidateEx> mutationRecords = MutationUtils.loadMutationMap(subject, loadAdaptedRecords);

			// mutationRecords = MutationUtils.mutateFragmentedTrace(session,
			// Settings.OVERWRITE_MUTATION, subject);

			MutantGenerator generator = new MutantGenerator(session, subject, GENMODE.ADAPT);

			Settings.USE_CRAWLJAX = false;
			Settings.aspectActive = false;

			WebDriver driver = DriverProvider.getInstance().getDriver();
			EmbeddedBrowser browser = WebDriverBackedEmbeddedBrowser.withDriver(driver);
			((WebDriverBackedEmbeddedBrowser) browser).setPixelDensity(Settings.PIXEL_DENSITY);

			List<MutationCandidateEx> adapted = generator.getAdaptedCandidates(mutationRecords, null);

			browser.close();

			MutationUtils.writeMutationRecords(subject, adapted, true);

			MutationUtils.writeRecordsToFile(adapted, new File("temp/adapted.json"));
		}

		System.exit(0);
	}

}
