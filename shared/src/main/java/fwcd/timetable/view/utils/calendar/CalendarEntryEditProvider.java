package fwcd.timetable.view.utils.calendar;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.calendar.popover.AppointmentDetailsView;
import fwcd.timetable.view.sidebar.task.TaskDetailsView;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

public class CalendarEntryEditProvider implements CalendarEntryVisitor<Option<FxView>> {
	private final Localizer localizer;
	private final TemporalFormatters formatters;
	private final CalendarCrateViewModel crate;
	private Runnable onDelete = () -> {};
	
	public CalendarEntryEditProvider(Localizer localizer, TemporalFormatters formatters, CalendarCrateViewModel crate) {
		this.localizer = localizer;
		this.formatters = formatters;
		this.crate = crate;
	}
	
	@Override
	public Option<FxView> visitCalendarEntry(CalendarEntryModel entry) {
		return Option.empty();
	}
	
	@Override
	public Option<FxView> visitAppointment(AppointmentModel appointment) {
		AppointmentDetailsView detailsView = new AppointmentDetailsView(localizer, formatters, crate, appointment);
		detailsView.setOnDelete(() -> onDelete.run());
		return Option.of(detailsView);
	}
	
	@Override
	public Option<FxView> visitTask(TaskModel task) {
		TaskDetailsView detailsView = new TaskDetailsView(localizer, formatters, crate, task);
		detailsView.setOnDelete(() -> onDelete.run());
		return Option.of(detailsView);
	}
	
	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
}
