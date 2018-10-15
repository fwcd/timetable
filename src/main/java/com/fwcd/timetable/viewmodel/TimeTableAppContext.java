package com.fwcd.timetable.viewmodel;

import java.time.format.DateTimeFormatter;
import java.util.Collections;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.timetable.model.language.Language;
import com.fwcd.timetable.model.language.LanguageManager;
import com.fwcd.timetable.viewmodel.settings.TimeTableAppSettings;

public class TimeTableAppContext {
	private final LanguageManager languageManager = new LanguageManager();
	private final Observable<TimeTableAppSettings> settings = new Observable<>(new TimeTableAppSettings.Builder().build());
	private final Observable<Language> language = new Observable<>(new Language("", Collections.emptyMap()));
	private final Observable<DateTimeFormatter> dateFormatter = new Observable<>(DateTimeFormatter.ISO_DATE);
	private final Observable<DateTimeFormatter> timeFormatter = new Observable<>(DateTimeFormatter.ISO_TIME);
	private final Observable<DateTimeFormatter> dateTimeFormatter = new Observable<>(DateTimeFormatter.ISO_DATE_TIME);
	
	public TimeTableAppContext() {
		settings.listenAndFire(it -> languageManager.getLanguage(it.getLanguage()).ifPresent(language::set));
		settings.listenAndFire(it -> dateFormatter.set(DateTimeFormatter.ofPattern(it.getDateFormat())));
		settings.listenAndFire(it -> timeFormatter.set(DateTimeFormatter.ofPattern(it.getTimeFormat())));
		settings.listenAndFire(it -> dateTimeFormatter.set(DateTimeFormatter.ofPattern(it.getDateTimeFormat())));
	}
	
	public Observable<TimeTableAppSettings> getSettings() { return settings; }
	
	public LanguageManager getLanguageManager() { return languageManager; }
	
	public void setLanguage(String key) { settings.set(settings.get().with().language(key).build()); }
	
	public ReadOnlyObservable<Language> getLanguage() { return language; }
	
	public String localize(String unlocalized) {
		return language.get().localize(unlocalized);
	}
	
	public ReadOnlyObservable<String> localized(String unlocalized) {
		return language.mapStrongly(it -> it.localize(unlocalized));
	}
}
