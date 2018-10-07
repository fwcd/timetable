package com.fwcd.timetable.view.sidebar.utils;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.query.QueryOutputNode;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TreeCell;

public class QueryOutputTreeCell extends TreeCell<QueryOutputNode> {
	private final CalendarEntryCell cell;
	
	public QueryOutputTreeCell() {
		cell = new CalendarEntryCell();
	}
	
	@Override
	protected void updateItem(QueryOutputNode item, boolean empty) {
		super.updateItem(item, empty);
		
		if ((item == null) || empty) {
			setText("");
			setGraphic(null);
		} else {
			Option<AppointmentModel> appointment = item.asAppointment();
			if (appointment.isPresent()) {
				cell.updateItem(appointment.unwrap());
		
				setText("");
				setGraphic(cell.getNode());
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			} else {
				setText(item.getName());
				setGraphic(null);
				setContentDisplay(ContentDisplay.TEXT_ONLY);
			}
		}
	}
}
