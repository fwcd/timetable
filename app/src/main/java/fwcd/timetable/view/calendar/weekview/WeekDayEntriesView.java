package fwcd.timetable.view.calendar.weekview;

import java.time.LocalDate;
import java.util.Collection;

import fwcd.fructose.Option;
import fwcd.fructose.structs.ArrayBiList;
import fwcd.fructose.structs.BiList;
import fwcd.fructose.time.LocalTimeInterval;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WeekDayEntriesView implements FxView {
	private final WeekDayTimeLayouter layouter;
	private final StackPane node;
	
	private final TimeTableAppContext context;
	private final CalendarCrateViewModel crate;
	
	private final BiList<LocalTimeInterval, StackPane> overlapBoxes = new ArrayBiList<>();
	private Option<LocalDate> currentDate = Option.empty();
	
	public WeekDayEntriesView(WeekDayTimeLayouter layouter, TimeTableAppContext context, CalendarCrateViewModel crate) {
		this.layouter = layouter;
		this.context = context;
		this.crate = crate;
		
		node = new StackPane();
		node.setMinSize(0, 0);
		node.setPickOnBounds(false);
		
		crate.getVisibleEntryListeners().add(it -> updateView());
		updateView();
	}
	
	public void setDate(LocalDate date) {
		currentDate = Option.of(date);
		updateView();
	}

	private void updateView() {
		clear();
		currentDate.ifPresent(date -> {
			Collection<Integer> selectedCalendars = crate.getSelectedCalendarIds();
			crate.streamEntries()
				.filter(it -> selectedCalendars.contains(it.getCalendarId()) && it.occursOn(date))
				.forEach(it -> it.accept(new CalendarEntryVisitor.ReturningNullByDefault<Void>() {
					@Override
					public Void visitAppointment(AppointmentModel appointment) {
						addAppointment(appointment, date);
						return null;
					}
					
					@Override
					public Void visitTask(TaskModel task) {
						addTask(task, date);
						return null;
					}
				}));
		});
	}

	private void addAppointment(AppointmentModel appointmentModel, LocalDate viewedDate) {
		AppointmentView appointmentView = new AppointmentView(layouter, context, crate, appointmentModel);
		Pane child = appointmentView.getNode();
		
		LocalTimeInterval eventInterval = appointmentModel.getTimeIntervalOn(viewedDate);
		AnchorPane.setTopAnchor(child, layouter.toPixelY(eventInterval.getStart()));
		child.maxWidthProperty().bind(node.widthProperty());
		child.setPrefHeight(layouter.toPixelHeight(eventInterval.getDuration()));
		
		StackPane overlappingBox = null;
		int overlapBoxCount = overlapBoxes.size();
		
		for (int i = 0; i < overlapBoxCount; i++) {
			LocalTimeInterval interval = overlapBoxes.getLeft(i);
			StackPane box = overlapBoxes.getRight(i);
			
			if (interval.overlaps(eventInterval)) {
				overlapBoxes.setLeft(i, interval.merge(eventInterval));
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
	
	private void addTask(TaskModel taskModel, LocalDate viewedDate) {
		TaskMarkView taskView = new TaskMarkView(taskModel);
		Pane child = taskView.getNode();
		
		AnchorPane.setTopAnchor(child, layouter.toPixelY(taskModel.getDateTime()
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
	}
	
	@Override
	public Node getNode() { return node; }
}
