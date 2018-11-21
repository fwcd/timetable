package com.fwcd.timetable.plugin.git;

import java.util.Collections;
import java.util.List;

import com.fwcd.timetable.plugin.TimeTableAppPlugin;
import com.fwcd.timetable.view.NamedFxView;
import com.fwcd.timetable.view.git.GitView;

public class GitPlugin implements TimeTableAppPlugin {
	private static final String NAME = "Git";
	private static final String DESCRIPTION = "Git integration for TimeTable";
	private final List<NamedFxView> sidebarViews = Collections.singletonList(NamedFxView.of("Git", new GitView()));
	
	@Override
	public String getName() { return NAME; }

	@Override
	public String getDescription() { return DESCRIPTION; }
	
	@Override
	public List<? extends NamedFxView> getSidebarViews() { return sidebarViews; }
}
