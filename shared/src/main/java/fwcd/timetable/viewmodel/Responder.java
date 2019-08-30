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
	 * Registers the provided responder as the
	 * next responder. Note that the implementor
	 * may still decide to "swallow" an occurring
	 * event.
	 * 
	 * @param responder - The responder to be registered
	 */
	void setNextResponder(Option<Responder> responder);
	
	/**
	 * Notifies the responder that some event
	 * occurred. May or may not follow in a
	 * call to the next responder in the chain.
	 */
	void fire();
}
