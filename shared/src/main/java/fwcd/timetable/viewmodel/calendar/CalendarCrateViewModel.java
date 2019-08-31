package fwcd.timetable.viewmodel.calendar;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarCrateModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.viewmodel.Responder;

public class CalendarCrateViewModel implements Responder {
	private final CalendarCrateModel model;
	private final Map<CalendarModel, CalendarViewModel> selectedCalendars = new HashMap<>();
	
	private final EventListenerList<CalendarCrateViewModel> changeListeners = new EventListenerList<>();
	private Option<Responder> nextResponder = Option.empty();
	
	public CalendarCrateViewModel(CalendarCrateModel model) {
		this.model = model;
		model.createNewCalendars();
	}
	
	@Override
	public void setNextResponder(Option<Responder> responder) {
		nextResponder = responder;
	}
	
	@Override
	public void fire() {
		changeListeners.fire(this);
	}
	
	public void addAndSelect(CalendarModel calendar) {
		model.getCalendars().add(calendar);
		selectedCalendars.put(calendar, new CalendarViewModel(calendar));
		fire();
	}
	
	public EventListenerList<CalendarCrateViewModel> getChangeListeners() { return changeListeners; }
	
	public Collection<CalendarViewModel> getSelectedCalendars() { return Collections.unmodifiableCollection(selectedCalendars.values()); }
}
