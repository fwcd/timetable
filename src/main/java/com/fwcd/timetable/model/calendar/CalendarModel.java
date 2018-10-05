package com.fwcd.timetable.model.calendar;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import com.fwcd.fructose.StreamUtils;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.task.TaskCrateModel;

public class CalendarModel {
	private final ObservableList<AppointmentModel> appointments = new ObservableList<>();
	private final TaskCrateModel taskCrate = new TaskCrateModel();
	
	public ObservableList<AppointmentModel> getAppointments() { return appointments; }
	
	public TaskCrateModel getTaskCrate() { return taskCrate; }
	
	public Stream<CalendarEntryModel> streamEntries() {
		return StreamUtils.merge(
			appointments.stream(),
			taskCrate.getLists().stream().flatMap(it -> it.getTasks().stream())
		);
	}
	
	public void addRandomSampleEntries() {
		for (int i = 0; i < 24; i += 2) {
			appointments.add(new AppointmentModel.Builder("Test")
				.start(LocalDateTime.now().plusHours(i))
				.end(LocalDateTime.now().plusHours(ThreadLocalRandom.current().nextInt(i, i + 4)))
				.build());
		}
	}
}
