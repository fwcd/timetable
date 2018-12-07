package fwcd.timetable.view.sidebar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fwcd.timetable.plugin.TimeTableAppPlugin;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.NamedFxView;
import fwcd.timetable.view.sidebar.calendar.CalendarsSideView;
import fwcd.timetable.view.sidebar.task.TasksView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SideBarView implements FxView {
	private final TabPane node;
	private final Map<String, Tab> pluginTabs = new HashMap<>();
	
	public SideBarView(TimeTableAppContext context, CalendarsViewModel viewModel) {
		node = new TabPane(
			FxUtils.tabOf(context.localized("calendars"), new CalendarsSideView(context, viewModel)),
			FxUtils.tabOf(context.localized("tasks"), new TasksView(context, viewModel))
		);
			
		context.getPluginManager().getLoadedPlugins().listenAndFire(plugins -> {
			Set<String> removedTabs = new HashSet<>(pluginTabs.keySet());
			
			for (TimeTableAppPlugin plugin : plugins) {
				for (NamedFxView view : plugin.getSidebarViews()) {
					String key = view.getName();
					if (pluginTabs.containsKey(key)) {
						removedTabs.remove(key);
					} else if (view.isNameLocalized()) {
						addPluginTab(key, FxUtils.tabOf(view.getName(), view.getNode()));
					} else {
						addPluginTab(key, FxUtils.tabOf(context.localize(view.getName()), view.getNode()));
					}
				}
			}
			
			for (String tab : removedTabs) {
				removePluginTab(tab);
			}
		});
	}
	
	private void addPluginTab(String key, Tab tab) {
		node.getTabs().add(tab);
		pluginTabs.put(key, tab);
	}
	
	private void removePluginTab(String key) {
		node.getTabs().remove(pluginTabs.remove(key));
	}
	
	@Override
	public Node getNode() { return node; }
}
