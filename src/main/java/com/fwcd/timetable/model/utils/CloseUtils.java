package com.fwcd.timetable.model.utils;

import java.util.Collection;

public final class CloseUtils {
	private CloseUtils() {}
	
	/**
	 * Closes and removes all elements from the Collection.
	 * 
	 * @throws CloseException if any exception occurs while cleaning up
	 */
	public static void clean(Collection<? extends AutoCloseable> resources) {
		try {
			for (AutoCloseable resource : resources) {
				resource.close();
			}
			resources.clear();
		} catch (Exception e) {
			throw new CloseException(e);
		}
	}
}
