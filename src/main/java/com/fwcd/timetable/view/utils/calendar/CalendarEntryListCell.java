package com.fwcd.timetable.view.utils.calendar;

import com.fwcd.timetable.model.calendar.CalendarEntryModel;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;

public class CalendarEntryListCell extends ListCell<CalendarEntryModel> {
	private final CalendarEntryCell cell;
	
	public CalendarEntryListCell() {
		cell = new CalendarEntryCell();
		
		setGraphic(cell.getNode());
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	}
	
	@Override
	protected void updateItem(CalendarEntryModel item, boolean empty) {
		super.updateItem(item, empty);
		if ((item == null) || empty) {
			setGraphic(null);
		} else {
			cell.updateItem(item);
			setGraphic(cell.getNode());
		}
	}
}
