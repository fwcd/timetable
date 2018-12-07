package fwcd.timetable.model.utils;

import java.util.concurrent.Future;

import fwcd.fructose.Observable;

public interface ProgressFuture<T> extends Future<T> {
	Observable<Double> getProgress();
}
