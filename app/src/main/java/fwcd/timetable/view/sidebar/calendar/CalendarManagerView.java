package fwcd.timetable.view.sidebar.calendar;

import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.model.utils.Identified;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class CalendarManagerView implements FxView {
	private final BorderPane node;
	
	private final CalendarCrateViewModel viewModel;
	private final ListView<Identified<CalendarModel>> calendarList;
	
	public CalendarManagerView(TimeTableAppContext context, CalendarCrateViewModel viewModel) {
		this.viewModel = viewModel;
		
		calendarList = new ListView<>();
		calendarList.setEditable(true);
		calendarList.setCellFactory(list -> new CalendarManagerListCell(context, viewModel));
		calendarList.getItems().setAll(viewModel.getCalendars());
		viewModel.getCalendarListeners().add(calendarList.getItems()::setAll);
		
		HBox controls = new HBox(
			FxUtils.buttonOf(context.localized("newcalendar"), this::createCalendar)
		);
		BorderPane.setMargin(controls, new Insets(4, 4, 4, 4));
		
		node = new BorderPane();
		node.setTop(controls);
		node.setCenter(calendarList);
	}
	
	private void createCalendar() {
		viewModel.select(viewModel.add(new CalendarModel("")));
		calendarList.edit(calendarList.getItems().size() - 1);
	}
	
	@Override
	public Node getNode() { return node; }
}
