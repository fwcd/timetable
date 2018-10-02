package com.fwcd.timetable.model;

import java.util.ArrayList;
import java.util.List;

import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.model.tasks.TaskListModel;

public class TimeTableAppModel {
	private final CalendarModel calendar = new CalendarModel();
	private final List<TaskListModel> taskLists = new ArrayList<>();
	
	public CalendarModel getCalendar() { return calendar; }
	
	public List<TaskListModel> getTaskLists() { return taskLists; }
}
