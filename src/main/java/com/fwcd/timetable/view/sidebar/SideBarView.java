package com.fwcd.timetable.view.sidebar;

import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.timetable.model.TimeTableAppModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.sidebar.task.TasksView;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SideBarView implements FxView {
	private final TabPane node;
	
	public SideBarView(TimeTableAppContext context, TimeTableAppModel model) {
		node = new TabPane(
			tabOf(context.localized("tasks"), new TasksView(context, model))
		);
	}
	
	private Tab tabOf(ReadOnlyObservable<String> name, FxView content) {
		return tabOf(name, content.getNode());
	}
	
	private Tab tabOf(ReadOnlyObservable<String> name, Node content) {
		Tab tab = new Tab();
		name.listenAndFire(tab::setText);
		tab.setClosable(false);
		tab.setContent(content);
		return tab;
	}
	
	@Override
	public Node getNode() { return node; }
}
