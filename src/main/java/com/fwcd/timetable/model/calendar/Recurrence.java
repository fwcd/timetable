package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;

public interface Recurrence {
	boolean matches(LocalDate date);
}
