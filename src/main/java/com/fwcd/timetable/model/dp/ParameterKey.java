package com.fwcd.timetable.model.dp;

public class ParameterKey {
	private final String name;
	private final String label;
	private final String description;
	
	public ParameterKey(String name, String label) {
		this.name = name;
		this.label = label;
		description = "";
	}
	
	public ParameterKey(String name, String label, String description) {
		this.name = name;
		this.label = label;
		this.description = description;
	}
	
	public String getName() { return name; }
	
	public String getLabel() { return label; }
	
	public String getDescription() { return description; }
}
