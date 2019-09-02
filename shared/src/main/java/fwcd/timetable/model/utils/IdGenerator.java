package fwcd.timetable.model.utils;

import java.io.Serializable;

/**
 * Generates a sequence of subsequent integer IDs.
 */
public class IdGenerator implements Serializable {
	private static final long serialVersionUID = 4796411062520985923L;
	/** An "empty" placeholder ID. Should never be generated through next(). */
	public static final int MISSING_ID = -1;
	private int current;
	
	public IdGenerator() {
		current = 0;
	}
	
	public IdGenerator(int start) {
		if (start < 0) {
			throw new IllegalArgumentException("ID generator needs to start with a non-negative ID");
		}
		current = start;
	}
	
	public int next() {
		int next = Math.addExact(current, 1);
		current = next;
		return next;
	}
}
