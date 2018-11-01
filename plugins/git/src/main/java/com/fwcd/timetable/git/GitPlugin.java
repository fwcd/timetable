package com.fwcd.timetable.git;

import com.fwcd.timetable.api.plugin.TimeTableAppPlugin;

public class GitPlugin implements TimeTableAppPlugin {
	@Override
	public String getName() { return "Git"; }

	@Override
	public String getDescription() { return "Git integration for TimeTable"; }
}
