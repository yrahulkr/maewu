package com.saltlab.webmutator.operators.dom;

public class MutationData {
	String key;
	Object value;
	
	public MutationData(String key, Object value) {
		this.key = key;
		this.value =value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return key + " : " + value;
	}
}
