package fwcd.timetable.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An immutable list of plugin JAR paths.
 */
public class PluginJarList implements Iterable<String> {
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
	
	public PluginJarList without(Set<String> removedPaths) {
		return new PluginJarList(jarPaths.stream().filter(it -> !removedPaths.contains(it)).collect(Collectors.toList()));
	}
	
	public List<String> asList() { return jarPaths; }
	
	public Stream<String> stream() { return jarPaths.stream(); }

	@Override
	public Iterator<String> iterator() { return jarPaths.iterator(); }
}
