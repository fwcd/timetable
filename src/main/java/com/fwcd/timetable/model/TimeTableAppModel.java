package com.fwcd.timetable.model;

import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.model.tasks.TaskCrateModel;

public class TimeTableAppModel {
	private final CalendarModel calendar = new CalendarModel();
	private final TaskCrateModel taskCrate = new TaskCrateModel();
	
	{
		// calendar.addRandomSampleEntries();
	}
	
	public CalendarModel getCalendar() { return calendar; }
	
	public TaskCrateModel getTaskCrate() { return taskCrate; }
}
