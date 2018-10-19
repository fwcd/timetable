package com.fwcd.timetable.viewmodel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.timetable.model.json.GsonUtils;
import com.fwcd.timetable.model.language.Language;
import com.fwcd.timetable.model.language.LanguageManager;
import com.fwcd.timetable.viewmodel.settings.TimeTableAppSettings;
import com.fwcd.timetable.viewmodel.utils.FileSaveState;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class TimeTableAppContext {
	private static final Gson GSON = GsonUtils.newGson();
	private final LanguageManager languageManager = new LanguageManager();
	
	private final Observable<TimeTableAppSettings> settings;
	private final Observable<Language> language = new Observable<>(new Language("", Collections.emptyMap()));
	private final Observable<DateTimeFormatter> dateFormatter = new Observable<>(DateTimeFormatter.ISO_DATE);
	private final Observable<DateTimeFormatter> timeFormatter = new Observable<>(DateTimeFormatter.ISO_TIME);
	private final Observable<DateTimeFormatter> dateTimeFormatter = new Observable<>(DateTimeFormatter.ISO_DATE_TIME);
	
	private final FileSaveState fileSaveState = new FileSaveState();
	private final Path configDirectory = Paths.get(System.getProperty("user.home"), ".timetable");
	private final Path savedSettingsPath = configDirectory.resolve("settings.json");
	private final boolean autoSaveSettingsEnabled = true;
	
	public TimeTableAppContext() {
		settings = new Observable<>(loadSettings().orElseGet(() -> new TimeTableAppSettings.Builder().build()));
		settings.listenAndFire(it -> {
			languageManager.getLanguage(it.getLanguage()).ifPresent(language::set);
			dateFormatter.set(DateTimeFormatter.ofPattern(it.getDateFormat()));
			timeFormatter.set(DateTimeFormatter.ofPattern(it.getTimeFormat()));
			dateTimeFormatter.set(DateTimeFormatter.ofPattern(it.getDateTimeFormat()));
			
			if (autoSaveSettingsEnabled) {
				saveSettings();
			}
		});
	}
	
	public void resetSettings() { settings.set(new TimeTableAppSettings.Builder().build()); }
	
	public Observable<TimeTableAppSettings> getSettings() { return settings; }
	
	public LanguageManager getLanguageManager() { return languageManager; }
	
	public void setLanguage(String key) { settings.set(settings.get().with().language(key).build()); }
	
	public ReadOnlyObservable<Language> getLanguage() { return language; }
	
	public ReadOnlyObservable<DateTimeFormatter> getDateFormatter() { return dateFormatter; }
	
	public ReadOnlyObservable<DateTimeFormatter> getTimeFormatter() { return timeFormatter; }
	
	public ReadOnlyObservable<DateTimeFormatter> getDateTimeFormatter() { return dateTimeFormatter; }
	
	public FileSaveState getFileSaveState() { return fileSaveState; }
	
	private Option<TimeTableAppSettings> loadSettings() {
		if (Files.exists(savedSettingsPath)) {
			try (BufferedReader reader = Files.newBufferedReader(savedSettingsPath, StandardCharsets.UTF_8)) {
				return Option.of(GSON.fromJson(reader, TimeTableAppSettings.class));
			} catch (JsonParseException | IOException e) {
				e.printStackTrace();
			}
		}
		return Option.empty();
	}
	
	private void saveSettings() {
		try {
			Files.createDirectories(savedSettingsPath.getParent());
			try (BufferedWriter writer = Files.newBufferedWriter(savedSettingsPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
				GSON.toJson(settings.get(), writer);
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	public String localize(String unlocalized) {
		return language.get().localize(unlocalized);
	}
	
	public ReadOnlyObservable<String> localized(String unlocalized) {
		return language.mapStrongly(it -> it.localize(unlocalized));
	}
}
