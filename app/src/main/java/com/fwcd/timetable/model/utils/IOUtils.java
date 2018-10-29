package com.fwcd.timetable.model.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Function;

import com.fwcd.fructose.function.IOFunction;
import com.fwcd.fructose.function.IORunnable;

public final class IOUtils {
	private IOUtils() {}
	
	public static Runnable wrap(IORunnable ioRunnable) {
		return () -> {
			try {
				ioRunnable.run();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}
	
	public static <I, O> Function<I, O> wrap(IOFunction<I, O> ioFunction) {
		return i -> {
			try {
				return ioFunction.apply(i);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}
}
