package fwcd.timetable.view.utils.calendar;

import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.utils.Contained;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import javafx.scene.control.ListView;

public class CalendarEntryListView implements FxView {
	private final ListView<Contained<CalendarEntryModel>> node;
	
	public CalendarEntryListView(Localizer localizer, TemporalFormatters formatters) {
		node = new ListView<>();
		node.setCellFactory(list -> new CalendarEntryListCell(localizer, formatters));
	}
	
	@Override
	public ListView<Contained<CalendarEntryModel>> getNode() { return node; }
}
