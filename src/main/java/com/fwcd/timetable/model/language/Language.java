package com.fwcd.timetable.model.language;

import java.util.Map;

public class Language {
	private final Map<String, String> mappings;
	
	public Language(Map<String, String> mappings) {
		this.mappings = mappings;
	}
	
	public String get(String key) {
		return mappings.get(key);
	}
}
