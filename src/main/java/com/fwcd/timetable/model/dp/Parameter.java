package com.fwcd.timetable.model.dp;

public class Parameter {
	private final ParameterKey key;
	private final String value;
	
	public Parameter(ParameterKey key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public ParameterKey getKey() { return key; }
	
	public String getValue() { return value; }
}
