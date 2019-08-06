package fwcd.timetable.model.calendar;

public interface CalendarEntryModel {
	String getType();
	
	String getName();
	
	String getDescription();
	
	void accept(CalendarEntryVisitor visitor);
}
