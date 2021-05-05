package com.saltlab.webmutator.utils;

import java.awt.Point;

import com.google.gson.internal.LinkedTreeMap;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.operators.dom.MutationData;

public class GsonUtils {
	public MutationData getMutationData(MutationRecord record) {
		
		return null;
	}

	public static Point deserializePoint(LinkedTreeMap value) {
		int x = ((Double) value.get("x")).intValue();
		int y = ((Double) value.get("y")).intValue();
		return new Point(x, y);
	}
	
	
}
