package com.fwcd.timetable.model.calendar.recurrence;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fwcd.timetable.model.language.Language;

public interface Recurrence extends Serializable {
	boolean matches(LocalDate date);
	
	String describeWith(Language language, DateTimeFormatter dateFormatter);
}
