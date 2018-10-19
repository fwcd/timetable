package com.fwcd.timetable.viewmodel.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.fwcd.fructose.Listenable;
import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.fructose.function.Subscription;

public class FileSaveManager {
	private final Charset charset;
	private final Function<? super Reader, ? extends Listenable<?>> loader;
	private final Consumer<? super Writer> saver;
	private final Supplier<Option<Path>> savePathProvider;
	private final Supplier<Option<Path>> openPathProvider;
	private final Observable<Boolean> hasUnsavedData = new Observable<>(false);
	private final Observable<Option<Path>> currentPath = new Observable<>(Option.empty());
	private final ReadOnlyObservable<Option<String>> currentTitle = currentPath.mapStrongly(it -> it.map(v -> v.getFileName().toString()));
	private Option<Subscription> valueSubscription = Option.empty();
	
	public FileSaveManager(
		Function<? super Reader, ? extends Listenable<?>> loader,
		Consumer<? super Writer> saver,
		Supplier<Option<Path>> savePathProvider,
		Supplier<Option<Path>> openPathProvider,
		Charset charset
	) {
		this.loader = loader;
		this.saver = saver;
		this.savePathProvider = savePathProvider;
		this.openPathProvider = openPathProvider;
		this.charset = charset;
	}
	
	public void open() throws IOException {
		Option<? extends Path> path = openPathProvider.get();
		if (path.isPresent()) {
			openFrom(path.unwrap());
		}
	}
	
	public void save() throws IOException {
		Option<? extends Path> path = currentPath.get().or(savePathProvider);
		if (path.isPresent()) {
			saveTo(path.unwrap());
		}
	}
	
	public void saveAs() throws IOException {
		Option<? extends Path> path = savePathProvider.get();
		if (path.isPresent()) {
			saveTo(path.unwrap());
		}
	}
	
	private void openFrom(Path path) throws IOException {
		currentPath.set(Option.of(path));
		
		try (Reader reader = Files.newBufferedReader(path, charset)) {
			Listenable<?> value = loader.apply(reader);
			valueSubscription.ifPresent(Subscription::unsubscribe);
			valueSubscription = Option.of(value.subscribe(it -> hasUnsavedData.set(true)));
		}
	}
	
	private void saveTo(Path path) throws IOException {
		try (Writer writer = Files.newBufferedWriter(path, charset)) {
			saver.accept(writer);
			hasUnsavedData.set(false);
		}
	}
	
	public Observable<Boolean> hasUnsavedData() { return hasUnsavedData; }
	
	public Observable<Option<Path>> getCurrentPath() { return currentPath; }
	
	public ReadOnlyObservable<Option<String>> getCurrentTitle() { return currentTitle; }
}
