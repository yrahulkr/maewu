package com.saltlab.murun;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.saltlab.murun.runner.MutantGenerator;
import com.saltlab.murun.runner.MutantGenerator.GENMODE;
import com.saltlab.murun.runner.MutationCandidateEx;
import com.saltlab.murun.runner.MutationRunner;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.runner.TraceSession;
import com.saltlab.murun.utils.Settings;
import com.saltlab.webmutator.MutationMode;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.MutationRecordEx;
import com.saltlab.webmutator.WebMutator;
import com.saltlab.webmutator.operators.dom.DomOperator;
import com.saltlab.webmutator.operators.dom.EventHandlerDelete;
import com.saltlab.webmutator.plugins.DefaultNodePicker;

import utils.DriverProvider;
import utils.Properties;

public class TestMutationOperations {
	@Test
	public void testEventMutations() {
		
		TraceSession provider = TraceSession.getInstance();
		try {
			Settings.outputDir = "testCrawljax";
			Settings.aspectActive = true;
			Settings.USE_CRAWLJAX = true;
			new Properties(SUBJECT.addressbook);

			provider.crawljaxSetup("testCrawljax", "http://localhost:3000");

			WebDriver driver = DriverProvider.getInstance().getDriver();
			driver.get("http://localhost:3000/addressbook");

			driver.findElement(By.name("user")).sendKeys("admin");
			driver.findElement(By.name("pass")).sendKeys("secret");
			driver.findElement(By.xpath("/html/body/div/div[4]/form/input[3]")).click();
			
			System.out.println(provider.getBrowser().getStrippedDom());
			
//			provider.stopTrace();
			
			Settings.aspectActive = false;
			Settings.USE_CRAWLJAX = false;
			System.out.println(provider.getCoverageMap());
			provider.getStateFlowGraph().getAllStates();
			
			WebMutator mutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());

			
			List<DomOperator> operatorList = new ArrayList<DomOperator>();
			operatorList.add(new EventHandlerDelete());
			mutator.setDomOperators(operatorList);
			
			MutantGenerator mutGen = new MutantGenerator(provider, SUBJECT.dummy, GENMODE.ADAPT);
						
			List<MutationCandidateEx> candidates = mutGen.getCandidatePool();
			
			System.out.println("found candidates " + candidates.size());
			
			MutationRecord record = mutator.applyMutationToCandidate(candidates.get(mutator.getRandom(candidates.size())));
			MutationRunner runner = new MutationRunner(SUBJECT.dummy, null, false);
			MutationRecordEx appliedRecord = MutationRunner.getInstance().applyMRToBrowserState(driver, record);
			System.out.println(appliedRecord);
			driver.findElement(By.xpath(appliedRecord.getMutatedXpath())).click();
			provider.stopTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			provider.getBrowser().close();
		}
		
		

	}
}
