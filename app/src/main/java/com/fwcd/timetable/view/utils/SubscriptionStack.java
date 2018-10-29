package com.fwcd.timetable.view.utils;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

import com.fwcd.fructose.Listenable;
import com.fwcd.fructose.function.Subscription;
import com.fwcd.fructose.structs.ArrayStack;
import com.fwcd.fructose.structs.Stack;

/**
 * An abstraction for managing listener subscriptions
 * with lists of listenables.
 */
public class SubscriptionStack {
	private final Stack<Subscription> subscriptions = new ArrayStack<>();
	
	public void unsubscribeAll() {
		while (!subscriptions.isEmpty()) {
			subscriptions.pop().unsubscribe();
		}
	}
	
	public void push(Subscription subscription) {
		subscriptions.push(subscription);
	}
	
	/** Moves all subscriptions in the provided collection to this queue. */
	public void takeAll(Iterable<Subscription> pushed) {
		Iterator<Subscription> iterator = pushed.iterator();
		while (iterator.hasNext()) {
			subscriptions.push(iterator.next());
			iterator.remove();
		}
	}
	
	/** Moves all subscriptions in the provided collection to this queue. */
	public void takeAll(SubscriptionStack other) {
		Iterator<Subscription> iterator = other.subscriptions.iterator();
		while (iterator.hasNext()) {
			subscriptions.push(iterator.next());
			iterator.remove();
		}
	}
	
	public <T> void subscribeAll(Iterable<? extends Listenable<T>> listenables, Consumer<? super T> listener) {
		for (Listenable<T> listenable : listenables) {
			subscriptions.push(listenable.subscribe(listener));
		}
	}
	
	public <T, O> void subscribeAll(Iterable<? extends T> inputs, Function<? super T, ? extends Listenable<O>> mapper, Consumer<? super O> listener) {
		for (T input : inputs) {
			subscriptions.push(mapper.apply(input).subscribe(listener));
		}
	}
}
