package com.fwcd.timetable.view.calendar.details;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AppointmentDetailsView implements FxView {
	private final VBox node;
	
	public AppointmentDetailsView(AppointmentModel model) {
		TextField title = new TextField();
		FxUtils.bindBidirectionally(model.getName(), title.textProperty());
		title.setFont(Font.font(14));
		
		node = new VBox(
			title
		);
		node.setPadding(new Insets(10, 10, 10, 10));
	}
	
	@Override
	public Node getNode() { return node; }
}
