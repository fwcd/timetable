package com.fwcd.timetable.model.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import com.fwcd.fructose.Observable;

public class CompletableProgressFuture<T> implements ProgressFuture<T> {
	private final CompletableFuture<T> delegate;
	private final Observable<Double> progress = new Observable<>(0D);
	
	private CompletableProgressFuture(CompletableFuture<T> delegate) {
		this.delegate = delegate;
	}
	
	public static <R> CompletableProgressFuture<R> completedFuture(R value) {
		return new CompletableProgressFuture<>(CompletableFuture.completedFuture(value));
	}
	
	public static CompletableProgressFuture<Void> runAsync(Runnable runnable) {
		return new CompletableProgressFuture<>(CompletableFuture.runAsync(runnable));
	}
	
	public static CompletableProgressFuture<Void> runAsync(Runnable runnable, Executor executor) {
		return new CompletableProgressFuture<>(CompletableFuture.runAsync(runnable, executor));
	}
	
	public static <R> CompletableProgressFuture<R> supplyAsync(Supplier<R> supplier) {
		return new CompletableProgressFuture<>(CompletableFuture.supplyAsync(supplier));
	}

	public static <R> CompletableProgressFuture<R> supplyAsync(Supplier<R> supplier, Executor executor) {
		return new CompletableProgressFuture<>(CompletableFuture.supplyAsync(supplier, executor));
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return delegate.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return delegate.isCancelled();
	}

	@Override
	public boolean isDone() {
		return delegate.isDone();
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		return delegate.get();
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return delegate.get(timeout, unit);
	}

	@Override
	public Observable<Double> getProgress() {
		return progress;
	}
}
