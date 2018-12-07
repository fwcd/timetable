package fwcd.timetable.viewmodel.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import fwcd.fructose.Listenable;
import fwcd.fructose.Option;
import fwcd.fructose.function.Subscription;

public class FileSaveManager {
	private final Charset charset;
	private final Function<? super Reader, ? extends Listenable<?>> loader;
	private final Consumer<? super Writer> saver;
	private final Supplier<Option<Path>> savePathProvider;
	private final Supplier<Option<Path>> openPathProvider;
	private final FileSaveState state;
	private Option<Subscription> valueSubscription = Option.empty();
	
	public FileSaveManager(
		Function<? super Reader, ? extends Listenable<?>> loader,
		Consumer<? super Writer> saver,
		Supplier<Option<Path>> savePathProvider,
		Supplier<Option<Path>> openPathProvider,
		FileSaveState state,
		Charset charset
	) {
		this.loader = loader;
		this.saver = saver;
		this.savePathProvider = savePathProvider;
		this.openPathProvider = openPathProvider;
		this.state = state;
		this.charset = charset;
	}
	
	public void open() throws IOException {
		Option<? extends Path> path = openPathProvider.get();
		if (path.isPresent()) {
			openFrom(path.unwrap());
		}
	}
	
	public void save() throws IOException {
		Option<? extends Path> path = state.getCurrentPath().get().or(savePathProvider);
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
		state.getCurrentPath().set(Option.of(path));
		
		try (Reader reader = Files.newBufferedReader(path, charset)) {
			Listenable<?> value = loader.apply(reader);
			valueSubscription.ifPresent(Subscription::unsubscribe);
			valueSubscription = Option.of(value.subscribe(it -> state.hasUnsavedData().set(true)));
		}
	}
	
	private void saveTo(Path path) throws IOException {
		state.getCurrentPath().set(Option.of(path));
		
		try (Writer writer = Files.newBufferedWriter(path, charset)) {
			saver.accept(writer);
			state.hasUnsavedData().set(false);
		}
	}
}
