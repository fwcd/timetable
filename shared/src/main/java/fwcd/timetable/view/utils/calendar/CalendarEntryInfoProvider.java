package fwcd.timetable.view.utils.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fwcd.fructose.Observable;
import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.model.utils.SubscriptionStack;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;

public class CalendarEntryInfoProvider implements CalendarEntryVisitor {
	private final Localizer localizer;
	private final TemporalFormatters formatters;
	private final Observable<String> info = new Observable<>("");
	private final SubscriptionStack subscriptions = new SubscriptionStack();
	
	public CalendarEntryInfoProvider(Localizer localizer, TemporalFormatters formatters) {
		this.localizer = localizer;
		this.formatters = formatters;
	}
	
	@Override
	public void visitAppointment(AppointmentModel appointment) {
		Runnable updater = () -> info.set(getAppointmentInfo(appointment));
		
		subscriptions.push(appointment.getDateTimeInterval().subscribe(v -> updater.run()));
		subscriptions.push(appointment.getLocation().subscribe(v -> updater.run()));
		subscriptions.push(appointment.ignoresDate().subscribe(v -> updater.run()));
		subscriptions.push(appointment.ignoresTime().subscribe(v -> updater.run()));
		subscriptions.push(appointment.getRecurrence().getParsed().subscribe(v -> updater.run()));
		
		updater.run();
	}
	
	private String getAppointmentInfo(AppointmentModel appointment) {
		StringBuilder str = new StringBuilder();
		boolean ignoreDate = appointment.ignoresDate().get();
		boolean ignoreTime = appointment.ignoresTime().get();
		
		DateTimeFormatter dateFormatter = formatters.getDateFormatter();
		DateTimeFormatter timeFormatter = formatters.getTimeFormatter();
		DateTimeFormatter dateTimeFormatter = formatters.getDateTimeFormatter();
		
		appointment.getRecurrence().getParsed().get()
			.map(it -> it.describeWith(localizer.getLanguage(), dateFormatter))
			.ifPresent(recurrence -> str.append(recurrence).append(" - "));
		
		if (!ignoreDate && !ignoreTime) {
			LocalDateTime start = appointment.getStart();
			LocalDateTime end = appointment.getEnd();
			if (start.toLocalDate().equals(end.toLocalDate())) {
				str.append(dateFormatter.format(start.toLocalDate()))
					.append(' ')
					.append(timeFormatter.format(start.toLocalTime()))
					.append(" - ")
					.append(timeFormatter.format(end.toLocalTime()));
			} else {
				str.append(dateTimeFormatter.format(start))
					.append(" - ")
					.append(dateTimeFormatter.format(end));
			}
		} else if (!ignoreDate) {
			LocalDate startDate = appointment.getStartDate();
			LocalDate lastDate = appointment.getLastDate();
			if (startDate.equals(lastDate)) {
				str.append(dateFormatter.format(startDate));
			} else {
				str.append(dateFormatter.format(startDate))
					.append(" - ")
					.append(dateFormatter.format(lastDate));
			}
		} else if (!ignoreTime) {
			str.append(timeFormatter.format(appointment.getStartTime()))
				.append(" - ")
				.append(timeFormatter.format(appointment.getEndTime()));
		}
		
		appointment.getLocation().get()
			.map(Location::getLabel)
			.filter(it -> !it.isEmpty())
			.ifPresent(location -> str.append(" - ").append(location));
		
		return str.toString();
	}
	
	public ReadOnlyObservable<String> getInfo() { return info; }
	
	public SubscriptionStack getSubscriptions() { return subscriptions; }
}
