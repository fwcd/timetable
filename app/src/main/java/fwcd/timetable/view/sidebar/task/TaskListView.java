package fwcd.timetable.view.sidebar.task;

import java.util.List;
import java.util.stream.Collectors;

import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor.TasksOnly;
import fwcd.timetable.model.utils.Contained;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.calendar.CalendarEntryListView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.scene.Node;
import javafx.scene.control.ListView;

public class TaskListView implements FxView {
	private final ListView<CalendarEntryModel> node;
	private final int taskListId;
	
	public TaskListView(TimeTableAppContext context, CalendarCrateViewModel crate, int taskListId) {
		this.taskListId = taskListId;
		node = new CalendarEntryListView(context.getLocalizer(), context.getFormatters()).getNode();
		
		// TODO: Update only when a task in this specific task list changes
		updateVisibleTasks(crate.getEntries());
		crate.getEntryListeners().add(this::updateVisibleTasks);
	}
	
	private void updateVisibleTasks(List<CalendarEntryModel> allEntries) {
		node.getItems().setAll(
			allEntries.stream()
				.flatMap(it -> it.accept(new TasksOnly()).stream())
				.filter(it -> it.getTaskListId() == taskListId)
				.sorted()
				.collect(Collectors.toList())
		);
	}
	
	@Override
	public Node getNode() { return node; }
}
