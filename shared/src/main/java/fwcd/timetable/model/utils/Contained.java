package fwcd.timetable.model.utils;

import java.util.Collection;

public class Contained<E> {
	private final E value;
	private final Collection<? extends E> container;
	
	public Contained(E value, Collection<? extends E> container) {
		this.value = value;
		this.container = container;
	}
	
	public E getValue() { return value; }
	
	public Collection<? extends E> getContainer() { return container; }
}
