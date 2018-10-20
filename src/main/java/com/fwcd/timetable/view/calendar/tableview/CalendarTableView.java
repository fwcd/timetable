package com.fwcd.timetable.view.calendar.tableview;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.view.utils.FxNavigableView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;
import javafx.scene.control.TableView;

public class CalendarTableView implements FxNavigableView {
	private final TableView<AppointmentModel> node;
	
	public CalendarTableView(TimeTableAppContext context, CalendarsViewModel viewModel) {
		node = new TableView<>();
	}
	
	@Override
	public Node getContentNode() { return node; }
}