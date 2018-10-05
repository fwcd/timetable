package com.fwcd.timetable.model;

import java.time.LocalDateTime;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;

public class TimeTableAppModel {
	private final CalendarModel calendar = new CalendarModel();
	
	{
		// calendar.addRandomSampleEntries();
		calendar.getAppointments().add(new AppointmentModel.Builder("Test")
			.start(LocalDateTime.now())
			.end(LocalDateTime.now().plusHours(1))
			.recurrence("d2")
			.build()
		);
	}
	
	public CalendarModel getCalendar() { return calendar; }
}
