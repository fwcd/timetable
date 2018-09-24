package com.fwcd.timetable.model.language;

import java.util.HashMap;
import java.util.Map;

import com.fwcd.fructose.io.ResourceFile;

public class LanguageManager {
	private final Map<String, Language> languages = new HashMap<>();
	private final LanguageParser parser = new LanguageParser();
	private final Language language;
	
	public LanguageManager() {
		languages.put("Deutsch", readResourceLanguage("Deutsch.json"));
		languages.put("English", readResourceLanguage("English.json"));
		
		language = languages.get("English");
	}
	
	public Language getLanguage() { return language; }
	
	public Language readResourceLanguage(String languageFileName) {
		return parser.parseFromJson(new ResourceFile("/languages/" + languageFileName));
	}
}
