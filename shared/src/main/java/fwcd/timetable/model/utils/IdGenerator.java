package fwcd.timetable.model.utils;

import java.io.Serializable;

/**
 * Generates a sequence of subsequent integer IDs.
 */
public class IdGenerator implements Serializable {
	private static final long serialVersionUID = 4796411062520985923L;
	private int current;
	
	public IdGenerator() {
		current = 0;
	}
	
	public IdGenerator(int start) {
		current = start;
	}
	
	public int next() {
		return current++;
	}
}
