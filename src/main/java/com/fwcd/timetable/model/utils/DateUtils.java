package com.fwcd.timetable.model.utils;

import java.time.LocalDate;

import com.fwcd.fructose.time.LocalDateInterval;

public final class DateUtils {
	private DateUtils() {}
	
	public static LocalDate firstDateIn(LocalDateInterval interval) {
		return interval.getStart();
	}
	
	public static LocalDate lastDateIn(LocalDateInterval interval) {
		return interval.getEnd().minusDays(1);
	}
}
