package com.fwcd.timetable.viewmodel.settings;

import java.io.Serializable;
import java.util.Objects;

/**
 * An immutable settings configuration.
 */
public class TimeTableAppSettings implements Serializable {
	private static final long serialVersionUID = 1382008053572958741L;
	private String language = "English";
	private String dateFormat = "dd.MM.yyyy";
	private String timeFormat = "HH:mm";
	private String dateTimeFormat = dateFormat + " " + timeFormat;
	private String yearMonthFormat = "MM.yyyy";
	
	public TimeTableAppSettings() {}
	
	public String getLanguage() { return Objects.requireNonNull(language); }
	
	public String getDateFormat() { return Objects.requireNonNull(dateFormat); }
	
	public String getTimeFormat() { return Objects.requireNonNull(timeFormat); }
	
	public String getDateTimeFormat() { return Objects.requireNonNull(dateTimeFormat); }
	
	public String getYearMonthFormat() { return Objects.requireNonNull(yearMonthFormat); }
	
	public Builder with() { return new Builder(this); }
	
	public static class Builder {
		private TimeTableAppSettings instance = new TimeTableAppSettings();
		
		public Builder(TimeTableAppSettings copied) {
			instance.language = copied.language;
			instance.dateFormat = copied.dateFormat;
			instance.timeFormat = copied.timeFormat;
			instance.dateTimeFormat = copied.dateTimeFormat;
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
		
		public TimeTableAppSettings build() {
			TimeTableAppSettings result = instance;
			instance = null;
			return result;
		}
	}
}
