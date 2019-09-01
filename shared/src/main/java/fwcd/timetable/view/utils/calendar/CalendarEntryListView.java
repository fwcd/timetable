package fwcd.timetable.view.utils.calendar;

import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import javafx.scene.control.ListView;

public class CalendarEntryListView implements FxView {
	private final ListView<CalendarEntryModel> node;
	
	public CalendarEntryListView(Localizer localizer, TemporalFormatters formatters) {
		node = new ListView<>();
		node.setCellFactory(list -> new CalendarEntryListCell(localizer, formatters));
	}
	
	@Override
	public ListView<CalendarEntryModel> getNode() { return node; }
}
