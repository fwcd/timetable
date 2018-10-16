package com.fwcd.timetable.view.calendar.weekview;

import java.time.LocalTime;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.view.utils.FxNavigableView;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

public class WeekView implements FxNavigableView {
	private final Node navigatorNode;
	private final BorderPane contentNode;
	
	private final WeekHeaderView header;
	private final WeekContentView content;
	private final ScrollPane scrollPane;
	private final WeekDayTimeLayouter timeLayouter = new WeekDayTimeLayouter();
	
	public WeekView(TimeTableAppContext context, CalendarsViewModel calendars) {
		content = new WeekContentView(timeLayouter, context, calendars);
		scrollPane = new ScrollPane(content.getNode());
		header = new WeekHeaderView(context, content);
		navigatorNode = new WeekNavigatorView(context, content).getNode();
		
		FxUtils.setVerticalScrollSpeed(scrollPane, 2);
		scrollPane.setFitToWidth(true);
		
		// Automatically scroll to the current time indicator
		double vmax = scrollPane.getVmax();
		double vmin = scrollPane.getVmin();
		double normalizedValue = timeLayouter.toPixelY(LocalTime.now()) / timeLayouter.toPixelY(LocalTime.MAX);
		scrollPane.setVvalue((normalizedValue * (vmax - vmin)) + vmin);
		
		contentNode = new BorderPane();
		contentNode.setTop(header.getNode());
		contentNode.setCenter(scrollPane);
	}
	
	@Override
	public Node getContentNode() { return contentNode; }
	
	@Override
	public Option<Node> getNavigatorNode() { return Option.of(navigatorNode); }
}
