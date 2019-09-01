package fwcd.timetable.view.calendar.listview;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor.AppointmentsOnly;
import fwcd.timetable.view.utils.FxNavigableView;
import fwcd.timetable.view.utils.calendar.CalendarEntryListView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.scene.Node;

public class CalendarListView implements FxNavigableView {
	private final Node node;
	private final CalendarEntryListView entries;
	
	public CalendarListView(TimeTableAppContext context, CalendarCrateViewModel crate) {
		entries = new CalendarEntryListView(context.getLocalizer(), context.getFormatters(), crate);
		node = entries.getNode();
		
		crate.getEntryListeners().add(it -> updateEntries(it, crate));
		updateEntries(crate.getEntries(), crate);
	}
	
	private void updateEntries(Collection<CalendarEntryModel> entryModels, CalendarCrateViewModel crate) {
		Set<Integer> selected = crate.getSelectedCalendarIds();
		entries.getNode().getItems().setAll(entryModels.stream()
			.filter(it -> selected.contains(it.getCalendarId()))
			.flatMap(it -> it.accept(new AppointmentsOnly()).stream())
			.sorted()
			.collect(Collectors.toList())
		);
	}
	
	@Override
	public Node getContentNode() { return node; }
}
