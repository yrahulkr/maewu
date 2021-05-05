package com.saltlab.murun.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.crawljax.vips_selenium.VipsUtils;
import com.crawljax.vips_selenium.VipsUtils.AccessType;
import com.crawljax.vips_selenium.VipsUtils.Coverage;
import com.saltlab.webmutator.operators.Operator;
import com.saltlab.webmutator.operators.dom.DomOperator;
import com.saltlab.webmutator.plugins.DefaultNodePicker;
import com.saltlab.webmutator.plugins.NodePickerPlugin;

public class FragmentNodePicker implements NodePickerPlugin{
	
	private MutationSession mutationSession;
	private TraceSession traceSession;
	private Random random;

	public FragmentNodePicker(MutationSession mutationSession, TraceSession traceSession) {
		this.mutationSession = mutationSession;
		this.traceSession = traceSession;
		random = new Random();
		random.longs();
	}

	@Override
	public Node pickNode(Document dom, Operator domOperator) {
		List<String> tagList = ((DomOperator)domOperator).getTagList();
		if(tagList.isEmpty()) {
			tagList = DomOperator.getDefaultTags();
		}
		List<Node> nodes = new ArrayList<Node>();
		for(String tag: tagList) {
			if(tag.equalsIgnoreCase("#text")) {
				//	            populateTextNodes(dom, nodes);
				DefaultNodePicker.populateLeafNodes(dom, nodes);
			}
			else {

				NodeList tagNodes = dom.getElementsByTagName(tag);
				for(int i = 0; i<tagNodes.getLength(); i++)
					nodes.add(tagNodes.item(i));
			}
		}
		List<Node> filteredByCoverage = nodes.stream()
				.filter(node -> (VipsUtils.getCoverage(node, AccessType.any) != Coverage.none))
				.collect(Collectors.toList());
//		for(Node node: nodes) {
//			if(VipsUtils.getCoverage(node, AccessType.any) != Coverage.none) {
//				
//			}
//		}
		
		if(filteredByCoverage.size() == 0) {
			return null;
		}

		int nodeNumber = Math.abs((int) (random.nextLong() % filteredByCoverage.size()));
		System.out.println("nodes available " + filteredByCoverage.size());

		System.out.println("Node number " + nodeNumber);
		System.out.println("Picked : " + filteredByCoverage.get(nodeNumber));
		return filteredByCoverage.get(nodeNumber);
	}
}
