package com.fwcd.timetable.view.sidebar.task;

import com.fwcd.timetable.model.TimeTableAppModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

public class TasksView implements FxView {
	private final BorderPane node;
	
	public TasksView(TimeTableAppContext context, TimeTableAppModel model) {
		node = new BorderPane();
		
		ComboBox<CalendarModel> comboBox = FxUtils.comboBoxOfObservable(model.getCalendars());
		comboBox.valueProperty().addListener((obs, old, selectedCalendar) -> {
			node.setCenter(new TaskCrateView(context, selectedCalendar.getTaskCrate()).getNode());
		});
		BorderPane.setMargin(comboBox, new Insets(4, 4, 4, 4));
		node.setTop(comboBox);
	}
	
	@Override
	public Node getNode() { return node; }
}
