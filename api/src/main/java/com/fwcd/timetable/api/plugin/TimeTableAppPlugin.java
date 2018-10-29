package com.fwcd.timetable.api.plugin;

import java.util.Collections;
import java.util.List;

import com.fwcd.timetable.api.view.NamedFxView;

public interface TimeTableAppPlugin {
	String getName();
	
	String getDescription();
	
	default List<? extends NamedFxView> getSidebarViews() { return Collections.emptyList(); }
}
