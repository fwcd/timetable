package com.fwcd.timetable.view.calendar.weekview;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.structs.ArrayBiList;
import com.fwcd.fructose.structs.BiList;
import com.fwcd.fructose.time.LocalTimeInterval;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.model.calendar.task.TaskModel;
import com.fwcd.timetable.model.utils.CloseUtils;
import com.fwcd.timetable.view.calendar.utils.Calendarized;
import com.fwcd.timetable.api.view.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WeekDayEntriesView implements FxView {
	private final WeekDayTimeLayouter layouter;
	private final StackPane node;
	
	private final TimeTableAppContext context;
	private final CalendarsViewModel calendars;
	
	private final List<AutoCloseable> childResources = new ArrayList<>();
	private final BiList<LocalTimeInterval, StackPane> overlapBoxes = new ArrayBiList<>();
	private Option<LocalDate> currentDate = Option.empty();
	
	public WeekDayEntriesView(WeekDayTimeLayouter layouter, TimeTableAppContext context, CalendarsViewModel calendars) {
		this.layouter = layouter;
		this.context = context;
		this.calendars = calendars;
		
		node = new StackPane();
		node.setMinSize(0, 0);
		node.setPickOnBounds(false);
		
		calendars.getStructuralChangeListeners().add(it -> updateView());
	}
	
	public void setDate(LocalDate date) {
		currentDate = Option.of(date);
		updateView();
	}

	private void updateView() {
		clear();
		currentDate.ifPresent(date -> {
			// Add appointments
			calendars.getSelectedCalendars().stream()
				.flatMap(cal -> cal.getAppointments().stream()
					.filter(app -> app.occursOn(date))
					.map(app -> new Calendarized<>(app, cal)))
				.forEach(it -> addAppointment(it, date));
			
			// Add tasks
			calendars.getSelectedCalendars().stream()
				.flatMap(cal -> cal.getTaskCrate().getLists().stream()
					.flatMap(task -> task.getTasks().stream())
					.filter(task -> task.occursOn(date))
					.map(it -> new Calendarized<>(it, cal)))
				.forEach(it -> addTask(it, date));
		});
	}

	private void addAppointment(Calendarized<AppointmentModel> appWithCal, LocalDate viewedDate) {
		AppointmentModel appointmentModel = appWithCal.getEntry();
		CalendarModel calendar = appWithCal.getCalendar();
		AppointmentView appointmentView = new AppointmentView(layouter, context, calendar, appointmentModel);
		Pane child = appointmentView.getNode();
		
		childResources.add(appointmentView);
		
		LocalTimeInterval eventInterval = appointmentModel.getTimeIntervalOn(viewedDate);
		AnchorPane.setTopAnchor(child, layouter.toPixelY(eventInterval.getStart()));
		child.maxWidthProperty().bind(node.widthProperty());
		child.setPrefHeight(layouter.toPixelHeight(eventInterval.getDuration()));
		
		StackPane overlappingBox = null;
		int overlapBoxCount = overlapBoxes.size();
		
		for (int i = 0; i < overlapBoxCount; i++) {
			LocalTimeInterval interval = overlapBoxes.getA(i);
			StackPane box = overlapBoxes.getB(i);
			
			if (interval.overlaps(eventInterval)) {
				overlapBoxes.setA(i, interval.merge(eventInterval));
				overlappingBox = box;
				break;
			}
		}
		
		if (overlappingBox == null) {
			overlappingBox = new StackPane();
			overlappingBox.setPickOnBounds(false);
			
			overlapBoxes.add(eventInterval, overlappingBox);
			node.getChildren().add(overlappingBox);
		}
		
		int overlaps = overlappingBox.getChildren().size();
		double indent = overlaps * 10;
		AnchorPane anchor = new AnchorPane(child);
		anchor.setPickOnBounds(false);
		AnchorPane.setLeftAnchor(child, indent);
		overlappingBox.getChildren().add(anchor);
	}
	
	private void addTask(Calendarized<TaskModel> taskWithCal, LocalDate viewedDate) {
		TaskModel taskModel = taskWithCal.getEntry();
		TaskMarkView taskView = new TaskMarkView(taskModel);
		Pane child = taskView.getNode();
		
		childResources.add(taskView);
		
		AnchorPane.setTopAnchor(child, layouter.toPixelY(taskModel.getDateTime().get()
			.unwrap("Tried to add a task without a datetime to a WeekDayEntriesView")
			.toLocalTime()));
		AnchorPane.setLeftAnchor(child, 0D);
		AnchorPane.setRightAnchor(child, 0D);
		
		AnchorPane anchor = new AnchorPane(child);
		anchor.setPickOnBounds(false);
		
		node.getChildren().add(anchor);
	}
	
	private void clear() {
		node.getChildren().clear();
		overlapBoxes.clear();
		CloseUtils.clean(childResources);
	}
	
	@Override
	public Node getNode() { return node; }
}