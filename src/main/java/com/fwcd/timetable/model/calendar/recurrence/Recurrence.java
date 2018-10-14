package com.fwcd.timetable.model.calendar.recurrence;

import java.io.Serializable;
import java.time.LocalDate;

public interface Recurrence extends Serializable {
	boolean matches(LocalDate date);
}
