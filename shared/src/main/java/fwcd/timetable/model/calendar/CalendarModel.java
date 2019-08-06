package fwcd.timetable.model.calendar;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import fwcd.fructose.draw.DrawColor;
import fwcd.timetable.model.calendar.task.TaskCrateModel;

public class CalendarModel implements Serializable {
	private static final long serialVersionUID = 831554590083407654L;
	private final String name;
	private final DrawColor color = randomColor();
	private final List<AppointmentModel> appointments = new ArrayList<>();
	private final TaskCrateModel taskCrate = new TaskCrateModel();
	
	public CalendarModel() {
		name = "";
	}
	
	public CalendarModel(String name) {
		this.name = name;
	}
	
	public DrawColor getColor() { return color; }
	
	public List<AppointmentModel> getAppointments() { return appointments; }
	
	public TaskCrateModel getTaskCrate() { return taskCrate; }
	
	public String getName() { return name; }
	
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
		return name;
	}
}
