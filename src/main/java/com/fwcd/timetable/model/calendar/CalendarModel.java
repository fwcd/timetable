package com.fwcd.timetable.model.calendar;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.draw.DrawColor;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.task.TaskCrateModel;

public class CalendarModel {
	private final Observable<String> name;
	private final Observable<DrawColor> color = new Observable<>(randomColor());
	private final ObservableList<AppointmentModel> appointments = new ObservableList<>();
	private final TaskCrateModel taskCrate = new TaskCrateModel();
	
	public CalendarModel(String name) {
		this.name = new Observable<>(name);
	}
	
	public Observable<DrawColor> getColor() { return color; }
	
	public ObservableList<AppointmentModel> getAppointments() { return appointments; }
	
	public TaskCrateModel getTaskCrate() { return taskCrate; }
	
	public Observable<String> getName() { return name; }
	
	public Stream<CalendarEntryModel> streamEntries() {
		return Stream.concat(
			appointments.stream(),
			taskCrate.getLists().stream().flatMap(it -> it.getTasks().stream())
		);
	}
	
	private DrawColor randomColor() {
		Random random = ThreadLocalRandom.current();
		return new DrawColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
	
	public void addRandomSampleEntries() {
		for (int i = 0; i < 24; i += 2) {
			appointments.add(new AppointmentModel.Builder("Test")
				.start(LocalDateTime.now().plusHours(i))
				.end(LocalDateTime.now().plusHours(ThreadLocalRandom.current().nextInt(i, i + 4)))
				.build());
		}
	}
	
	@Override
	public String toString() {
		return name.get();
	}
}
