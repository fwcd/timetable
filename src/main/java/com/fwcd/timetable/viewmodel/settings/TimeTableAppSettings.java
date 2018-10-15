package com.fwcd.timetable.viewmodel.settings;

import java.io.Serializable;
import java.util.Objects;

/**
 * An immutable settings configuration.
 */
public class TimeTableAppSettings implements Serializable {
	private static final long serialVersionUID = 1382008053572958741L;
	private String language;
	private String dateFormat;
	private String timeFormat;
	private String dateTimeFormat;
	
	private TimeTableAppSettings() {}
	
	public String getLanguage() { return Objects.requireNonNull(language); }
	
	public String getDateFormat() { return Objects.requireNonNull(dateFormat); }
	
	public String getTimeFormat() { return Objects.requireNonNull(timeFormat); }
	
	public String getDateTimeFormat() { return Objects.requireNonNull(dateTimeFormat); }
	
	public Builder with() { return new Builder(this); }
	
	public static class Builder {
		private TimeTableAppSettings instance = new TimeTableAppSettings();
		
		public Builder(TimeTableAppSettings copied) {
			instance.language = copied.language;
			instance.dateFormat = copied.dateFormat;
			instance.timeFormat = copied.timeFormat;
			instance.dateTimeFormat = copied.dateTimeFormat;
		}
		
		public Builder() {
			instance.language = "English";
			instance.dateFormat = "yyyy/MM/dd";
			instance.timeFormat = "HH:mm";
			instance.dateTimeFormat = instance.dateFormat + " " + instance.timeFormat;
		}
		
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
		
		public TimeTableAppSettings build() {
			TimeTableAppSettings result = instance;
			instance = null;
			return result;
		}
	}
}
