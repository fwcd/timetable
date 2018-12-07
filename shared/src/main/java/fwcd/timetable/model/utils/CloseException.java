package fwcd.timetable.model.utils;

/** Wraps a (possibly checked) exception that ocurred while closing resources. */
public class CloseException extends RuntimeException {
	private static final long serialVersionUID = -7298665101531177424L;
	
	public CloseException(Throwable cause) { super(cause); }
}
