package fwcd.timetable.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger LOG = LoggerFactory.getLogger(PluginManager.class);
	private final ObservableList<TimeTableAppPlugin> loadedPlugins = new ObservableList<>();
	private final Observable<PluginJarList> pluginJars = new Observable<>(new PluginJarList());
	private boolean updatingPluginJars = false;

	public void setupPluginJarListeners(TimeTableAppApi api) {
		pluginJars.listenAndFire(jars -> {
			if (!updatingPluginJars) {
				Set<String> removed = new HashSet<>();
				Stream.Builder<URL> urls = Stream.builder();
				boolean foundURLs = false;

				for (String jar : jars) {
					Path path = Paths.get(jar);
					if (Files.exists(path)) {
						try {
							urls.accept(path.toUri().toURL());
							foundURLs = true;
						} catch (MalformedURLException e) {
							LOG.error("Found malformed URL while loading stored plugin jars", e);
						}
					} else {
						LOG.warn("Plugin " + jar + " could not be found, will be removed");
						removed.add(jar);
					}
				}
				
				if (foundURLs) {
					add(new JarPluginBundle(urls.build().toArray(URL[]::new)), api);
				}
				if (!removed.isEmpty()) {
					pluginJars.set(pluginJars.get().without(removed));
				}
			}
		});
	}
	
	public ReadOnlyObservableList<TimeTableAppPlugin> getLoadedPlugins() { return loadedPlugins; }
	
	public Observable<PluginJarList> getPluginJars() { return pluginJars; }
	
	public void add(JarPluginBundle bundle, TimeTableAppApi api) {
		updatingPluginJars = true;
		PluginJarList previousJars = pluginJars.get();
		pluginJars.set(previousJars
			.with(Arrays.stream(bundle.getURLs())
			.map(it -> Paths.get(IOUtils.toURI(it)).toAbsolutePath().toString())
			.filter(it -> !previousJars.contains(it))
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
