package fwcd.timetable.view.sidebar.task;

import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.model.utils.Identified;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TasksView implements FxView {
	private final BorderPane node;
	
	public TasksView(TimeTableAppContext context, CalendarCrateViewModel viewModel) {
		node = new BorderPane();
		
		ComboBox<Identified<CalendarModel>> comboBox = new ComboBox<>();
		comboBox.setCellFactory(it -> new ListCell<Identified<CalendarModel>>() {
			@Override
			protected void updateItem(Identified<CalendarModel> item, boolean empty) {
				super.updateItem(item, empty);
				
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getValue().getName());
				}
			}
		});

		viewModel.getCalendarListeners().add(comboBox.getItems()::setAll);
		comboBox.getItems().setAll(viewModel.getCalendars());
		
		comboBox.valueProperty().addListener((obs, old, selectedCalendar) -> {
			node.setCenter(new TaskManagerView(context, viewModel, selectedCalendar.getId()).getNode());
		});
		
		HBox top = new HBox(
			FxUtils.labelOf(context.localized("calendar").mapStrongly(it -> it + ": ")),
			comboBox
		);
		top.setAlignment(Pos.CENTER_LEFT);
		BorderPane.setMargin(top, new Insets(4, 4, 4, 4));
		
		node.setTop(top);
	}
	
	@Override
	public Node getNode() { return node; }
}
