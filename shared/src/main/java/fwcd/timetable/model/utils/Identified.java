package fwcd.timetable.model.utils;

import java.util.function.UnaryOperator;

/**
 * A pair of a value and an ID.
 * 
 * @param <E> - The element type
 */
public class Identified<E> {
	private final E value;
	private final int id;
	
	public Identified(E value, int id) {
		this.value = value;
		this.id = id;
	}
	
	public E getValue() { return value; }
	
	public int getId() { return id; }
	
	public Identified<E> map(UnaryOperator<E> mapper) { return new Identified<>(mapper.apply(value), id); }
	
	@Override
	public String toString() {
		return "Identified { value = " + value + ", id = " + id + " }";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!getClass().equals(obj.getClass())) return false;
		Identified<?> other = (Identified<?>) obj;
		return id == other.id;
	}
	
	@Override
	public int hashCode() {
		return id * 27;
	}
}
