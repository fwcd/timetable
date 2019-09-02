package fwcd.timetable.view.sidebar.task;

import fwcd.timetable.model.calendar.task.TaskListModel;
import fwcd.timetable.model.utils.Identified;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class TaskListsView implements FxView {
	private final Accordion node;
	private final TaskManagerViewModel manager;
	
	private final TimeTableAppContext context;
	
	public TaskListsView(TimeTableAppContext context, TaskManagerViewModel manager) {
		this.manager = manager;
		this.context = context;
		
		node = new Accordion();
		setVisibleLists(manager.getCrate().getTaskLists());
		manager.getCrate().getTaskListListeners().add(this::setVisibleLists);
		
		if (!node.getPanes().isEmpty()) {
			node.setExpandedPane(node.getPanes().get(0));
		}
	}
	
	private void setVisibleLists(Iterable<Identified<TaskListModel>> lists) {
		node.getPanes().clear();
		
		for (Identified<TaskListModel> list : lists) {
			TitledPane titledPane = wrapListInTitledPane(new TaskListView(context, manager.getCrate(), list.getId(), list.getValue().getCalendarId()), list.getValue().getName());
			node.expandedPaneProperty().addListener((obs, old, newValue) -> {
				if ((newValue != null) && newValue.equals(titledPane)) {
					manager.select(list.getId());
				}
			});
			node.getPanes().add(titledPane);
		}
	}
	
	private TitledPane wrapListInTitledPane(TaskListView list, String title) {
		TitledPane pane = new TitledPane();
		pane.setText(title);
		pane.setContent(list.getNode());
		return pane;
	}
	
	@Override
	public Node getNode() { return node; }
}
