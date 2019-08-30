package fwcd.timetable.viewmodel;

/**
 * A more readable wrapper around a boolean
 * for "silencable" setters.
 */
public enum Silent {
	YES, NO;
	
	public boolean is() { return this == YES; }
	
	public boolean isNot() { return this == NO; }
}
