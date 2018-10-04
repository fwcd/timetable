package com.fwcd.timetable.view.utils;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.fructose.function.Subscription;
import com.fwcd.fructose.time.LocalTimeInterval;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarEntryVisitor;
import com.fwcd.timetable.model.calendar.tt.TimeTableEntryModel;

public class CalendarEntryInfoProvider implements CalendarEntryVisitor {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
	private final Observable<String> info = new Observable<>("");
	private final Collection<Subscription> subscriptions = new HashSet<>();
	
	@Override
	public void visitAppointment(AppointmentModel appointment) {
		Runnable updater = () -> info.set(getAppointmentInfo(appointment));
		
		subscriptions.add(appointment.getDateInterval().subscribe(v -> updater.run()));
		subscriptions.add(appointment.getTimeInterval().subscribe(v -> updater.run()));
		subscriptions.add(appointment.getLocation().subscribe(v -> updater.run()));
		
		updater.run();
	}
	
	@Override
	public void visitTimeTableEntry(TimeTableEntryModel ttEntry) {
		Runnable updater = () -> info.set(getTimeTableEntryInfo(ttEntry));
		
		subscriptions.add(ttEntry.getTimeInterval().subscribe(v -> updater.run()));
		subscriptions.add(ttEntry.getLocation().subscribe(v -> updater.run()));
		
		updater.run();
	}
	
	private String getAppointmentInfo(AppointmentModel appointment) {
		StringBuilder str = new StringBuilder();
		
		str.append(FORMATTER.format(appointment.getStart()))
			.append(" - ")
			.append(FORMATTER.format(appointment.getEnd()));
		
		appointment.getLocation().get().ifPresent(location -> {
			str.append(" - ").append(location.getLabel());
		});
		
		return str.toString();
	}
	
	private String getTimeTableEntryInfo(TimeTableEntryModel ttEntry) {
		StringBuilder str = new StringBuilder();
		LocalTimeInterval timeInterval = ttEntry.getTimeInterval().get();
		
		str.append(FORMATTER.format(timeInterval.getStart()))
			.append(" - ")
			.append(FORMATTER.format(timeInterval.getEnd()));
		
		ttEntry.getLocation().get().ifPresent(location -> {
			str.append(" - ").append(location.getLabel());
		});
		
		return str.toString();
	}
	
	public ReadOnlyObservable<String> getInfo() { return info; }
	
	public Collection<Subscription> getSubscriptions() { return subscriptions; }
}
