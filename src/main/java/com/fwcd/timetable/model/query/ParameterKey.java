package com.fwcd.timetable.model.query;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fwcd.fructose.Option;

public class ParameterKey {
	private final String name;
	private final String label;
	private final boolean hidden;
	private final Option<String> description;
	private final List<ParameterValue> values;
	
	private ParameterKey(String name, String label, boolean hidden, Option<String> description, List<ParameterValue> values) {
		this.name = name;
		this.label = label;
		this.hidden = hidden;
		this.description = description;
		this.values = values;
	}
	
	public String getName() { return name; }
	
	public String getLabel() { return label; }
	
	public Option<String> getDescription() { return description; }
	
	public List<ParameterValue> getValues() { return values; }
	
	public boolean isHidden() { return hidden; }
	
	@Override
	public String toString() {
		return label;
	}
	
	public static class Builder {
		private final String name;
		private boolean hidden = false;
		private String label;
		private Option<String> description = Option.empty();
		private List<ParameterValue> values = Collections.emptyList();
		
		public Builder(String name) {
			this.name = name;
			label = name;
		}
		
		public Builder label(String label) {
			this.label = label;
			return this;
		}
		
		public Builder hidden(boolean hidden) {
			this.hidden = hidden;
			return this;
		}
		
		public Builder description(String description) {
			this.description = Option.of(description);
			return this;
		}
		
		public Builder values(ParameterValue... values) {
			this.values = Arrays.asList(values);
			return this;
		}
		
		public ParameterKey build() {
			return new ParameterKey(name, label, hidden, description, values);
		}
	}
}
