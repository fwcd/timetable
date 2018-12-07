package fwcd.timetable.view.calendar.monthview;

import fwcd.fructose.Option;
import fwcd.timetable.view.utils.FxNavigableView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;

public class MonthView implements FxNavigableView {
	private final Node contentNode;
	private final Node navigatorNode;
	
	public MonthView(TimeTableAppContext context, CalendarsViewModel calendars) {
		MonthContentView content = new MonthContentView(context, calendars);
		contentNode = content.getNode();
		navigatorNode = new MonthNavigatorView(context, content).getNode();
	}
	
	@Override
	public Node getContentNode() { return contentNode; }
	
	@Override
	public Option<Node> getNavigatorNode() { return Option.of(navigatorNode); }
}
