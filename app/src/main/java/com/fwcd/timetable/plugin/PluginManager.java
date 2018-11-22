package com.fwcd.timetable.plugin;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.viewmodel.TimeTableAppApi;

public class PluginManager {
	private final ObservableList<TimeTableAppPlugin> loadedPlugins = new ObservableList<>();
	
	public ObservableList<TimeTableAppPlugin> getLoadedPlugins() { return loadedPlugins; }
	
	public void add(JarPluginBundle bundle, TimeTableAppApi api) {
		for (TimeTableAppPlugin plugin : bundle) {
			add(plugin, api);
		}
	}
	
	public void add(TimeTableAppPlugin plugin, TimeTableAppApi api) {
		new Thread(() -> plugin.initialize(api), "Plugin initializer").start();
		loadedPlugins.add(plugin);
	}
}
