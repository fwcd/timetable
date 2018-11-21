package com.fwcd.timetable.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ServiceLoader;

import com.fwcd.timetable.plugin.TimeTableAppPlugin;

public class JarPluginBundle implements Iterable<TimeTableAppPlugin> {
	private final ServiceLoader<TimeTableAppPlugin> serviceLoader;
	
	public JarPluginBundle(URL... urls) {
		ClassLoader classLoader = URLClassLoader.newInstance(urls, TimeTableAppPlugin.class.getClassLoader());
		serviceLoader = ServiceLoader.load(TimeTableAppPlugin.class, classLoader);
	}
	
	@Override
	public Iterator<TimeTableAppPlugin> iterator() { return serviceLoader.iterator(); }
}
