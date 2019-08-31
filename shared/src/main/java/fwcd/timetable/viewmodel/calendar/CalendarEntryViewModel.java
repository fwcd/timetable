package fwcd.timetable.viewmodel.calendar;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor;
import fwcd.timetable.viewmodel.Responder;

public class CalendarEntryViewModel implements Responder {
	private final CalendarEntryModel model;
	
	private final EventListenerList<CalendarEntryViewModel> changeListeners = new EventListenerList<>();
	private Option<Responder> nextResponder = Option.empty();
	
	public CalendarEntryViewModel(CalendarEntryModel model) {
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
	
	public <T> T accept(CalendarEntryViewModelVisitor<T> visitor) {
		return model.accept(new CalendarEntryVisitor<T>() {
			@Override
			public T visitCalendarEntry(CalendarEntryModel entry) {
				return visitor.visitCalendarEntryViewModel(this);
			}
		});
	}
}
