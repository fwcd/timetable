package fwcd.timetable.plugin.ical;

import java.util.Collections;
import java.util.List;

import fwcd.timetable.plugin.TimeTableAppPlugin;
import fwcd.timetable.view.NamedFxView;
import fwcd.timetable.view.ical.ICalView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppApi;

/**
 * A simple timer UI.
 */
public class ICalPlugin implements TimeTableAppPlugin {
	private static final String NAME = "iCal";
	private static final String DESCRIPTION = "iCalendar importer & exporter";
	private final ICalView view = new ICalView();
	private final List<NamedFxView> sidebarViews = Collections.singletonList(NamedFxView.ofUnlocalized("iCal", view));
	
	@Override
	public void initialize(TimeTableAppApi api) {
		FxUtils.runAndWait(() -> view.initialize(api));
	}
	
	@Override
	public String getName() { return NAME; }

	@Override
	public String getDescription() { return DESCRIPTION; }
	
	@Override
	public List<? extends NamedFxView> getSidebarViews() { return sidebarViews; }
}
