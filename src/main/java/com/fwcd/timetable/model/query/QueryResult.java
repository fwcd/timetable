package com.fwcd.timetable.model.query;

import java.util.List;

import com.fwcd.timetable.model.calendar.AppointmentModel;

public interface QueryResult {
	List<AppointmentModel> getAppointments();
}
