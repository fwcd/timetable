package com.fwcd.timetable.model.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.fwcd.fructose.Observable;

public class CompletableProgressFuture<T> implements ProgressFuture<T> {
	private final CompletableFuture<? extends T> delegate;
	private final Observable<Double> progress;
	
	private CompletableProgressFuture(CompletableFuture<? extends T> delegate, Observable<Double> progress) {
		this.delegate = delegate;
		this.progress = progress;
	}
	
	public static <R> CompletableProgressFuture<R> completedFuture(R value) {
		CompletableProgressFuture<R> result = new CompletableProgressFuture<>(CompletableFuture.completedFuture(value), new Observable<>(1.0));
		return result;
	}
	
	public static CompletableProgressFuture<Void> runAsync(Consumer<? super Observable<Double>> task) {
		Observable<Double> progress = new Observable<>(0.0);
		return new CompletableProgressFuture<>(CompletableFuture.runAsync(() -> task.accept(progress)), progress);
	}
	
	public static CompletableProgressFuture<Void> runAsync(Consumer<? super Observable<Double>> task, Executor executor) {
		Observable<Double> progress = new Observable<>(0.0);
		return new CompletableProgressFuture<>(CompletableFuture.runAsync(() -> task.accept(progress), executor), progress);
	}
	
	public static <R> CompletableProgressFuture<R> supplyAsync(Function<? super Observable<Double>, ? extends R> task) {
		Observable<Double> progress = new Observable<>(0.0);
		return new CompletableProgressFuture<>(CompletableFuture.supplyAsync(() -> task.apply(progress)), progress);
	}

	public static <R> CompletableProgressFuture<R> supplyAsync(Function<? super Observable<Double>, ? extends R> task, Executor executor) {
		Observable<Double> progress = new Observable<>(0.0);
		return new CompletableProgressFuture<>(CompletableFuture.supplyAsync(() -> task.apply(progress), executor), progress);
	}
	
	public CompletableProgressFuture<Void> thenAccept(BiConsumer<? super T, ? super Observable<Double>> action) {
		return new CompletableProgressFuture<>(delegate.thenAccept(v -> action.accept(v, progress)), progress);
	}
	
	public <R> CompletableProgressFuture<R> thenApply(BiFunction<? super T, ? super Observable<Double>, ? extends R> action) {
		return new CompletableProgressFuture<>(delegate.thenApplyAsync(v -> action.apply(v, progress)), progress);
	}
	
	public <R> CompletableProgressFuture<R> handle(BiFunction<? super T, Throwable, ? extends R> handler) {
		return new CompletableProgressFuture<>(delegate.handle(handler), progress);
	}
	
	public CompletableProgressFuture<T> whenComplete(BiConsumer<? super T, Throwable> action) {
		return new CompletableProgressFuture<>(delegate.whenComplete(action), progress);
	}
	
	public CompletableProgressFuture<T> useProgress(Consumer<? super Observable<Double>> consumer) {
		consumer.accept(progress);
		return this;
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
