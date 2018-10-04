package com.fwcd.timetable.model.calendar;

import com.fwcd.timetable.model.calendar.task.TaskModel;
import com.fwcd.timetable.model.calendar.tt.TimeTableEntryModel;

public interface CalendarEntryVisitor {
	default void visitTask(TaskModel task) {}
	
	default void visitTimeTableEntry(TimeTableEntryModel ttEntry) {}
	
	default void visitAppointment(AppointmentModel appointment) {}
}
