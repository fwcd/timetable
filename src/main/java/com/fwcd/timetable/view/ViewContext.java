package com.fwcd.timetable.view;

import com.fwcd.timetable.model.language.LanguageManager;

public class ViewContext {
	private final LanguageManager languageManager = new LanguageManager();
	
	public LanguageManager getLanguageManager() { return languageManager; }
	
	public String localize(String languageKey) {
		return languageManager.getLanguage().get().localize(languageKey);
	}
}
