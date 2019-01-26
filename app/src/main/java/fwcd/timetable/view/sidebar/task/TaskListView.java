package fwcd.timetable.view.sidebar.task;

import java.util.List;
import java.util.stream.Collectors;

import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.task.TaskListModel;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.model.utils.Contained;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.calendar.CalendarEntryListView;
import fwcd.timetable.viewmodel.TimeTableAppContext;

import javafx.scene.Node;
import javafx.scene.control.ListView;

public class TaskListView implements FxView {
	private final ListView<Contained<CalendarEntryModel>> node;
	private final TaskListModel model;
	
	public TaskListView(TimeTableAppContext context, TaskListModel model) {
		this.model = model;
		node = new CalendarEntryListView(context.getLocalizer(), context.getFormatters()).getNode();
		model.getTasks().listenAndFire(this::setVisibleTasks);
	}
	
	private void setVisibleTasks(List<TaskModel> tasks) {
		node.getItems().setAll(
			tasks.stream()
				.map(it -> new Contained<CalendarEntryModel>(it, model.getTasks()))
				.collect(Collectors.toList())
		);
	}
	
	public TaskListModel getModel() { return model; }
	
	@Override
	public Node getNode() { return node; }
}
