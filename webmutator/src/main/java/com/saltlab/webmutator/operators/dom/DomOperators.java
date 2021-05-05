package com.saltlab.webmutator.operators.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DomOperators {
	
	Set<String> eventListeners;
	Map<String, Set<String>> attributeTokens;
	Set<String> textTokens;
	
	public static enum OperatorType{
		AttributeAdd, AttributeDelete, AttributeReplace, SubtreeDelete, SubtreeInsert, SubtreeMove, TextInsert, TextDelete, TextReplace, StyleColor, StyleVisibility, StyleSize, StylePosition, EventHandlerAdd, EventHandlerDelete, EventHandlerReplace,
	}
	
	public static OperatorType getOperatorType(String opString) {
		switch(opString) {
		case "AttributeAdd":
			return OperatorType.AttributeAdd;
		case "AttributeDelete":
			return OperatorType.AttributeDelete;
		case "AttributeReplace":
			return OperatorType.AttributeReplace;
		case "SubtreeDelete":
			return OperatorType.SubtreeDelete;
		case "SubtreeInsert":
			return OperatorType.SubtreeInsert;
		case "SubtreeMove":
			return OperatorType.SubtreeMove;
		case "TextInsert":
			return OperatorType.TextInsert;
		case "TextDelete":
			return OperatorType.TextDelete;
		case "TextReplace":
			return OperatorType.TextReplace;
		case "StyleColor":
			return OperatorType.StyleColor;
		case "StylePosition":
			return OperatorType.StylePosition;
		case "StyleSize":
			return OperatorType.StyleSize;
		case "StyleVisibility":
			return OperatorType.StyleVisibility;
		case "EventHandlerAdd":
			return OperatorType.EventHandlerAdd;
		case"EventHandlerDelete":
			return OperatorType.EventHandlerDelete;
		case "EventHandlerReplace":
			return OperatorType.EventHandlerReplace;
			default:
				return null;
		}
	}
	

	public DomOperators() {
		eventListeners = null;
		attributeTokens = null;
		textTokens = null;
	}
	
	public DomOperators(Set<String> eventListeners, Map<String, Set<String>> attributeTokens, Set<String> textTokens) {
		this.eventListeners= eventListeners;
		this.attributeTokens = attributeTokens;
		this.textTokens = textTokens;
	}
	
	
	
	public List<DomOperator> getDomOperators(List<String> excludeList){
		List<DomOperator> returnList = new ArrayList<DomOperator>();
		// Attribute
//		returnList.add((DomOperator) new AttributeMutator());
		returnList.add((DomOperator) new AttributeAdd(attributeTokens));
		returnList.add((DomOperator) new AttributeDelete());
		returnList.add((DomOperator) new AttributeReplace(attributeTokens));

//		// Subtree
		returnList.add((DomOperator) new SubtreeDelete());
		returnList.add((DomOperator) new SubtreeInsert());
		returnList.add((DomOperator) new SubtreeMove());


		
//		TextNode
//		returnList.add((DomOperator) new TextNodeMutator());
		returnList.add((DomOperator) new TextInsert(textTokens));
		returnList.add((DomOperator) new TextDelete());
		returnList.add((DomOperator) new TextReplace(textTokens));
		
		//Style
		returnList.add((DomOperator) new StyleColor());
		returnList.add((DomOperator) new StylePosition());
		returnList.add((DomOperator) new StyleVisibility());
		returnList.add((DomOperator) new StyleSize());
	
		//Event Handling
		returnList.add((DomOperator) new EventHandlerAdd(eventListeners));
		returnList.add((DomOperator) new EventHandlerDelete());
		returnList.add((DomOperator) new EventHandlerReplace(eventListeners));
	
//		// Container
//		returnList.add((Operator) new ContainerNodeMutator());
//
//		// LeafNode
//		returnList.add((Operator) new ActionableNodeMutator());

		// Tag
//		returnList.add((Operator) new TagMutator());

		
		return returnList;
	}
}
