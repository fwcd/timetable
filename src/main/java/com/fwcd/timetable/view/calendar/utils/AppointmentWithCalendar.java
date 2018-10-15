package com.fwcd.timetable.view.calendar.utils;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;

public class AppointmentWithCalendar {
	private final AppointmentModel appointment;
	private final CalendarModel calendar;
	
	public AppointmentWithCalendar(AppointmentModel appointment, CalendarModel calendar) {
		this.appointment = appointment;
		this.calendar = calendar;
	}
	
	public AppointmentModel getAppointment() { return appointment; }
	
	public CalendarModel getCalendar() { return calendar; }
}
