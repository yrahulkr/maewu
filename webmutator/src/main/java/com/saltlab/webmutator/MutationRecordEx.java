package com.saltlab.webmutator;

public class MutationRecordEx extends MutationRecord{

	boolean visible;
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public MutationRecordEx(boolean success, String error) {
		super(success, error);
	}
	
	public MutationRecordEx(MutationRecord record) {
		super(record.success, record.error);
		mutatedXpath = record.mutatedXpath;
		originalXpath = record.originalXpath;
		operator = record.operator;
		success = record.success;
		error = record.error;
		visible = false;
		data = record.getData();
	}

}
