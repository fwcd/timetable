package com.fwcd.timetable.plugin.timer;

import java.util.Collections;
import java.util.List;

import com.fwcd.timetable.plugin.TimeTableAppPlugin;
import com.fwcd.timetable.view.NamedFxView;
import com.fwcd.timetable.view.timer.TimerView;
import com.fwcd.timetable.viewmodel.TimeTableAppApi;

/**
 * A simple timer UI.
 */
public class TimerPlugin implements TimeTableAppPlugin {
	private static final String NAME = "Timer";
	private static final String DESCRIPTION = "A simple timer UI";
	private final TimerView view = new TimerView();
	private final List<NamedFxView> sidebarViews = Collections.singletonList(NamedFxView.of("Timer", view));
	
	@Override
	public void initialize(TimeTableAppApi api) {
		
	}
	
	@Override
	public String getName() { return NAME; }

	@Override
	public String getDescription() { return DESCRIPTION; }
	
	@Override
	public List<? extends NamedFxView> getSidebarViews() { return sidebarViews; }
}
