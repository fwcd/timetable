package com.fwcd.timetable.view.calendar.monthview;

import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonthNavigatorView implements FxView {
	private final HBox node;
	
	public MonthNavigatorView(TimeTableAppContext context, MonthContentView content) {
		Label monthLabel = new Label();
		monthLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
		content.getMonth().listenAndFire(it -> monthLabel.setText(context.getYearMonthFormatter().get().format(it)));
		
		HBox.setMargin(monthLabel, new Insets(0, /* right */ 4, 0, 0));
		
		node = new HBox(
			monthLabel,
			FxUtils.buttonOf("<", content::showPreviousMonth),
			FxUtils.buttonOf(context.localized("today"), content::showCurrentMonth),
			FxUtils.buttonOf(">", content::showNextMonth)
		);
		node.setAlignment(Pos.CENTER);
	}
	
	@Override
	public Node getNode() { return node; }
}
