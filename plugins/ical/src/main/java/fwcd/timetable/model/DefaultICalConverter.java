package fwcd.timetable.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.stream.Collectors;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor.AppointmentsOnly;
import fwcd.timetable.model.calendar.Location;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;

public class DefaultICalConverter implements ICalConverter {
	@Override
	public Collection<CalendarEntryModel> toTimeTableEntries(Calendar iCal, int calendarId) {
		return iCal.<VEvent>getComponents(Component.VEVENT).stream()
			.map(e -> toTimeTableEntry(e, calendarId))
			.collect(Collectors.toList());
	}
	
	private AppointmentModel toTimeTableEntry(VEvent event, int calendarId) {
		// TODO: Recurrences
		return new AppointmentModel.Builder(Option.ofNullable(event.getSummary()).map(Property::getValue).orElse(event.getName()), calendarId)
			.location(Option.ofNullable(event.getLocation()).map(Property::getValue).map(Location::new))
			.start(toLocalDateTime(event.getStartDate().getDate()))
			.end(toLocalDateTime(event.getEndDate().getDate()))
			.description(Option.ofNullable(event.getDescription()).map(Property::getValue).orElse(""))
			.build();
	}
	
	private LocalDateTime toLocalDateTime(Date date) {
		// TODO: Use time zones throughout the TimeTable application
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	@Override
	public Calendar toiCalCalendar(Collection<? extends CalendarEntryModel> entries) {
		Calendar iCal = new Calendar();
		entries.stream()
			.flatMap(it -> it.accept(new AppointmentsOnly()).stream())
			.map(this::toiCalEvent)
			.forEach(iCal.getComponents()::add);
		return iCal;
	}
	
	private VEvent toiCalEvent(AppointmentModel appointment) {
		// TODO: Recurrences
		VEvent event = new VEvent(toDate(appointment.getStart()), toDate(appointment.getEnd()), appointment.getName());
		PropertyList<Property> properties = event.getProperties();
		appointment.getLocation().map(Location::getLabel).map(net.fortuna.ical4j.model.property.Location::new).ifPresent(properties::add);
		return event;
	}
	
	private Date toDate(LocalDateTime dateTime) {
		return new Date(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}
}
