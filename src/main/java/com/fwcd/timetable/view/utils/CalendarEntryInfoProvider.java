package com.fwcd.timetable.view.utils;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.fructose.function.Subscription;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarEntryVisitor;

public class CalendarEntryInfoProvider implements CalendarEntryVisitor {
	private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	
	private final Observable<String> info = new Observable<>("");
	private final Collection<Subscription> subscriptions = new HashSet<>();
	
	@Override
	public void visitAppointment(AppointmentModel appointment) {
		Runnable updater = () -> info.set(getAppointmentInfo(appointment));
		
		subscriptions.add(appointment.getDateTimeInterval().subscribe(v -> updater.run()));
		subscriptions.add(appointment.getLocation().subscribe(v -> updater.run()));
		subscriptions.add(appointment.ignoresDate().subscribe(v -> updater.run()));
		subscriptions.add(appointment.ignoresTime().subscribe(v -> updater.run()));
		
		updater.run();
	}
	
	private String getAppointmentInfo(AppointmentModel appointment) {
		StringBuilder str = new StringBuilder();
		boolean ignoreDate = appointment.ignoresDate().get();
		boolean ignoreTime = appointment.ignoresTime().get();
		
		if (!ignoreDate && !ignoreTime) {
			str.append(DT_FORMATTER.format(appointment.getStart()))
				.append(" - ")
				.append(DT_FORMATTER.format(appointment.getEnd()));
		} else if (!ignoreDate) {
			str.append(DATE_FORMATTER.format(appointment.getStartDate()))
				.append(" - ")
				.append(DATE_FORMATTER.format(appointment.getLastDate()));
		} else if (!ignoreTime) {
			str.append(TIME_FORMATTER.format(appointment.getStartTime()))
				.append(" - ")
				.append(TIME_FORMATTER.format(appointment.getEndTime()));
		}
		
		appointment.getLocation().get().ifPresent(location -> {
			str.append(" - ").append(location.getLabel());
		});
		
		return str.toString();
	}
	
	public ReadOnlyObservable<String> getInfo() { return info; }
	
	public Collection<Subscription> getSubscriptions() { return subscriptions; }
}
