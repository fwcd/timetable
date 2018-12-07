package fwcd.timetable.model.calendar;

import fwcd.fructose.Observable;

public interface CalendarEntryModel {
	String getType();
	
	Observable<String> getName();
	
	Observable<String> getDescription();
	
	void accept(CalendarEntryVisitor visitor);
}
