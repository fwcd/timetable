package com.fwcd.timetable.view.calendar.monthview;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.stream.Collectors;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.view.calendar.popover.AppointmentDetailsView;
import com.fwcd.timetable.view.calendar.utils.Calendarized;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.api.view.FxView;
import com.fwcd.timetable.view.utils.SubscriptionStack;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import org.controlsfx.control.PopOver;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonthDayView implements FxView, AutoCloseable {
	private final BorderPane node;
	private final Label indexLabel;
	private final VBox content;
	
	private final LocalDate date;
	private final CalendarsViewModel calendars;
	
	private final TimeTableAppContext context;
	private final SubscriptionStack subscriptions = new SubscriptionStack();
	
	public MonthDayView(TimeTableAppContext context, CalendarsViewModel calendars, LocalDate date) {
		this.calendars = calendars;
		this.date = date;
		this.context = context;
		
		node = new BorderPane();
		node.setPadding(new Insets(5));
		
		DayOfWeek weekDay = date.getDayOfWeek();
		if ((weekDay == DayOfWeek.SATURDAY) || (weekDay == DayOfWeek.SUNDAY)) {
			node.getStyleClass().add("month-day-weekend");
		}
		
		indexLabel = new Label(Integer.toString(date.getDayOfMonth()));
		indexLabel.setFont(Font.font(null, FontWeight.BOLD, 15));
		BorderPane.setAlignment(indexLabel, Pos.TOP_LEFT);
		node.setTop(indexLabel);
		
		content = new VBox();
		node.setCenter(content);
		
		subscriptions.push(calendars.getChangeListeners().subscribe(it -> updateView()));
	}
	
	private void updateView() {
		content.getChildren().setAll(calendars.getSelectedCalendars().stream()
			.flatMap(cal -> cal.getAppointments().stream()
				.filter(app -> app.occursOn(date))
				.map(app -> new Calendarized<>(app, cal)))
			.map(this::appointmentLabelOf)
			.collect(Collectors.toList())
		);
	}

	private Label appointmentLabelOf(Calendarized<AppointmentModel> appWithCal) {
		Label label = new Label(appWithCal.getEntry().getName().get());
		label.setOnMouseClicked(e -> {
			AppointmentDetailsView detailsView = new AppointmentDetailsView(appWithCal.getCalendar().getAppointments(), context, appWithCal.getEntry());
			PopOver popOver = FxUtils.newPopOver(detailsView);
			
			detailsView.setOnDelete(popOver::hide);
			FxUtils.showIndependentPopOver(
				popOver,
				label
			);
		});
		return label;
	}
	
	@Override
	public void close() {
		subscriptions.unsubscribeAll();
	}
	
	@Override
	public Node getNode() { return node; }
}
