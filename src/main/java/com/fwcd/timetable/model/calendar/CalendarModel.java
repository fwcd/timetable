package com.fwcd.timetable.model.calendar;

import com.fwcd.fructose.structs.ObservableList;

public class CalendarModel {
	private final ObservableList<Appointment> appointments = new ObservableList<>();
	
	public ObservableList<Appointment> getAppointments() { return appointments; }
}
