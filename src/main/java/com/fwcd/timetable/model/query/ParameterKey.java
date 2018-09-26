package com.fwcd.timetable.model.query;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import com.fwcd.fructose.Option;

public class ParameterKey {
	private final String name;
	private final String label;
	private final Option<String> description;
	private final ParameterValueNode valueTree;
	
	private ParameterKey(String name, String label, Option<String> description, ParameterValueNode valueTree) {
		this.name = name;
		this.label = label;
		this.description = description;
		this.valueTree = valueTree;
	}
	
	public String getName() { return name; }
	
	public String getLabel() { return label; }
	
	public Option<String> getDescription() { return description; }
	
	public ParameterValueNode getValueTree() { return valueTree; }
	
	@Override
	public String toString() {
		return name + " (" + label + ") ";
	}
	
	public static class Builder {
		private final String name;
		private String label;
		private Option<String> description = Option.empty();
		private ParameterValueNode valueTree = new ParameterValueNode(Collections.emptyList());
		
		public Builder(String name) {
			this.name = name;
			label = name;
		}
		
		public Builder label(String label) {
			this.label = label;
			return this;
		}
		
		public Builder description(String description) {
			this.description = Option.of(description);
			return this;
		}
		
		public Builder values(ParameterValue... values) {
			valueTree = new ParameterValueNode(Arrays.stream(values).map(ParameterValueNode::new).collect(Collectors.toList()));
			return this;
		}
		
		public Builder valueTree(ParameterValueNode root) {
			valueTree = root;
			return this;
		}
		
		public ParameterKey build() {
			return new ParameterKey(name, label, description, valueTree);
		}
	}
}
