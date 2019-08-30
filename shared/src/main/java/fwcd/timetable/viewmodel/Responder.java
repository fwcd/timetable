package fwcd.timetable.viewmodel;

import fwcd.fructose.Option;

/**
 * A responder implements a handler in a chain
 * of responsibility, most commonly used for
 * event bubbling in the viewmodel layer (hence
 * this class being located in the viewmodel
 * package).
 */
public interface Responder {
	/**
	 * Fetches the next responder in the chain.
	 */
	Option<Responder> getNextResponder();
}
