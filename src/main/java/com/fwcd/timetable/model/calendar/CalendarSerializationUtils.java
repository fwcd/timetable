package com.fwcd.timetable.model.calendar;

import com.fwcd.timetable.model.calendar.recurrence.Recurrence;
import com.fwcd.timetable.model.utils.GsonUtils;
import com.google.gson.Gson;

public class CalendarSerializationUtils {
	private CalendarSerializationUtils() {}
	
	public static Gson newGson() {
		return GsonUtils.newGson(Recurrence.class);
	}
}
