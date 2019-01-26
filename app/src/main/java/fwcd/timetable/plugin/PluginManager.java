package fwcd.timetable.plugin;

import fwcd.fructose.structs.ObservableList;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import javafx.application.Platform;

/**
 * A facility that manages and initializes application plugins.
 */
public class PluginManager {
	private final ObservableList<TimeTableAppPlugin> loadedPlugins = new ObservableList<>();
	
	public ObservableList<TimeTableAppPlugin> getLoadedPlugins() { return loadedPlugins; }
	
	public void add(JarPluginBundle bundle, TimeTableAppApi api) {
		for (TimeTableAppPlugin plugin : bundle) {
			add(plugin, api);
		}
	}
	
	public void add(TimeTableAppPlugin plugin, TimeTableAppApi api) {
		Platform.runLater(() -> {
			plugin.initialize(api);
			loadedPlugins.add(plugin);
		});
	}
}
