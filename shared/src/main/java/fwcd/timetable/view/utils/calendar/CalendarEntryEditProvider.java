package fwcd.timetable.view.utils.calendar;

import java.util.Collection;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.calendar.popover.AppointmentDetailsView;
import fwcd.timetable.view.sidebar.task.TaskDetailsView;
import fwcd.timetable.viewmodel.TimeTableAppContext;

public class CalendarEntryEditProvider implements CalendarEntryVisitor {
	private final TimeTableAppContext context;
	private final Collection<? extends CalendarEntryModel> parent;
	private Runnable onDelete = () -> {};
	private Option<FxView> view = Option.empty();
	
	public CalendarEntryEditProvider(TimeTableAppContext context, Collection<? extends CalendarEntryModel> parent) {
		this.context = context;
		this.parent = parent;
	}
	
	@Override
	public void visitAppointment(AppointmentModel appointment) {
		AppointmentDetailsView detailsView = new AppointmentDetailsView(parent, context, appointment);
		detailsView.setOnDelete(() -> onDelete.run());
		view = Option.of(detailsView);
	}
	
	@Override
	public void visitTask(TaskModel task) {
		TaskDetailsView detailsView = new TaskDetailsView(parent, context, task);
		detailsView.setOnDelete(() -> onDelete.run());
		view = Option.of(detailsView);
	}
	
	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
	
	public Option<FxView> getView() { return view; }
}
