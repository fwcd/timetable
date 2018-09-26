package com.fwcd.timetable.view.calendar;

import java.time.LocalDate;
import java.util.List;

import com.fwcd.fructose.math.IntRange;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.fructose.structs.events.ListModifyEvent;
import com.fwcd.timetable.model.calendar.CalendarEvent;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;
import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.Calendar;
import com.jibbow.fastis.WeekView;
import com.jibbow.fastis.util.TimeInterval;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class CalendarView implements FxView {
	private final Pane node;
	private final Calendar viewModel;
	
	public CalendarView(CalendarModel model) {
		ObservableList<CalendarEvent> modelEvents = model.getEvents();
		
		viewModel = new Calendar(modelEvents.stream().map(this::toAppointment).toArray(Appointment[]::new));
		node = new WeekView(LocalDate.now(), viewModel);
		
		model.getEvents().listenForModifications(this::onModelChange);
	}
	
	private void onModelChange(ListModifyEvent<CalendarEvent> event) {
		IntRange range = event.getIndices();
		List<Appointment> section = viewModel.subList(range.getStart(), range.getEnd());
		section.clear();
		event.getElements().stream().map(this::toAppointment).forEach(section::add);
	}
	
	private Appointment toAppointment(CalendarEvent event) {
		return new Appointment(new TimeInterval(event.getStart(), event.getEnd()), event.getName());
	}
	
	@Override
	public Parent getNode() { return node; }
}
