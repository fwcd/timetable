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
	
	/**
	 * Returns the underlying (immutable) model.
	 */
	public AppointmentModel getModel() { return model; }
	
	/**
	 * Swaps the underlying model against a new one.
	 * 
	 * @param silent - Determines whether listeners should be notified.
	 *                 Should only be false if the view calls this method
	 *                 and would thus cause an infinite recursion.
	 *                 TODO: Provide listener-level support for caller ids
	 *                       and prevent feedback loops
	 */
	public void setModel(AppointmentModel model, Silent silent) {
		this.model = model;
		if (silent.isNot()) {
			fire();
		}
	}
}
