package com.fwcd.timetable.view.sidebar.task;

import com.fwcd.timetable.model.TimeTableAppModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TasksView implements FxView {
	private final BorderPane node;
	
	public TasksView(TimeTableAppContext context, TimeTableAppModel model) {
		node = new BorderPane();
		
		ComboBox<CalendarModel> comboBox = FxUtils.comboBoxOfObservable(model.getCalendarCrate().getCalendars());
		comboBox.valueProperty().addListener((obs, old, selectedCalendar) -> {
			node.setCenter(new TaskCrateView(context, selectedCalendar.getTaskCrate()).getNode());
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
