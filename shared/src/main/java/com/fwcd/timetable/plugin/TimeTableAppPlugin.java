package com.fwcd.timetable.plugin;

import java.util.Collections;
import java.util.List;

import com.fwcd.timetable.view.NamedFxView;
import com.fwcd.timetable.viewmodel.TimeTableAppApi;

public interface TimeTableAppPlugin {
	void initialize(TimeTableAppApi api);
	
	String getName();
	
	String getDescription();
	
	default List<? extends NamedFxView> getSidebarViews() { return Collections.emptyList(); }
}
