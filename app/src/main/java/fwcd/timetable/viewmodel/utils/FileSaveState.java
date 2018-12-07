package fwcd.timetable.viewmodel.utils;

import java.nio.file.Path;

import fwcd.fructose.Observable;
import fwcd.fructose.Option;
import fwcd.fructose.ReadOnlyObservable;

/**
 * Holds a path to the currently loaded file together
 * with its name and save state.
 */
public class FileSaveState {
	private final Observable<Boolean> hasUnsavedData = new Observable<>(false);
	private final Observable<Option<Path>> currentPath = new Observable<>(Option.empty());
	private final ReadOnlyObservable<Option<String>> currentTitle = currentPath.mapStrongly(it -> it.map(v -> v.getFileName().toString()));
	
	public Observable<Boolean> hasUnsavedData() { return hasUnsavedData; }
	
	public Observable<Option<Path>> getCurrentPath() { return currentPath; }
	
	public ReadOnlyObservable<Option<String>> getCurrentFileName() { return currentTitle; }
}
