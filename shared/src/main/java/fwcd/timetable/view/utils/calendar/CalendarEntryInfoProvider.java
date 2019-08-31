package fwcd.timetable.view.utils.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;

public class CalendarEntryInfoProvider implements CalendarEntryVisitor<String> {
	private final Localizer localizer;
	private final TemporalFormatters formatters;
	
	public CalendarEntryInfoProvider(Localizer localizer, TemporalFormatters formatters) {
		this.localizer = localizer;
		this.formatters = formatters;
	}
	
	@Override
	public String visitCalendarEntry(CalendarEntryModel entry) {
		return "";
	}
	
	@Override
	public String visitAppointment(AppointmentModel appointment) {
		StringBuilder str = new StringBuilder();
		boolean ignoreDate = appointment.ignoresDate();
		boolean ignoreTime = appointment.ignoresTime();
		
		DateTimeFormatter dateFormatter = formatters.getDateFormatter();
		DateTimeFormatter timeFormatter = formatters.getTimeFormatter();
		DateTimeFormatter dateTimeFormatter = formatters.getDateTimeFormatter();
		
		appointment.getRecurrence()
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
		
		appointment.getLocation()
			.map(Location::getLabel)
			.filter(it -> !it.isEmpty())
			.ifPresent(location -> str.append(" - ").append(location));
		
		return str.toString();
	}
}
