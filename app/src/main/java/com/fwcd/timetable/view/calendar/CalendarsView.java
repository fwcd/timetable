package com.fwcd.timetable.view.calendar;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.model.calendar.CalendarSerializationUtils;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.view.calendar.listview.CalendarListView;
import com.fwcd.timetable.view.calendar.monthview.MonthView;
import com.fwcd.timetable.view.calendar.tableview.CalendarTableView;
import com.fwcd.timetable.view.calendar.weekview.WeekView;
import com.fwcd.timetable.api.view.FxView;
import com.fwcd.timetable.view.utils.NavigableTabPane;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import javafx.scene.Node;
import javafx.scene.input.TransferMode;

public class CalendarsView implements FxView {
	private static final Gson GSON = CalendarSerializationUtils.newGson();
	private final Node node;
	private final WeekView weekView;
	private final MonthView monthView;
	private final CalendarListView listView;
	private final CalendarTableView tableView;
	
	public CalendarsView(TimeTableAppContext context, CalendarsViewModel viewModel) {
		weekView = new WeekView(context, viewModel);
		monthView = new MonthView(context, viewModel);
		listView = new CalendarListView(context, viewModel);
		tableView = new CalendarTableView(context, viewModel);
		
		NavigableTabPane tabPane = new NavigableTabPane();
		tabPane.addTab(context.localized("week"), weekView);
		tabPane.addTab(context.localized("month"), monthView);
		tabPane.addTab(context.localized("list"), listView);
		tabPane.addTab(context.localized("table"), tableView);
		node = tabPane.getNode();
		node.setOnDragOver(e -> {
			e.acceptTransferModes(TransferMode.COPY);
			e.consume();
		});
		node.setOnDragDropped(e -> {
			try {
				String raw = e.getDragboard().getString();
				if (raw == null) {
					e.setDropCompleted(false);
				} else {
					AppointmentModel appointment = GSON.fromJson(raw, AppointmentModel.class);
					// TODO: Let the user select a calendar
					Option<CalendarModel> calendar = Option.of(viewModel.getSelectedCalendars().stream().findAny());
					if (calendar.isPresent()) {
						calendar.unwrap().getAppointments().add(appointment);
					}
					e.setDropCompleted(calendar.isPresent());
				}
			} catch (JsonParseException f) {
				// Ignore any dragboard contents that are not a valid
				// JSON appointment
				e.setDropCompleted(false);
			} catch (NullPointerException f) {
				f.printStackTrace();
				e.setDropCompleted(false);
			}
		});
	}
	
	@Override
	public Node getNode() { return node; }
}
