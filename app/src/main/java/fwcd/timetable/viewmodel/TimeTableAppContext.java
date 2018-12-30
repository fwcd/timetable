package fwcd.timetable.viewmodel;

import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fwcd.fructose.Observable;
import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.model.language.Language;
import fwcd.timetable.model.language.LanguageManager;
import fwcd.timetable.plugin.PluginManager;
import fwcd.timetable.viewmodel.settings.TimeTableAppSettings;
import fwcd.timetable.viewmodel.theme.Theme;
import fwcd.timetable.viewmodel.theme.ThemeManager;
import fwcd.timetable.viewmodel.utils.FileSaveState;
import fwcd.timetable.viewmodel.utils.PersistentStorage;

/**
 * An aggregate of application-specific managers and state.
 */
public class TimeTableAppContext {
	private final LanguageManager languageManager = new LanguageManager();
	private final ThemeManager themeManager = new ThemeManager();
	private final PersistentStorage persistentStorage = new PersistentStorage(Paths.get(System.getProperty("user.home"), ".timetable"));
	private final PluginManager pluginManager = new PluginManager();
	
	private final Observable<TimeTableAppSettings> settings = new Observable<>(new TimeTableAppSettings.Builder().build());
	private final Observable<Language> language = new Observable<>(new Language("", Collections.emptyMap()));
	private final Observable<Theme> theme = new Observable<>(new Theme("", ""));
	private final Observable<DateTimeFormatter> dateFormatter = new Observable<>(DateTimeFormatter.ISO_DATE);
	private final Observable<DateTimeFormatter> timeFormatter = new Observable<>(DateTimeFormatter.ISO_TIME);
	private final Observable<DateTimeFormatter> dateTimeFormatter = new Observable<>(DateTimeFormatter.ISO_DATE_TIME);
	private final Observable<DateTimeFormatter> yearMonthFormatter = new Observable<>(DateTimeFormatter.ofPattern("MM.yyyy"));
	
	private final FileSaveState fileSaveState = new FileSaveState();
	private final Map<String, ReadOnlyObservable<String>> cachedLocalizations = new HashMap<>();
	
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
	
	public PluginManager getPluginManager() { return pluginManager; }
	
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
		ReadOnlyObservable<String> result = cachedLocalizations.get(unlocalized);
		if (result == null) {
			result = language.mapStrongly(it -> it.localize(unlocalized));
			cachedLocalizations.put(unlocalized, result);
		}
		return result;
	}
}