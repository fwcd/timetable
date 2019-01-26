package fwcd.timetable.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An immutable list of plugin JAR paths.
 */
public class PluginJarList {
	private List<String> jarPaths = new ArrayList<>();
	
	public PluginJarList(List<String> jarPaths) {
		this.jarPaths = Collections.unmodifiableList(jarPaths);
	}
	
	public PluginJarList(String... jarPaths) {
		this.jarPaths = Arrays.asList(jarPaths);
	}
	
	public PluginJarList with(String... addedPaths) {
		Stream.Builder<String> newPaths = Stream.builder();
		for (String existingPath : jarPaths) {
			newPaths.accept(existingPath);
		}
		for (String addedPath : addedPaths) {
			newPaths.accept(addedPath);
		}
		return new PluginJarList(newPaths.build().collect(Collectors.toList()));
	}
	
	public PluginJarList without(String removedPath) {
		return new PluginJarList(jarPaths.stream().filter(it -> !it.equals(removedPath)).collect(Collectors.toList()));
	}
	
	public List<String> getJarPaths() { return jarPaths; }
	
	public Stream<String> streamJarPaths() { return jarPaths.stream(); }
}
