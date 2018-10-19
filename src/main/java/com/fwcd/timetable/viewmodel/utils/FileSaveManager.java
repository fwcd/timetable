package com.fwcd.timetable.viewmodel.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;

import com.fwcd.fructose.Listenable;
import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.fructose.function.Subscription;

public class FileSaveManager<T extends Listenable<?>> {
	private final Charset charset;
	private final Function<? super Reader, ? extends T> loader;
	private final Consumer<? super Writer> saver;
	private final Observable<Boolean> hasUnsavedData = new Observable<>(false);
	private final Observable<Option<Path>> currentPath = new Observable<>(Option.empty());
	private final ReadOnlyObservable<Option<String>> currentTitle = currentPath.mapStrongly(it -> it.map(v -> v.getFileName().toString()));
	private Option<Subscription> valueSubscription = Option.empty();
	
	public FileSaveManager(Function<? super Reader, ? extends T> loader, Consumer<? super Writer> saver, Charset charset) {
		this.loader = loader;
		this.saver = saver;
		this.charset = charset;
	}
	
	public void open(Path path) throws IOException {
		currentPath.set(Option.of(path));
		
		try (Reader reader = Files.newBufferedReader(path, charset)) {
			T value = loader.apply(reader);
			valueSubscription.ifPresent(Subscription::unsubscribe);
			valueSubscription = Option.of(value.subscribe(it -> hasUnsavedData.set(true)));
		}
	}
	
	public void save(Path path) throws IOException {
		try (Writer writer = Files.newBufferedWriter(path, charset)) {
			saver.accept(writer);
			hasUnsavedData.set(false);
		}
	}
	
	public Observable<Boolean> hasUnsavedData() { return hasUnsavedData; }
	
	public Observable<Option<Path>> getCurrentPath() { return currentPath; }
	
	public ReadOnlyObservable<Option<String>> getCurrentTitle() { return currentTitle; }
}
