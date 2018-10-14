package com.fwcd.timetable.viewmodel.calendar;

import java.util.LinkedHashSet;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.fructose.structs.ObservableSet;
import com.fwcd.timetable.model.calendar.CalendarCrateModel;
import com.fwcd.timetable.model.calendar.CalendarModel;

public class CalendarsViewModel {
	private final CalendarCrateModel model;
	private final ObservableSet<CalendarModel> selectedCalendars = new ObservableSet<>(new LinkedHashSet<>());
	
	private final EventListenerList<CalendarsViewModel> structuralChangeListeners = new EventListenerList<>();
	
	public CalendarsViewModel(CalendarCrateModel model) {
		this.model = model;
		
		setupChangeListeners();
		addAndSelect(new CalendarModel("Calendar"));
	}
	
	private void setupChangeListeners() {
		model.getStructuralChangeListeners().listen(it -> structuralChangeListeners.fire(this));
		selectedCalendars.listen(it -> structuralChangeListeners.fire(this));
	}
	
	public void addAndSelect(CalendarModel calendar) {
		model.getCalendars().add(calendar);
		selectedCalendars.add(calendar);
	}
	
	public EventListenerList<CalendarsViewModel> getStructuralChangeListeners() { return structuralChangeListeners; }
	
	public ObservableSet<CalendarModel> getSelectedCalendars() { return selectedCalendars; }
	
	public CalendarCrateModel getModel() { return model; }
}
