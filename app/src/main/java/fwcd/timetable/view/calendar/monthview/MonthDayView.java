package fwcd.timetable.view.calendar.monthview;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.controlsfx.control.PopOver;

import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor.AppointmentsOnly;
import fwcd.timetable.model.utils.SubscriptionStack;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.calendar.popover.AppointmentDetailsView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
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
	private final CalendarCrateViewModel crate;
	
	private final TimeTableAppContext context;
	private final SubscriptionStack subscriptions = new SubscriptionStack();
	
	public MonthDayView(TimeTableAppContext context, CalendarCrateViewModel calendars, LocalDate date) {
		this.crate = calendars;
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
		
		crate.getEntryListeners().add(it -> updateView());
	}
	
	private void updateView() {
		Set<Integer> selected = crate.getSelectedCalendarIds();
		content.getChildren().setAll(crate.streamEntries()
			.filter(it -> selected.contains(it.getCalendarId()))
			.flatMap(it -> it.accept(new AppointmentsOnly()).stream())
			.filter(app -> app.occursOn(date))
			.map(this::appointmentLabelOf)
			.collect(Collectors.toList())
		);
	}

	private Label appointmentLabelOf(AppointmentModel app) {
		Label label = new Label(app.getName());
		label.setOnMouseClicked(e -> {
			AppointmentDetailsView detailsView = new AppointmentDetailsView(context.getLocalizer(), context.getFormatters(), crate, app);
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
