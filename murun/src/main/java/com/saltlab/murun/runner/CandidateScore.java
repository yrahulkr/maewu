package com.saltlab.murun.runner;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.fragmentation.Fragment;
import com.saltlab.webmutator.operators.dom.DomOperators;
import com.saltlab.webmutator.operators.dom.DomOperators.OperatorType;
import com.saltlab.webmutator.utils.AttributeUtils;
import com.saltlab.webmutator.utils.DomUtils;

public class CandidateScore {
	private static final Logger LOG = LoggerFactory.getLogger(CandidateScore.class);

	private double elementConstant;
	private double operatorConstant;

	private double repeatConstant;

	public CandidateScore(double elementConstant, double operatorConstant, double repeatConstant) {
		this.elementConstant = elementConstant;
		this.operatorConstant = operatorConstant;
		this.repeatConstant = repeatConstant;
	}
	
	public double recomputeCandidateScore(MutationCandidateEx candidate, MutationCandidateEx selected, TraceSession traceSession) {
		double currentScore = candidate.getCurrentScore();

		List<Fragment> related = traceSession.getFragmentManager().getRelatedFragments(candidate.getFragment());
		

		int repeat = 0;
		if(related!=null)
			repeat = related.size();
		
		List<String> stateReps = new ArrayList<String>();
		for(Fragment rel: related) {
			// Add duplicate instances
			repeat += traceSession.stateClusters.get(rel.getReferenceState().getName()).size();
		}
		
		if(candidate.getOrigNode().isSameNode(selected.getOrigNode())) {
			// Reduce priority based on selection of same web element
			if(currentScore<=1) {
				LOG.info("Priority already lowest for candidate");
			}
			else {
				currentScore = currentScore - repeat*elementConstant*repeatConstant;
				candidate.setCurrentScore(currentScore);
			}
		}
		return currentScore;
	}

	public double computeCandidateScore(MutationCandidateEx candidate, TraceSession traceSession) {
		// Combine Element Score and Operator Score 	
		double elementScore = computeElementScore(candidate, traceSession);
		double operatorScore = computeOperatorScore(candidate, traceSession);
		candidate.setElementScore(elementScore);
		candidate.setOperatorScore(operatorScore);
		
		double candidateScore = elementConstant * elementScore+ operatorConstant * operatorScore;
		candidate.setPriorityScore(candidateScore);
		return candidateScore;
	}
	
	// Add number of instances based on duplicate states in session.allconcrete states as well.
	public int computeElementScore(MutationCandidateEx candidate, TraceSession traceSession) {
		// Repetitions, Coverage Type, Num Access
		CoverageNode cov = new CoverageNode(candidate.getFragment().getReferenceState().getId(), candidate.getXpath());
		List<CoverageDetail> testCov = traceSession.coverageMap.get(cov);
		List<CoverageDetail> obsCov = traceSession.observerCoverageMap.get(cov);
		List<Fragment> related = traceSession.getFragmentManager().getRelatedFragments(candidate.getFragment());
		

		int repeat = 0;
		if(related!=null)
			repeat = related.size();
		
		for(Fragment rel: related) {
			// Add duplicate instances
			repeat += traceSession.stateClusters.get(rel.getReferenceState().getName()).size();
		}
		
		
		int accessNum = 0;
		
		if(testCov!=null)
			accessNum = testCov.size();
		
		int jsNum = 0;
		if(obsCov!=null)
			jsNum = obsCov.size();
		
	/*	
		Coverage type = VipsUtils.getCoverage(candidate.getOrigNode(), AccessType.any);
		
		AccessType access = VipsUtils.getAccessType(candidate.getOrigNode());
		*/
		int score = repeat + accessNum + jsNum;	
		
		return score;
	}
	
	public int computeOperatorScore(MutationCandidateEx candidate, TraceSession traceSession) {
		// Element Kind (Interactable or Non-interactable), Coverage Access Type, JavaScript Usage
		CoverageNode cov = new CoverageNode(candidate.getFragment().getReferenceState().getId(), candidate.getXpath());
		List<CoverageDetail> testCov = traceSession.coverageMap.get(cov);
		List<CoverageDetail> obsCov = traceSession.observerCoverageMap.get(cov);
		
		if(testCov == null) {
			testCov = new ArrayList<>();
		}
		
		if(obsCov == null) {
			obsCov = new ArrayList<>();
		}
		
		int interactable = isInteractable(candidate) ? 1: 0;
		
		
		int non_interactable = interactable==0?1:0;
		
		int visible = candidate.getOrigNodeProperties().isDisplayed()? 1: 0;
		
		int hasText = candidate.getOrigNode().getTextContent().trim().isEmpty()? 0: 1;
		
		int isLeafNode = DomUtils.isLeafNode(candidate.getOrigNode())? 1: 0;
		
		int isContainerNode = isLeafNode == 0 ? 1: 0;
		
		int attrAccess = 0;
		
		int textAccess = 0;
		
		int subTreeAccess = 0;
		
		int actionAccess = 0;
		
		int findAccess = 0;
		
		
		for(CoverageDetail detail: testCov) {
			switch(detail.coverage) {
			case action:
				actionAccess = 1;
				break;
			case find:
				findAccess = 1;
				break;
			default:
			}
		}
		
		for(CoverageDetail detail: obsCov) {
			switch(detail.coverage) {
			case characterData:
				textAccess = 1;
				break;
			case subtree:
				subTreeAccess = 1;
				break;
			case childList:
				subTreeAccess = 1;
				break;
			case attributes:
				attrAccess = 1;
				break;
			default:
			}
		}
		
//		Operator operator = candidate.getOperator();
		
		int score = 0;
		OperatorType opType = DomOperators.getOperatorType(candidate.getOpString());
		
		switch(opType) {
		case AttributeAdd:
		case AttributeDelete:
		case AttributeReplace:
			score = interactable + visible + attrAccess + findAccess + actionAccess;
			break;
		case EventHandlerAdd:
		case EventHandlerDelete:
		case EventHandlerReplace:
			score = interactable + visible + findAccess + actionAccess + subTreeAccess + isLeafNode;
			break;
		case StyleColor:
		case StylePosition:
		case StyleSize:
			score = visible + findAccess + actionAccess + subTreeAccess + hasText + textAccess;
			break;
		case StyleVisibility:
			score = interactable + visible + findAccess + actionAccess + subTreeAccess + hasText + non_interactable + textAccess;
			break;
		case SubtreeDelete:
		case SubtreeInsert:
		case SubtreeMove:
			score = visible + findAccess + subTreeAccess + isContainerNode;
			break;
		case TextDelete:
		case TextInsert:
		case TextReplace:
			score = visible + findAccess + subTreeAccess + hasText + non_interactable + textAccess + isLeafNode;
			break;
		default:
			break;
			
		}
		return score;
	}

	private boolean isInteractable(MutationCandidateEx candidate) {
		boolean hasEventHandler = (candidate.getOrigNodeProperties().getEventListener()!=null && !candidate.getOrigNodeProperties().getEventListener().trim().isEmpty());
		boolean isStandardInteractable = AttributeUtils.isStandardInteractable(candidate.getOrigNode());
		return hasEventHandler || isStandardInteractable;
	}
}
