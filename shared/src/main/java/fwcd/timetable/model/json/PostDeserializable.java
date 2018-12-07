package fwcd.timetable.model.json;

/**
 * Performs additional initialization after all
 * fields have been deserialized.
 */
public interface PostDeserializable {
	/**
	 * Re-initializes the object after all fields have
	 * been deserialized (this usually excludes transient
	 * fields).
	 * 
	 * <p>This might be necessary, because Gson replaces
	 * final fields after the constructor invocation.</p>
	 */
	void postDeserialize();
}
