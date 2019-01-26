package fwcd.timetable.viewmodel;

import java.time.format.DateTimeFormatter;

public interface TemporalFormatters {
	DateTimeFormatter getDateFormatter();
	
	DateTimeFormatter getTimeFormatter();
	
	DateTimeFormatter getDateTimeFormatter();
}
