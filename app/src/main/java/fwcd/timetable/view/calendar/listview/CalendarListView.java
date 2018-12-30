package fwcd.timetable.view.calendar.listview;

import java.util.stream.Collectors;

import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.utils.Contained;
import fwcd.timetable.view.utils.FxNavigableView;
import fwcd.timetable.view.utils.calendar.CalendarEntryListView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;

public class CalendarListView implements FxNavigableView {
	private final Node node;
	private final CalendarEntryListView entries;
	
	public CalendarListView(TimeTableAppContext context, CalendarsViewModel calendars) {
		entries = new CalendarEntryListView(context);
		node = entries.getNode();
		
		calendars.getChangeListeners().add(it -> updateEntries(calendars));
		updateEntries(calendars);
	}
	
	private void updateEntries(CalendarsViewModel calendars) {
		entries.getNode().getItems().setAll(calendars.getSelectedCalendars()
			.stream()
			.flatMap(cal -> cal.getAppointments().stream()
				.sorted()
				.map(app -> new Contained<CalendarEntryModel>(app, cal.getAppointments())))
			.collect(Collectors.toList())
		);
	}
	
	@Override
	public Node getContentNode() { return node; }
}