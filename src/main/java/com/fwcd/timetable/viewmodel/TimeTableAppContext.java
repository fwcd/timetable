package com.fwcd.timetable.viewmodel;

import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.timetable.model.language.Language;
import com.fwcd.timetable.model.language.LanguageManager;
import com.fwcd.timetable.viewmodel.settings.TimeTableAppSettings;
import com.fwcd.timetable.viewmodel.theme.Theme;
import com.fwcd.timetable.viewmodel.theme.ThemeManager;
import com.fwcd.timetable.viewmodel.utils.FileSaveState;
import com.fwcd.timetable.viewmodel.utils.PersistentStorage;

public class TimeTableAppContext {
	private final LanguageManager languageManager = new LanguageManager();
	private final ThemeManager themeManager = new ThemeManager();
	private final PersistentStorage persistentStorage = new PersistentStorage(Paths.get(System.getProperty("user.home"), ".timetable"));
	
	private final Observable<TimeTableAppSettings> settings = new Observable<>(new TimeTableAppSettings.Builder().build());
	private final Observable<Language> language = new Observable<>(new Language("", Collections.emptyMap()));
	private final Observable<Theme> theme = new Observable<>(new Theme("", ""));
	private final Observable<DateTimeFormatter> dateFormatter = new Observable<>(DateTimeFormatter.ISO_DATE);
	private final Observable<DateTimeFormatter> timeFormatter = new Observable<>(DateTimeFormatter.ISO_TIME);
	private final Observable<DateTimeFormatter> dateTimeFormatter = new Observable<>(DateTimeFormatter.ISO_DATE_TIME);
	private final Observable<DateTimeFormatter> yearMonthFormatter = new Observable<>(DateTimeFormatter.ofPattern("MM.yyyy"));
	
	private final FileSaveState fileSaveState = new FileSaveState();
	
	public TimeTableAppContext() {
		settings.listenAndFire(it -> {
			languageManager.getLanguage(it.getLanguage()).ifPresent(language::set);
			themeManager.getTheme(it.getTheme()).ifPresent(theme::set);
			
			dateFormatter.set(DateTimeFormatter.ofPattern(it.getDateFormat()));
			timeFormatter.set(DateTimeFormatter.ofPattern(it.getTimeFormat()));
			dateTimeFormatter.set(DateTimeFormatter.ofPattern(it.getDateTimeFormat()));
			yearMonthFormatter.set(DateTimeFormatter.ofPattern(it.getYearMonthFormat()));
		});
		
		persistentStorage.add("settings", settings, TimeTableAppSettings.class);
		persistentStorage.loadFromDisk();
		persistentStorage.addAutoSaveHooks();
	}
	
	public void resetSettings() { settings.set(new TimeTableAppSettings.Builder().build()); }
	
	public Observable<TimeTableAppSettings> getSettings() { return settings; }
	
	public LanguageManager getLanguageManager() { return languageManager; }
	
	public ThemeManager getThemeManager() { return themeManager; }
	
	public void setLanguage(String key) { settings.set(settings.get().with().language(key).build()); }

	public void setTheme(String key) { settings.set(settings.get().with().theme(key).build()); }
	
	public ReadOnlyObservable<Language> getLanguage() { return language; }
	
	public ReadOnlyObservable<Theme> getTheme() { return theme; }
	
	public ReadOnlyObservable<DateTimeFormatter> getDateFormatter() { return dateFormatter; }
	
	public ReadOnlyObservable<DateTimeFormatter> getTimeFormatter() { return timeFormatter; }
	
	public ReadOnlyObservable<DateTimeFormatter> getDateTimeFormatter() { return dateTimeFormatter; }
	
	public ReadOnlyObservable<DateTimeFormatter> getYearMonthFormatter() { return yearMonthFormatter; }
	
	public FileSaveState getFileSaveState() { return fileSaveState; }
	
	public String localize(String unlocalized) {
		return language.get().localize(unlocalized);
	}
	
	public ReadOnlyObservable<String> localized(String unlocalized) {
		return language.mapStrongly(it -> it.localize(unlocalized));
	}
}
