package fwcd.timetable.model.calendar;

import fwcd.timetable.model.calendar.task.TaskModel;

public interface CalendarEntryVisitor<T> {
	T visitCalendarEntry(CalendarEntryModel entry);

	default T visitTask(TaskModel task) { return visitCalendarEntry(task); }
	
	default T visitAppointment(AppointmentModel appointment) { return visitCalendarEntry(appointment); }
}
