package com.fwcd.timetable.plugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.fwcd.fructose.concurrent.ClosingExecutor;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.viewmodel.TimeTableAppApi;

/**
 * A facility that manages and initializes application plugins.
 */
public class PluginManager {
	private final ObservableList<TimeTableAppPlugin> loadedPlugins = new ObservableList<>();
	
	public ObservableList<TimeTableAppPlugin> getLoadedPlugins() { return loadedPlugins; }
	
	public void add(JarPluginBundle bundle, TimeTableAppApi api) {
		try (ClosingExecutor executor = new ClosingExecutor(Executors.newCachedThreadPool())) {
			for (TimeTableAppPlugin plugin : bundle) {
				add(plugin, api, executor);
			}
		}
	}
	
	public void add(TimeTableAppPlugin plugin, TimeTableAppApi api) {
		try (ClosingExecutor executor = new ClosingExecutor(Executors.newSingleThreadExecutor())) {
			add(plugin, api, executor);
		}
	}
	
	private void add(TimeTableAppPlugin plugin, TimeTableAppApi api, Executor executor) {
		executor.execute(() -> plugin.initialize(api));
		loadedPlugins.add(plugin);
	}
}
