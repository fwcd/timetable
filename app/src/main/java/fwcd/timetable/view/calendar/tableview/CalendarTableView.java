package fwcd.timetable.view.calendar.tableview;

import java.util.stream.Collectors;

import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.view.utils.FxNavigableView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

import javafx.scene.Node;
import javafx.scene.control.TableView;

public class CalendarTableView implements FxNavigableView {
	private final TableView<AppointmentModel> node;
	
	public CalendarTableView(TimeTableAppContext context, CalendarCrateViewModel calendars) {
		node = new TableView<>();
		calendars.getChangeListeners().add(it -> updateEntries(calendars));
	}
	
	private void updateEntries(CalendarCrateViewModel calendars) {
		node.getItems().setAll(calendars.getSelectedCalendars()
			.stream()
			.flatMap(it -> it.getAppointments().stream())
			.collect(Collectors.toList())
		);
	}

	@Override
	public Node getContentNode() { return node; }
}
