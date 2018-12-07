package fwcd.timetable.model.utils;

import java.util.function.BiConsumer;

import fwcd.fructose.ReadOnlyObservable;

public final class ObservableUtils {
	public static final ReadOnlyObservable<String> EMPTY_STRING = new ReadOnlyObservable<>("");
	
	private ObservableUtils() {}
	
	/** Listens to two observables "at once" */
	public static <A, B> void dualListen(ReadOnlyObservable<? extends A> a, ReadOnlyObservable<? extends B> b, BiConsumer<? super A, ? super B> listener) {
		a.listen(aValue -> listener.accept(aValue, b.get()));
		b.listen(bValue -> listener.accept(a.get(), bValue));
	}
	
	public static <A, B> void dualListenAndFire(ReadOnlyObservable<? extends A> a, ReadOnlyObservable<? extends B> b, BiConsumer<? super A, ? super B> listener) {
		dualListen(a, b, listener);
		listener.accept(a.get(), b.get());
	}
	
	/** Listens to three observables "at once" */
	public static <A, B, C> void triListen(ReadOnlyObservable<? extends A> a, ReadOnlyObservable<? extends B> b, ReadOnlyObservable<? extends C> c, TriConsumer<? super A, ? super B, ? super C> listener) {
		a.listen(aValue -> listener.accept(aValue, b.get(), c.get()));
		b.listen(bValue -> listener.accept(a.get(), bValue, c.get()));
		c.listen(cValue -> listener.accept(a.get(), b.get(), cValue));
	}
	
	public static <A, B, C> void triListenAndFire(ReadOnlyObservable<? extends A> a, ReadOnlyObservable<? extends B> b, ReadOnlyObservable<? extends C> c, TriConsumer<? super A, ? super B, ? super C> listener) {
		triListen(a, b, c, listener);
		listener.accept(a.get(), b.get(), c.get());
	}
}
