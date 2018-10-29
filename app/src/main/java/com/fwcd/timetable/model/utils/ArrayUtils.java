package com.fwcd.timetable.model.utils;

import java.util.concurrent.ThreadLocalRandom;

public final class ArrayUtils {
	private ArrayUtils() {}
	
	public static <T> T pickRandom(T[] values) {
		return values[ThreadLocalRandom.current().nextInt(values.length)];
	}
}
