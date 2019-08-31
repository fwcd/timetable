package fwcd.timetable.view.calendar.weekview;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fwcd.fructose.Option;
import fwcd.fructose.structs.ArrayBiList;
import fwcd.fructose.structs.BiList;
import fwcd.fructose.time.LocalTimeInterval;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.model.utils.CloseUtils;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.calendar.utils.Calendarized;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.AppointmentViewModel;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import fwcd.timetable.viewmodel.calendar.CalendarViewModel;
import fwcd.timetable.viewmodel.calendar.task.TaskViewModel;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WeekDayEntriesView implements FxView {
	private final WeekDayTimeLayouter layouter;
	private final StackPane node;
	
	private final TimeTableAppContext context;
	private final CalendarCrateViewModel calendars;
	
	private final List<AutoCloseable> childResources = new ArrayList<>();
	private final BiList<LocalTimeInterval, StackPane> overlapBoxes = new ArrayBiList<>();
	private Option<LocalDate> currentDate = Option.empty();
	
	public WeekDayEntriesView(WeekDayTimeLayouter layouter, TimeTableAppContext context, CalendarCrateViewModel calendars) {
		this.layouter = layouter;
		this.context = context;
		this.calendars = calendars;
		
		node = new StackPane();
		node.setMinSize(0, 0);
		node.setPickOnBounds(false);
		
		calendars.getChangeListeners().add(it -> updateView());
	}
	
	public void setDate(LocalDate date) {
		currentDate = Option.of(date);
		updateView();
	}

	private void updateView() {
		clear();
		currentDate.ifPresent(date -> {
			Collection<CalendarViewModel> selectedCalendars = calendars.getSelectedCalendars();

			// Add appointments
			selectedCalendars.stream()
				.flatMap(cal -> cal.getModel().getAppointments().stream()
					.filter(app -> app.occursOn(date))
					.map(app -> new Calendarized<>(new AppointmentViewModel(app), cal)))
				.forEach(it -> addAppointment(it, date));
			
			// Add tasks
			selectedCalendars.stream()
				.flatMap(cal -> cal.getModel().getTaskCrate().getLists().stream()
					.flatMap(task -> task.getTasks().stream())
					.filter(task -> task.occursOn(date))
					.map(it -> new Calendarized<>(new TaskViewModel(it), cal)))
				.forEach(it -> addTask(it, date));
		});
	}

	private void addAppointment(Calendarized<AppointmentViewModel> appWithCal, LocalDate viewedDate) {
		AppointmentViewModel appointmentVM = appWithCal.getEntry();
		CalendarViewModel calendar = appWithCal.getCalendar();
		AppointmentView appointmentView = new AppointmentView(layouter, context, calendar, appointmentVM);
		Pane child = appointmentView.getNode();
		
		childResources.add(appointmentView);
		
		LocalTimeInterval eventInterval = appointmentVM.getModel().getTimeIntervalOn(viewedDate);
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
	
	private void addTask(Calendarized<TaskViewModel> taskWithCal, LocalDate viewedDate) {
		TaskViewModel taskVM = taskWithCal.getEntry();
		TaskMarkView taskView = new TaskMarkView(taskVM);
		Pane child = taskView.getNode();
		
		childResources.add(taskView);
		
		AnchorPane.setTopAnchor(child, layouter.toPixelY(taskVM.getModel().getDateTime()
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
