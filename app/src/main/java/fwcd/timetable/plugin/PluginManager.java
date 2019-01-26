package fwcd.timetable.plugin;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import fwcd.fructose.Observable;
import fwcd.fructose.structs.ObservableList;
import fwcd.fructose.structs.ReadOnlyObservableList;
import fwcd.timetable.model.utils.IOUtils;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import javafx.application.Platform;

/**
 * A facility that manages and initializes application plugins.
 */
public class PluginManager {
	private final ObservableList<TimeTableAppPlugin> loadedPlugins = new ObservableList<>();
	private final Observable<PluginJarList> pluginJars = new Observable<>(new PluginJarList());
	private boolean updatingPluginJars = false;
	
	public void setupPluginJarListeners(TimeTableAppApi api) {
		pluginJars.listenAndFire(jars -> {
			if (!updatingPluginJars) {
				setAll(loadedPlugins, api);
			}
		});
	}
	
	public ReadOnlyObservableList<TimeTableAppPlugin> getLoadedPlugins() { return loadedPlugins; }
	
	public Observable<PluginJarList> getPluginJars() { return pluginJars; }
	
	public void add(JarPluginBundle bundle, TimeTableAppApi api) {
		updatingPluginJars = true;
		pluginJars.set(pluginJars.get()
			.with(Arrays.stream(bundle.getURLs())
			.map(it -> Paths.get(IOUtils.toURI(it)))
			.toArray(String[]::new)));
		updatingPluginJars = false;
		
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
	
	public void setAll(List<TimeTableAppPlugin> plugins, TimeTableAppApi api) {
		Platform.runLater(() -> {
			for (TimeTableAppPlugin plugin : plugins) {
				plugin.initialize(api);
			}
			loadedPlugins.set(plugins);
		});
	}
}
