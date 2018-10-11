package com.fwcd.timetable.view.calendar.details;

import com.fwcd.fructose.time.LocalDateTimeInterval;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import tornadofx.control.DateTimePicker;

public class AppointmentDetailsView implements FxView {
	private final VBox node;
	
	public AppointmentDetailsView(AppointmentModel model) {
		TextField title = new TextField();
		FxUtils.bindBidirectionally(model.getName(), title.textProperty());
		title.setFont(Font.font(14));
		
		GridPane properties = new GridPane();
		
		DateTimePicker start = new DateTimePicker();
		FxUtils.bindBidirectionally(
			model.getDateTimeInterval(),
			start.dateTimeValueProperty(),
			interval -> interval.getStart(),
			dateTime -> new LocalDateTimeInterval(dateTime, model.getEnd())
		);
		properties.addRow(1, new Label("Start: "), start);
		
		DateTimePicker end = new DateTimePicker();
		FxUtils.bindBidirectionally(
			model.getDateTimeInterval(),
			end.dateTimeValueProperty(),
			interval -> interval.getEnd(),
			dateTime -> new LocalDateTimeInterval(model.getStart(), dateTime)
		);
		properties.addRow(2, new Label("End: "), end);
		
		node = new VBox(
			title,
			properties
		);
		node.setPadding(new Insets(10, 10, 10, 10));
	}
	
	@Override
	public Node getNode() { return node; }
}
