package fwcd.timetable.model.query;

import fwcd.fructose.Observable;

public class MutableParameter {
	private final Observable<String> key;
	private final Observable<String> value;
	
	public MutableParameter() {
		key = new Observable<>("");
		value = new Observable<>("");
	}
	
	public MutableParameter(String key, String value) {
		this.key = new Observable<>(key);
		this.value = new Observable<>(value);
	}
	
	public Observable<String> getKey() { return key; }
	
	public Observable<String> getValue() { return value; }
	
	public Parameter toImmutable() { return new Parameter(key.get(), value.get()); }
}
