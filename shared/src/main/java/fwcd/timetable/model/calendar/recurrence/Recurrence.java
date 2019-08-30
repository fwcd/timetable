package fwcd.timetable.model.calendar.recurrence;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

import fwcd.fructose.Option;
import fwcd.timetable.model.language.Language;

public interface Recurrence extends Serializable {
	boolean matches(LocalDate date);
	
	String describeWith(Language language, DateTimeFormatter dateFormatter);
	
	Option<LocalDate> getEnd();
	
	default Set<? extends LocalDate> getExcludes() { return Collections.emptySet(); }
}
