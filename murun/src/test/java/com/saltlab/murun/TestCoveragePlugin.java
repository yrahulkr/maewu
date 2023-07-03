package com.saltlab.murun;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.crawljax.core.state.StateVertex;
import com.crawljax.fragmentation.FragmentationPlugin;
import com.crawljax.plugins.coverage.CoveragePlugin;
import com.crawljax.stateabstractions.hybrid.HybridStateVertexImpl;
import com.crawljax.util.DomUtils;

public class TestCoveragePlugin {

	public StateVertex getHybridStateVertex(String domLocation, String screenShotLocation) throws IOException {
		String stateName = "dummy";
//		String domLocation = Paths.get(Settings.traceDir, subject.name(), Settings.REL_TRACE_LOC, "doms", stateName+".html").toString();
//		String fragDomLocation = Paths.get(Settings.traceDir, subject.name(), Settings.REL_TRACE_LOC, "doms", "frag_" + stateName + ".html").toString();
//		String screenShotLocation = Paths.get(Settings.traceDir, subject.name(), Settings.REL_TRACE_LOC, "screenshots", stateName+".png").toString();
		String dom = FileUtils.readFileToString(new File(domLocation));
		StateVertex stateVertex = new HybridStateVertexImpl(0, "http://dummy", stateName, dom, dom, 0.0, true);
		
		BufferedImage screenshot = ImageIO.read(new File(screenShotLocation));
		
		FragmentationPlugin.loadFragmentState(stateVertex, null, DomUtils.asDocument(dom), screenshot);
		return stateVertex;
	} 
	
	@Test
	public void testStateLabelling() {
		
		try {
			StateVertex state = getHybridStateVertex("src/test/resources/dom.html", "src/test/resources/screenshot.png");
			CoveragePlugin plugin = new CoveragePlugin();
			BufferedImage annotated = plugin.annotateState(state);
			CoveragePlugin.showPicture(annotated);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
