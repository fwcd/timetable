package fwcd.timetable.viewmodel.calendar;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.viewmodel.Responder;

public class CalendarViewModel implements Responder {
	private final CalendarModel model;

	private final EventListenerList<CalendarViewModel> changeListeners = new EventListenerList<>();
	private Option<Responder> nextResponder = Option.empty();
	
	public CalendarViewModel(CalendarModel model) {
		this.model = model;
	}
	
	@Override
	public void setNextResponder(Option<Responder> responder) {
		nextResponder = responder;
	}
	
	@Override
	public void fire() {
		changeListeners.fire(this);
	}
	
	/**
	 * Fetches the underlying model. It is NOT intended to be
	 * mutated (use the viewmodels instead).
	 */
	public CalendarModel getModel() { return model; }
	
	public void remove(AppointmentModel appointment) {
		model.getAppointments().remove(appointment);
		fire();
	}
	
	public EventListenerList<CalendarViewModel> getChangeListeners() { return changeListeners; }
}
