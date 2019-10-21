package fwcd.timetable.model.calendar;

import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import fwcd.timetable.model.calendar.recurrence.DailyRecurrence;
import fwcd.timetable.model.calendar.recurrence.ExcludingRecurrence;
import fwcd.timetable.model.calendar.recurrence.MonthlyDayRecurrence;
import fwcd.timetable.model.calendar.recurrence.MonthlyWeekRecurrence;
import fwcd.timetable.model.calendar.recurrence.Recurrence;
import fwcd.timetable.model.calendar.recurrence.WeeklyRecurrence;
import fwcd.timetable.model.calendar.recurrence.YearlyRecurrence;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.model.json.GsonConfigurator;

/**
 * A configurator that registers polymorphic calendar
 * classes for serialization.
 */
public class CalendarGsonConfigurator implements GsonConfigurator {
	public void apply(GsonBuilder builder) {
		builder
			.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(CalendarEntryModel.class)
				.registerSubtype(AppointmentModel.class)
				.registerSubtype(TaskModel.class))
			.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Recurrence.class)
				.registerSubtype(DailyRecurrence.class)
				.registerSubtype(MonthlyDayRecurrence.class)
				.registerSubtype(MonthlyWeekRecurrence.class)
				.registerSubtype(WeeklyRecurrence.class)
				.registerSubtype(YearlyRecurrence.class)
				.registerSubtype(ExcludingRecurrence.class));
	}
}
