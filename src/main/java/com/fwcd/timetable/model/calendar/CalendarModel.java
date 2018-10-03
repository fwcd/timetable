package com.fwcd.timetable.model.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.fructose.time.LocalDateInterval;
import com.fwcd.fructose.time.LocalTimeInterval;
import com.fwcd.timetable.model.utils.ArrayUtils;

public class CalendarModel {
	private final ObservableList<AppointmentModel> appointments = new ObservableList<>();
	private final ObservableList<TimeTableModel> timeTables = new ObservableList<>();
	
	public ObservableList<AppointmentModel> getAppointments() { return appointments; }
	
	public ObservableList<TimeTableModel> getTimeTables() { return timeTables; }
	
	public void addRandomSampleEntries() {
		TimeTableModel tt = new TimeTableModel(new LocalDateInterval(LocalDate.now(), LocalDate.now().plusDays(32)));
		for (int i = 0; i < 24; i += 2) {
			appointments.add(new AppointmentModel.Builder("Test")
				.start(LocalDateTime.now().plusHours(i))
				.end(LocalDateTime.now().plusHours(ThreadLocalRandom.current().nextInt(i, i + 4)))
				.build());
			tt.getEntries().add(tt.createEntry("TimeTable Entry", Option.empty(), new LocalTimeInterval(
				LocalTime.now().plusHours(i - 1),
				LocalTime.now().plusHours(ThreadLocalRandom.current().nextInt(i, i + 5))
			), ArrayUtils.pickRandom(DayOfWeek.values())));
		}
		timeTables.add(tt);
	}
}
