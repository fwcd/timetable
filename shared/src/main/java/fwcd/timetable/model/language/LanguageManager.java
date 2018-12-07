package fwcd.timetable.model.language;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fwcd.fructose.Option;
import fwcd.fructose.io.ResourceFile;

public class LanguageManager {
	private final Map<String, Language> languages = new HashMap<>();
	private final LanguageParser parser = new LanguageParser();
	
	public LanguageManager() {
		addLanguage(readResourceLanguage(LanguageKey.ENGLISH, "English.json"));
		addLanguage(readResourceLanguage(LanguageKey.DEUTSCH, "Deutsch.json"));
	}
	
	public Option<Language> getLanguage(String key) { return Option.ofNullable(languages.get(key)); }
	
	public Set<String> getLanguageKeys() { return languages.keySet(); }
	
	public void addLanguage(Language language) {
		languages.put(language.getName(), language);
	}
	
	public Language readResourceLanguage(String name, String languageFileName) {
		return parser.parseFromJson(name, new ResourceFile("/languages/" + languageFileName));
	}
}
