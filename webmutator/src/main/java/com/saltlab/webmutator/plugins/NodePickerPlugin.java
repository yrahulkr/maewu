package com.saltlab.webmutator.plugins;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.saltlab.webmutator.operators.Operator;

public interface NodePickerPlugin {

	Node pickNode(Document dom, Operator domOperator);
	
}
