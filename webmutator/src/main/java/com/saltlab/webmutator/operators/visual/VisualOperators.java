package com.saltlab.webmutator.operators.visual;

import java.util.ArrayList;
import java.util.List;

import com.saltlab.webmutator.operators.Operator;

public class VisualOperators {

	public List<Operator> getVisualOperators(List<String> excludedOperators) {
		List<Operator> returnList = new ArrayList<Operator>();
		// Mask
		returnList.add((Operator) new MaskMutator());

		// Distort
		returnList.add((Operator) new DistortMutator());

		return returnList;
	}

}
