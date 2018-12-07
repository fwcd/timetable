package fwcd.timetable.model.query;

public class Parameter {
	private final String key;
	private final String value;
	
	public Parameter(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() { return key; }
	
	public String getValue() { return value; }
}
