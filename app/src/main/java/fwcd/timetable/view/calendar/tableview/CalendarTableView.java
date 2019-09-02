package fwcd.timetable.view.calendar.tableview;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor.AppointmentsOnly;
import fwcd.timetable.view.utils.FxNavigableView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.scene.Node;
import javafx.scene.control.TableView;

public class CalendarTableView implements FxNavigableView {
	private final TableView<AppointmentModel> node;
	
	public CalendarTableView(TimeTableAppContext context, CalendarCrateViewModel crate) {
		node = new TableView<>();
		crate.getVisibleEntryListeners().add(it -> updateEntries(it, crate));
	}
	
	private void updateEntries(Collection<CalendarEntryModel> entryModels, CalendarCrateViewModel crate) {
		Set<Integer> selected = crate.getSelectedCalendarIds();
		node.getItems().setAll(entryModels
			.stream()
			.filter(it -> selected.contains(it.getCalendarId()))
			.flatMap(it -> it.accept(new AppointmentsOnly()).stream())
			.collect(Collectors.toList())
		);
	}

	@Override
	public Node getContentNode() { return node; }
}
