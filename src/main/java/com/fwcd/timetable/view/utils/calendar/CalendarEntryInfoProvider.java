package com.fwcd.timetable.view.utils.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.fructose.function.Subscription;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarEntryVisitor;
import com.fwcd.timetable.model.calendar.Location;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;

public class CalendarEntryInfoProvider implements CalendarEntryVisitor {
	private final TimeTableAppContext context;
	private final Observable<String> info = new Observable<>("");
	private final Collection<Subscription> subscriptions = new HashSet<>();
	
	public CalendarEntryInfoProvider(TimeTableAppContext context) {
		this.context = context;
	}
	
	@Override
	public void visitAppointment(AppointmentModel appointment) {
		Runnable updater = () -> info.set(getAppointmentInfo(appointment));
		
		subscriptions.add(appointment.getDateTimeInterval().subscribe(v -> updater.run()));
		subscriptions.add(appointment.getLocation().subscribe(v -> updater.run()));
		subscriptions.add(appointment.ignoresDate().subscribe(v -> updater.run()));
		subscriptions.add(appointment.ignoresTime().subscribe(v -> updater.run()));
		subscriptions.add(appointment.getRecurrence().getParsed().subscribe(v -> updater.run()));
		
		updater.run();
	}
	
	private String getAppointmentInfo(AppointmentModel appointment) {
		StringBuilder str = new StringBuilder();
		boolean ignoreDate = appointment.ignoresDate().get();
		boolean ignoreTime = appointment.ignoresTime().get();
		
		DateTimeFormatter dateFormatter = context.getDateFormatter().get();
		DateTimeFormatter timeFormatter = context.getTimeFormatter().get();
		DateTimeFormatter dateTimeFormatter = context.getDateTimeFormatter().get();
		
		appointment.getRecurrence().getParsed().get()
			.map(it -> it.describeWith(context.getLanguage().get(), dateFormatter))
			.ifPresent(recurrence -> str.append(recurrence).append(", "));
		
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
			.ifPresent(location -> str.append(", ").append(location));
		
		return str.toString();
	}
	
	public ReadOnlyObservable<String> getInfo() { return info; }
	
	public Collection<Subscription> getSubscriptions() { return subscriptions; }
}
