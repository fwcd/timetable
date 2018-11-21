package com.fwcd.timetable.plugin;

import java.util.Collections;
import java.util.List;

import com.fwcd.timetable.view.NamedFxView;

public interface TimeTableAppPlugin {
	String getName();
	
	String getDescription();
	
	default List<? extends NamedFxView> getSidebarViews() { return Collections.emptyList(); }
}
