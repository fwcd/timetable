package fwcd.timetable.viewmodel.settings;

import java.io.Serializable;
import java.util.Objects;

import fwcd.timetable.model.language.LanguageKey;
import fwcd.timetable.viewmodel.theme.ThemeKey;

/**
 * An immutable settings configuration.
 */
public class TimeTableAppSettings implements Serializable {
	private static final long serialVersionUID = 1382008053572958741L;
	private String language = LanguageKey.ENGLISH;
	private String dateFormat = "dd.MM.yyyy";
	private String timeFormat = "HH:mm";
	private String dateTimeFormat = dateFormat + " " + timeFormat;
	private String yearMonthFormat = "MM.yyyy";
	private String theme = ThemeKey.LIGHT;
	private boolean prettyPrintJson = false;
	
	public TimeTableAppSettings() {}
	
	public String getLanguage() { return Objects.requireNonNull(language); }
	
	public String getDateFormat() { return Objects.requireNonNull(dateFormat); }
	
	public String getTimeFormat() { return Objects.requireNonNull(timeFormat); }
	
	public String getDateTimeFormat() { return Objects.requireNonNull(dateTimeFormat); }
	
	public String getYearMonthFormat() { return Objects.requireNonNull(yearMonthFormat); }
	
	public String getTheme() { return theme; }
	
	public boolean shouldPrettyPrintJson() { return prettyPrintJson; }
	
	public Builder with() { return new Builder(this); }
	
	public static class Builder {
		private TimeTableAppSettings instance = new TimeTableAppSettings();
		
		public Builder(TimeTableAppSettings copied) {
			instance.language = copied.language;
			instance.dateFormat = copied.dateFormat;
			instance.timeFormat = copied.timeFormat;
			instance.dateTimeFormat = copied.dateTimeFormat;
			instance.yearMonthFormat = copied.yearMonthFormat;
			instance.theme = copied.theme;
			instance.prettyPrintJson = copied.prettyPrintJson;
		}
		
		public Builder() {}
		
		public Builder language(String language) {
			instance.language = language;
			return this;
		}
		
		public Builder dateFormat(String dateFormat) {
			instance.dateFormat = dateFormat;
			return this;
		}
		
		public Builder timeFormat(String timeFormat) {
			instance.timeFormat = timeFormat;
			return this;
		}
		
		public Builder dateTimeFormat(String dateTimeFormat) {
			instance.dateTimeFormat = dateTimeFormat;
			return this;
		}
		
		public Builder yearMonthFormat(String yearMonthFormat) {
			instance.yearMonthFormat = yearMonthFormat;
			return this;
		}
		
		public Builder theme(String theme) {
			instance.theme = theme;
			return this;
		}
		
		public Builder prettyPrintJson(boolean prettyPrintJson) {
			instance.prettyPrintJson = prettyPrintJson;
			return this;
		}
		
		public TimeTableAppSettings build() {
			TimeTableAppSettings result = instance;
			instance = null;
			return result;
		}
	}
}
