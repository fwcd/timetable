package fwcd.timetable.model.calendar;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.task.TaskModel;

public interface CalendarEntryVisitor<T> {
	T visitCalendarEntry(CalendarEntryModel entry);

	default T visitTask(TaskModel task) { return visitCalendarEntry(task); }
	
	default T visitAppointment(AppointmentModel appointment) { return visitCalendarEntry(appointment); }
	
	interface ReturningNullByDefault<T> extends CalendarEntryVisitor<T> {
		@Override
		default T visitCalendarEntry(CalendarEntryModel entry) { return null; }
	}
	
	interface ReturningEmptyByDefault<T> extends CalendarEntryVisitor<Option<T>> {
		@Override
		default Option<T> visitCalendarEntry(CalendarEntryModel entry) { return Option.empty(); }
	}
	
	class TasksOnly implements CalendarEntryVisitor.ReturningEmptyByDefault<TaskModel> {
		@Override
		public Option<TaskModel> visitTask(TaskModel task) { return Option.of(task); }
	}
	
	class AppointmentsOnly implements CalendarEntryVisitor.ReturningEmptyByDefault<AppointmentModel> {
		@Override
		public Option<AppointmentModel> visitAppointment(AppointmentModel appointment) { return Option.of(appointment); }
	}
}
