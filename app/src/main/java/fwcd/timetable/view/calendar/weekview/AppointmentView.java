package fwcd.timetable.view.calendar.weekview;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.function.Consumer;

import org.controlsfx.control.PopOver;

import fwcd.fructose.draw.DrawColor;
import fwcd.fructose.time.LocalDateTimeInterval;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.model.utils.Identified;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.calendar.popover.AppointmentDetailsView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AppointmentView implements FxView {
	private final Pane node;
	private final Label nameLabel;
	private final Label locationLabel;
	private final Label timeLabel;
	
	/** Strongly refers to a calendar listener that is dropped together with this view. */
	private Consumer<Collection<Identified<CalendarModel>>> calendarListener;
	
	public AppointmentView(WeekDayTimeLayouter layouter, TimeTableAppContext context, CalendarCrateViewModel crate, AppointmentModel model) {
		Color fgColor = Color.BLACK;
		
		node = new VBox();
		node.setMinWidth(0);
		node.getStyleClass().add("appointment");
		
		nameLabel = new Label();
		nameLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		nameLabel.setTextFill(fgColor);
		nameLabel.setWrapText(true);
		nameLabel.setText(model.getName());
		node.getChildren().add(nameLabel);
		
		locationLabel = new Label();
		locationLabel.setFont(Font.font(11));
		locationLabel.setTextFill(fgColor);
		model.getLocation().map(Location::getLabel).ifPresent(locationLabel::setText);
		locationLabel.setVisible(model.getLocation().isPresent());
		locationLabel.managedProperty().bind(locationLabel.visibleProperty()); // Do not occupy space if the label is not visible
		node.getChildren().add(locationLabel);
		
		timeLabel = new Label();
		timeLabel.setFont(Font.font(11));
		timeLabel.setTextFill(fgColor);
		timeLabel.setText(formatTimeInterval(model.getDateTimeInterval(), context.getDateTimeFormatter().get()));
		node.getChildren().add(timeLabel);
		
		AppointmentDetailsView detailsView = new AppointmentDetailsView(context.getLocalizer(), context.getFormatters(), crate, model);
		PopOver popOver = FxUtils.newPopOver(detailsView);
		detailsView.setOnDelete(popOver::hide);
		node.setOnMouseClicked(e -> {
			FxUtils.showIndependentPopOver(popOver, node);
			e.consume();
		});
		
		calendarListener = it -> {
			CalendarModel calendar = crate.getCalendarById(model.getCalendarId());
			if (calendar == null) {
				// Remove listener once calendar has been deleted
				crate.getCalendarListeners().removeWeakListener(calendarListener);
			} else {
				updateBackground(calendar.getColor());
			}
		};
		crate.getCalendarListeners().addWeakListener(calendarListener);
		calendarListener.accept(crate.getCalendars());
	}
	
	private void updateBackground(DrawColor color) {
		node.setBackground(new Background(new BackgroundFill(brightColor(FxUtils.toFxColor(color)), new CornerRadii(3), Insets.EMPTY)));
	}
	
	private String formatTimeInterval(LocalDateTimeInterval interval, DateTimeFormatter formatter) {
		return formatter.format(interval.getStart()) + " - " + formatter.format(interval.getEnd());
	}

	private Color brightColor(Color calColor) {
		double amount = 0.5;
		double r = calColor.getRed() + amount;
		double g = calColor.getGreen() + amount;
		double b = calColor.getBlue() + amount;
		double f = max(1.0, r, g, b); // Only scale if r, g or b is > 1.0
		return new Color(r / f, g / f, b / f, calColor.getOpacity());
	}
	
	private double max(double a, double b, double c, double d) {
		return Math.max(Math.max(a, b), Math.max(c, d));
	}

	@Override
	public Pane getNode() { return node; }
}
