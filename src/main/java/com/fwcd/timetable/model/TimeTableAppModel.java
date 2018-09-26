package com.fwcd.timetable.model;

import java.time.LocalDateTime;

import com.fwcd.timetable.model.calendar.CalendarEvent;
import com.fwcd.timetable.model.calendar.CalendarModel;

public class TimeTableAppModel {
	private final CalendarModel calendar = new CalendarModel();
	
	{
		for (int i=0; i<24; i++) {
			calendar.getEvents().add(new CalendarEvent.Builder("Test")
				.start(LocalDateTime.now().plusHours(i))
				.end(LocalDateTime.now().plusHours(i + 1))
				.build()
			);
		}
	}
	
	public CalendarModel getCalendar() { return calendar; }
}
