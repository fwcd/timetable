package fwcd.timetable.view.utils.calendar;

import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.scene.control.ListView;

public class CalendarEntryListView implements FxView {
	private final ListView<CalendarEntryModel> node;
	
	public CalendarEntryListView(Localizer localizer, TemporalFormatters formatters, CalendarCrateViewModel crate) {
		node = new ListView<>();
		node.setCellFactory(list -> new CalendarEntryListCell(localizer, formatters, crate));
	}
	
	@Override
	public ListView<CalendarEntryModel> getNode() { return node; }
}
