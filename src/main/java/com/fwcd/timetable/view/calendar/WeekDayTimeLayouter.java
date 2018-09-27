package com.fwcd.timetable.view.calendar;

import java.time.LocalTime;

public class WeekDayTimeLayouter {
	private final double hourHeight = 40;
	
	public double toPixelY(LocalTime time) {
		return (time.getHour() * hourHeight)
			+ (time.getMinute() * (hourHeight / 60))
			+ (time.getSecond() * (hourHeight / 3600));
	}
}
