package fwcd.timetable.plugin;

import java.util.Collections;
import java.util.List;

import fwcd.timetable.view.NamedFxView;
import fwcd.timetable.viewmodel.TimeTableAppApi;

/**
 * An extension of the application that can interact
 * with it through {@link TimeTableAppApi}.
 */
public interface TimeTableAppPlugin extends AutoCloseable {
	void initialize(TimeTableAppApi api);
	
	String getName();
	
	String getDescription();
	
	default List<? extends NamedFxView> getSidebarViews() { return Collections.emptyList(); }
	
	@Override
	default void close() {}
}
