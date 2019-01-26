package fwcd.timetable.plugin;

import java.util.Collections;
import java.util.List;

import fwcd.timetable.view.NamedFxView;
import fwcd.timetable.view.sidebar.cau.CauView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppApi;

/**
 * A plugin that provides access to the lecture
 * database of the University of Kiel.
 */
public class CauPlugin implements TimeTableAppPlugin {
	private static final String NAME = "CAU";
	private static final String DESCRIPTION = "CAU lecture database integration";
	private final CauView view = new CauView();
	private final List<NamedFxView> sidebarViews = Collections.singletonList(NamedFxView.ofUnlocalized("CAU", view));
	
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
	
	@Override
	public void close() {
		
	}
}
