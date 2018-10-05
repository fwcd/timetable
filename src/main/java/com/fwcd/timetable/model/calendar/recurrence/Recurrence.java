package com.fwcd.timetable.model.calendar.recurrence;

import java.time.LocalDate;

public interface Recurrence {
	boolean matches(LocalDate date);
}
