package com.fwcd.timetable.plugin;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.api.plugin.TimeTableAppPlugin;

public class PluginManager {
	private final ObservableList<TimeTableAppPlugin> loadedPlugins = new ObservableList<>();
	
	public ObservableList<TimeTableAppPlugin> getLoadedPlugins() { return loadedPlugins; }
	
	public void add(JarPluginBundle bundle) {
		for (TimeTableAppPlugin plugin : bundle) {
			loadedPlugins.add(plugin);
		}
	}
}
