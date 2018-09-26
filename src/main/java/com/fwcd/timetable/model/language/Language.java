package com.fwcd.timetable.model.language;

import java.util.Map;
import java.util.NoSuchElementException;

public class Language {
	private final String name;
	private final Map<String, String> mappings;
	
	public Language(String name, Map<String, String> mappings) {
		this.name = name;
		this.mappings = mappings;
	}
	
	public String getName() {
		return name;
	}
	
	public String localize(String key) {
		String localized = mappings.get(key);
		if (localized == null) {
			throw new NoSuchElementException("Could not find language key " + key + " in language " + name);
		}
		return localized;
	}
}
