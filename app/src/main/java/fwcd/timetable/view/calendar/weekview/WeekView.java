package fwcd.timetable.view.calendar.weekview;

import java.time.LocalTime;

import fwcd.fructose.Option;
import fwcd.timetable.view.utils.FxNavigableView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

public class WeekView implements FxNavigableView {
	private final Node navigatorNode;
	private final BorderPane contentNode;
	
	private final WeekContentView content;
	private final ScrollPane scrollPane;
	private final WeekDayTimeLayouter timeLayouter = new WeekDayTimeLayouter();
	
	public WeekView(TimeTableAppContext context, CalendarCrateViewModel calendars) {
		content = new WeekContentView(timeLayouter, context, calendars);
		scrollPane = new ScrollPane(content.getNode());
		navigatorNode = new WeekNavigatorView(context, content).getNode();
		
		FxUtils.setVerticalScrollSpeed(scrollPane, 2);
		scrollPane.setFitToWidth(true);
		
		// Automatically scroll to the current time indicator
		double vmax = scrollPane.getVmax();
		double vmin = scrollPane.getVmin();
		double normalizedValue = timeLayouter.toPixelY(LocalTime.now()) / timeLayouter.toPixelY(LocalTime.MAX);
		scrollPane.setVvalue((normalizedValue * (vmax - vmin)) + vmin);
		
		contentNode = new BorderPane();
		contentNode.setTop(content.getHeaderNode());
		contentNode.setCenter(scrollPane);
	}
	
	@Override
	public Node getContentNode() { return contentNode; }
	
	@Override
	public Option<Node> getNavigatorNode() { return Option.of(navigatorNode); }
}
