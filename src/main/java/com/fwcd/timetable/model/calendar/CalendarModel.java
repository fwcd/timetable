package com.fwcd.timetable.model.calendar;

import com.fwcd.fructose.structs.ObservableList;

public class CalendarModel {
	private final ObservableList<AppointmentModel> appointments = new ObservableList<>();
	
	public ObservableList<AppointmentModel> getAppointments() { return appointments; }
}
