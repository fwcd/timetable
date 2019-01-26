package fwcd.timetable.model.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Function;

import fwcd.fructose.exception.Rethrow;
import fwcd.fructose.function.IOFunction;
import fwcd.fructose.function.IORunnable;

public final class IOUtils {
	private IOUtils() {}

	public static URI toURI(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new Rethrow(e);
		}
	}
	
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
