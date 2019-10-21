package fwcd.timetable.viewmodel;

import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import fwcd.fructose.Observable;
import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.model.json.GsonConfigurator;
import fwcd.timetable.model.json.GsonUtils;
import fwcd.timetable.model.language.Language;
import fwcd.timetable.model.language.LanguageManager;
import fwcd.timetable.plugin.PluginJarList;
import fwcd.timetable.plugin.PluginManager;
import fwcd.timetable.viewmodel.settings.TimeTableAppSettings;
import fwcd.timetable.viewmodel.theme.Theme;
import fwcd.timetable.viewmodel.theme.ThemeManager;
import fwcd.timetable.viewmodel.utils.FileSaveState;
import fwcd.timetable.viewmodel.utils.PersistentStorage;
import fwcd.timetable.viewmodel.utils.SettingsBasedGsonConfigurator;

/**
 * An aggregate of application-specific managers and state.
 */
public class TimeTableAppContext {
	private final Observable<TimeTableAppSettings> settings = new Observable<>(new TimeTableAppSettings.Builder().build());
	private final Observable<Language> language = new Observable<>(new Language("", Collections.emptyMap()));
	private final Observable<Theme> theme = new Observable<>(new Theme("", ""));
	
	private final LanguageManager languageManager = new LanguageManager();
	private final ThemeManager themeManager = new ThemeManager();
	private final GsonConfigurator gsonConfigurator = GsonUtils.DEFAULT_CONFIGURATOR.andThen(new SettingsBasedGsonConfigurator(settings));
	private final PersistentStorage persistentStorage = new PersistentStorage(Paths.get(System.getProperty("user.home"), ".timetable"), gsonConfigurator);
	private final PluginManager pluginManager = new PluginManager();
	
	private final FileSaveState fileSaveState = new FileSaveState();
	private final Localizer localizer = new LocalizerBackend(language);
	private final TemporalFormattersBackend formatters = new TemporalFormattersBackend(settings);
	
	public TimeTableAppContext() {
		settings.listenAndFire(it -> {
			languageManager.getLanguage(it.getLanguage()).ifPresent(language::set);
			themeManager.getTheme(it.getTheme()).ifPresent(theme::set);
		});
		
		persistentStorage.add("settings", settings, TimeTableAppSettings.class);
		persistentStorage.add("pluginJars", pluginManager.getPluginJars(), PluginJarList.class);
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
	
	public ReadOnlyObservable<DateTimeFormatter> getDateFormatter() { return formatters.getObservableDateFormatter(); }
	
	public ReadOnlyObservable<DateTimeFormatter> getTimeFormatter() { return formatters.getObservableTimeFormatter(); }
	
	public ReadOnlyObservable<DateTimeFormatter> getDateTimeFormatter() { return formatters.getObservableDateTimeFormatter(); }
	
	public ReadOnlyObservable<DateTimeFormatter> getYearMonthFormatter() { return formatters.getObservableYearMonthFormatter(); }
	
	public GsonConfigurator getGsonConfigurator() { return gsonConfigurator; }
	
	public FileSaveState getFileSaveState() { return fileSaveState; }
	
	public String localize(String unlocalized) { return localizer.localize(unlocalized); }
	
	public ReadOnlyObservable<String> localized(String unlocalized) { return localizer.localized(unlocalized); }
	
	public Localizer getLocalizer() { return localizer; }
	
	public TemporalFormatters getFormatters() { return formatters; }
}
