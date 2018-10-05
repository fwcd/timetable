package com.fwcd.timetable.view.sidebar.calendar;

import com.fwcd.timetable.model.TimeTableAppModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class CalendarManagerView implements FxView {
	private final Node node;
	
	public CalendarManagerView(TimeTableAppContext context, TimeTableAppModel model) {
		ListView<CalendarModel> calendarList = new ListView<>();
		model.getCalendars().listenAndFire(calendarList.getItems()::setAll);
		
		node = new Pane(
			calendarList
		);
	}
	
	@Override
	public Node getNode() { return node; }
}
