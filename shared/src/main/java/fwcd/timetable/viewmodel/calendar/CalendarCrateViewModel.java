package fwcd.timetable.viewmodel.calendar;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarCrateModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.viewmodel.Responder;

public class CalendarCrateViewModel implements Responder {
	private final CalendarCrateModel model;
	private final Set<CalendarModel> selectedCalendars = new LinkedHashSet<>();
	
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
		selectedCalendars.add(calendar);
		fire();
	}
	
	public EventListenerList<CalendarCrateViewModel> getChangeListeners() { return changeListeners; }
	
	public Set<CalendarModel> getSelectedCalendars() { return Collections.unmodifiableSet(selectedCalendars); }
}
