package com.fwcd.timetable.model.language;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.io.ResourceFile;

public class LanguageManager {
	private final Map<String, Language> languages = new HashMap<>();
	private final LanguageParser parser = new LanguageParser();
	private final Observable<Language> language;
	
	public LanguageManager() {
		addLanguage(readResourceLanguage("English", "English.json"));
		addLanguage(readResourceLanguage("Deutsch", "Deutsch.json"));
		
		language = new Observable<>(languages.get("English"));
	}
	
	public Observable<Language> getLanguage() { return language; }
	
	public void setLanguage(String key) { language.set(languages.get(key)); }
	
	public Set<String> getLanguageKeys() { return languages.keySet(); }
	
	public void addLanguage(Language language) {
		languages.put(language.getName(), language);
	}
	
	public Language readResourceLanguage(String name, String languageFileName) {
		return parser.parseFromJson(name, new ResourceFile("/languages/" + languageFileName));
	}
}
