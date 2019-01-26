package fwcd.timetable.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ServiceLoader;

import fwcd.timetable.plugin.TimeTableAppPlugin;

/**
 * A JAR-packaged plugin bundle that can hold multiple
 * plugins inside the META-INF/services directory.
 */
public class JarPluginBundle implements Iterable<TimeTableAppPlugin> {
	private final ServiceLoader<TimeTableAppPlugin> serviceLoader;
	private final URL[] urls;
	
	public JarPluginBundle(URL... urls) {
		this.urls = urls;
		
		ClassLoader classLoader = URLClassLoader.newInstance(urls, TimeTableAppPlugin.class.getClassLoader());
		serviceLoader = ServiceLoader.load(TimeTableAppPlugin.class, classLoader);
	}
	
	public URL[] getURLs() { return urls; }
	
	@Override
	public Iterator<TimeTableAppPlugin> iterator() { return serviceLoader.iterator(); }
}
