package com.fwcd.timetable.view;

import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.timetable.model.TimeTableAppModel;
import com.fwcd.timetable.model.language.LanguageManager;

public class TimeTableAppContext {
	private final TimeTableAppModel model = new TimeTableAppModel();
	private final LanguageManager languageManager = new LanguageManager();
	
	public LanguageManager getLanguageManager() { return languageManager; }
	
	public String localize(String unlocalized) {
		return languageManager.getLanguage().get().localize(unlocalized);
	}
	
	public ReadOnlyObservable<String> localized(String unlocalized) {
		return languageManager.getLanguage().mapStrongly(it -> it.localize(unlocalized));
	}
	
	public TimeTableAppModel getModel() { return model; }
}
