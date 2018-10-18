package com.fwcd.timetable.model.calendar;

import com.fwcd.timetable.model.calendar.recurrence.DailyRecurrence;
import com.fwcd.timetable.model.calendar.recurrence.MonthlyDayRecurrence;
import com.fwcd.timetable.model.calendar.recurrence.MonthlyWeekRecurrence;
import com.fwcd.timetable.model.calendar.recurrence.Recurrence;
import com.fwcd.timetable.model.calendar.recurrence.WeeklyRecurrence;
import com.fwcd.timetable.model.calendar.recurrence.YearlyRecurrence;
import com.fwcd.timetable.model.calendar.task.TaskModel;
import com.fwcd.timetable.model.json.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class CalendarSerializationUtils {
	private CalendarSerializationUtils() {}
	
	public static Gson newGson() {
		return GsonUtils.buildGson()
			.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(CalendarEntryModel.class)
				.registerSubtype(AppointmentModel.class)
				.registerSubtype(TaskModel.class))
			.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Recurrence.class)
				.registerSubtype(DailyRecurrence.class)
				.registerSubtype(MonthlyDayRecurrence.class)
				.registerSubtype(MonthlyWeekRecurrence.class)
				.registerSubtype(WeeklyRecurrence.class)
				.registerSubtype(YearlyRecurrence.class))
			.create();
	}
}
