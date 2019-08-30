package fwcd.timetable.viewmodel.calendar;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.viewmodel.Responder;
import fwcd.timetable.viewmodel.Silent;

public class AppointmentViewModel implements Responder {
	private AppointmentModel model;
	
	private final EventListenerList<AppointmentViewModel> changeListeners = new EventListenerList<>();
	private Option<Responder> nextResponder = Option.empty();
	
	public AppointmentViewModel(AppointmentModel model) {
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
	
	public EventListenerList<AppointmentViewModel> getChangeListeners() { return changeListeners; }
	
	public AppointmentModel getModel() { return model; }
	
	public void setModel(AppointmentModel model, Silent silent) {
		this.model = model;
		if (silent.isNot()) {
			fire();
		}
	}
}
