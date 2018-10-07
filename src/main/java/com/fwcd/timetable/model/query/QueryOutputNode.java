package com.fwcd.timetable.model.query;

import java.util.List;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;

public interface QueryOutputNode {
	Option<CalendarEntryModel> asCalendarEntry();
	
	List<? extends QueryOutputNode> getChilds();
}
