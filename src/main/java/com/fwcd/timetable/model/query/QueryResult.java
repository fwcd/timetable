package com.fwcd.timetable.model.query;

import java.util.List;

import com.fwcd.timetable.model.calendar.Appointment;

public interface QueryResult {
	List<Appointment> getAppointments();
}
