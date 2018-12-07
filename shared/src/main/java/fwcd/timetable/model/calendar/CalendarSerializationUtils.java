package fwcd.timetable.model.calendar;

import fwcd.timetable.model.calendar.recurrence.DailyRecurrence;
import fwcd.timetable.model.calendar.recurrence.ExcludingRecurrence;
import fwcd.timetable.model.calendar.recurrence.MonthlyDayRecurrence;
import fwcd.timetable.model.calendar.recurrence.MonthlyWeekRecurrence;
import fwcd.timetable.model.calendar.recurrence.Recurrence;
import fwcd.timetable.model.calendar.recurrence.WeeklyRecurrence;
import fwcd.timetable.model.calendar.recurrence.YearlyRecurrence;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.model.json.GsonUtils;
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
				.registerSubtype(YearlyRecurrence.class)
				.registerSubtype(ExcludingRecurrence.class))
			.create();
	}
}
