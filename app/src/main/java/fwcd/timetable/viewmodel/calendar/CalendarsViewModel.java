package fwcd.timetable.viewmodel.calendar;

import java.util.LinkedHashSet;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.structs.ObservableSet;
import fwcd.timetable.model.calendar.CalendarCrateModel;
import fwcd.timetable.model.calendar.CalendarModel;

public class CalendarsViewModel {
	private final CalendarCrateModel model;
	private final ObservableSet<CalendarModel> selectedCalendars = new ObservableSet<>(new LinkedHashSet<>());
	
	private final EventListenerList<CalendarsViewModel> changeListeners = new EventListenerList<>();
	private final EventListenerList<CalendarsViewModel> structuralChangeListeners = new EventListenerList<>();
	
	public CalendarsViewModel(CalendarCrateModel model) {
		this.model = model;
		
		model.getCalendars().listen(selectedCalendars::retainAll);
		model.getOnLoadListeners().add(() -> {
			selectedCalendars.clear();
			selectedCalendars.addAll(model.getCalendars());
		});
		
		setupChangeListeners();
		model.createNewCalendars();
	}
	
	private void setupChangeListeners() {
		model.getStructuralChangeListeners().listen(it -> structuralChangeListeners.fire(this));
		model.getChangeListeners().listen(it -> changeListeners.fire(this));
		selectedCalendars.listen(it -> {
			changeListeners.fire(this);
			structuralChangeListeners.fire(this);
		});
	}
	
	public void addAndSelect(CalendarModel calendar) {
		model.getCalendars().add(calendar);
		selectedCalendars.add(calendar);
	}
	
	public EventListenerList<CalendarsViewModel> getChangeListeners() { return changeListeners; }
	
	public EventListenerList<CalendarsViewModel> getStructuralChangeListeners() { return structuralChangeListeners; }
	
	public ObservableSet<CalendarModel> getSelectedCalendars() { return selectedCalendars; }
	
	public CalendarCrateModel getModel() { return model; }
}
