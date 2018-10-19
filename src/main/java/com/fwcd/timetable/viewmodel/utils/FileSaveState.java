package com.fwcd.timetable.viewmodel.utils;

import java.nio.file.Path;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.ReadOnlyObservable;

public class FileSaveState {
	private final Observable<Boolean> hasUnsavedData = new Observable<>(false);
	private final Observable<Option<Path>> currentPath = new Observable<>(Option.empty());
	private final ReadOnlyObservable<Option<String>> currentTitle = currentPath.mapStrongly(it -> it.map(v -> v.getFileName().toString()));
	
	public Observable<Boolean> hasUnsavedData() { return hasUnsavedData; }
	
	public Observable<Option<Path>> getCurrentPath() { return currentPath; }
	
	public ReadOnlyObservable<Option<String>> getCurrentFileName() { return currentTitle; }
}
