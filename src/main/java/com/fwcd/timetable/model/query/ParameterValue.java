package com.fwcd.timetable.model.query;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fwcd.fructose.Option;

/**
 * A concrete parameter value.
 */
public class ParameterValue {
	private final String value;
	private final String label;
	private final Option<String> description;
	private final List<String> possibleParameters;
	
	public ParameterValue(String value, String label, Option<String> description, List<String> possibleParameters) {
		this.value = value;
		this.label = label;
		this.description = description;
		this.possibleParameters = possibleParameters;
	}
	
	public String getValueString() { return value; }
	
	public String getLabel() { return label; }
	
	public Option<String> getDescription() { return description; }
	
	public List<String> getPossibleParameters() { return possibleParameters; }
	
	@Override
	public String toString() {
		return value + " (" + label + ") <" + getPossibleParameters() + ">";
	}
	
	public static class Builder {
		private final String value;
		private String label;
		private Option<String> description = Option.empty();
		private List<String> possibleParameters = Collections.emptyList();
		
		public Builder(String value) {
			this.value = value;
		}
		
		public Builder label(String label) {
			this.label = label;
			return this;
		}
		
		public Builder description(String description) {
			this.description = Option.of(description);
			return this;
		}
		
		public Builder possibleParameters(String... possibleParameters) {
			return possibleParameters(Arrays.asList(possibleParameters));
		}
		
		public Builder possibleParameters(List<String> possibleParameters) {
			this.possibleParameters = possibleParameters;
			return this;
		}
		
		public ParameterValue build() {
			return new ParameterValue(value, label, description, possibleParameters);
		}
	}
}
