package com.fwcd.timetable.model;

import java.time.LocalDateTime;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;

public class TimeTableAppModel {
	private final CalendarModel calendar = new CalendarModel();
	
	{
		// DEBUG:
		for (int i = 0; i < 24; i++) {
			calendar.getAppointments().add(new AppointmentModel.Builder("Test")
				.start(LocalDateTime.now().plusHours(i))
				.end(LocalDateTime.now().plusHours(i + 1))
				.build()
			);
		}
	}
	
	public CalendarModel getCalendar() { return calendar; }
}
