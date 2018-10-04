package com.fwcd.timetable.view.sidebar.task;

import java.util.List;

import com.fwcd.timetable.model.calendar.task.TaskListModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class TaskListsView implements FxView {
	private final Accordion node;
	private final TaskCrateViewModel crate;
	
	public TaskListsView(TaskCrateViewModel crate) {
		this.crate = crate;
		
		node = new Accordion();
		crate.getModel().getLists().listenAndFire(this::setVisibleLists);
		
		if (!node.getPanes().isEmpty()) {
			node.setExpandedPane(node.getPanes().get(0));
		}
	}
	
	private void setVisibleLists(List<TaskListModel> lists) {
		node.getPanes().clear();
		
		for (TaskListModel list : lists) {
			TitledPane titledPane = wrapListInTitledPane(new TaskListView(list));
			node.expandedPaneProperty().addListener((obs, old, newValue) -> {
				if ((newValue != null) && newValue.equals(titledPane)) {
					crate.select(list);
				}
			});
			node.getPanes().add(titledPane);
		}
	}
	
	private TitledPane wrapListInTitledPane(TaskListView list) {
		TitledPane pane = new TitledPane();
		list.getModel().getName().listenAndFire(pane::setText);
		pane.setContent(list.getNode());
		return pane;
	}
	
	@Override
	public Node getNode() { return node; }
}
