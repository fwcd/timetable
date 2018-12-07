package fwcd.timetable.model.calendar;

import fwcd.timetable.model.calendar.task.TaskModel;

public interface CalendarEntryVisitor {
	default void visitTask(TaskModel task) {}
	
	default void visitAppointment(AppointmentModel appointment) {}
}
