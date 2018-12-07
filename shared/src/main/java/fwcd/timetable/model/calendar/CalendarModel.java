package fwcd.timetable.model.calendar;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.Observable;
import fwcd.fructose.draw.DrawColor;
import fwcd.fructose.structs.ObservableList;
import fwcd.timetable.model.calendar.task.TaskCrateModel;
import fwcd.timetable.model.json.PostDeserializable;
import fwcd.timetable.model.utils.SubscriptionStack;

public class CalendarModel implements Serializable, PostDeserializable {
	private static final long serialVersionUID = 831554590083407654L;
	private final Observable<String> name;
	private final Observable<DrawColor> color = new Observable<>(randomColor());
	private final ObservableList<AppointmentModel> appointments = new ObservableList<>();
	private final TaskCrateModel taskCrate = new TaskCrateModel();
	
	private transient EventListenerList<CalendarModel> nullableChangeListeners;
	private transient EventListenerList<CalendarModel> nullableStructuralChangeListeners;
	private transient SubscriptionStack nullableAppointmentSubscriptions;
	
	public CalendarModel() {
		name = new Observable<>("");
		setupChangeListeners();
	}
	
	public CalendarModel(String name) {
		this.name = new Observable<>(name);
		setupChangeListeners();
	}
	
	@Override
	public void postDeserialize() {
		setupChangeListeners();
	}
	
	private void setupChangeListeners() {
		name.listen(it -> getChangeListeners().fire(this));
		color.listen(it -> getChangeListeners().fire(this));
		taskCrate.getChangeListeners().listen(it -> {
			getChangeListeners().fire(this);
			// TODO: More fine-grained event handling for task crates
			getStructuralChangeListeners().fire(this);
		});
		appointments.listenAndFire(it -> {
			getChangeListeners().fire(this);
			getStructuralChangeListeners().fire(this);
			getAppointmentSubscriptions().unsubscribeAll();
			getAppointmentSubscriptions().subscribeAll(appointments, AppointmentModel::getChangeListeners, app -> getChangeListeners().fire(this));
			getAppointmentSubscriptions().subscribeAll(appointments, AppointmentModel::getStructuralChangeListeners, app -> getStructuralChangeListeners().fire(this));
		});
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
	
	public EventListenerList<CalendarModel> getChangeListeners() {
		if (nullableChangeListeners == null) {
			nullableChangeListeners = new EventListenerList<>();
		}
		return nullableChangeListeners;
	}
	
	public EventListenerList<CalendarModel> getStructuralChangeListeners() {
		if (nullableStructuralChangeListeners == null) {
			nullableStructuralChangeListeners = new EventListenerList<>();
		}
		return nullableStructuralChangeListeners;
	}
	
	private SubscriptionStack getAppointmentSubscriptions() {
		if (nullableAppointmentSubscriptions == null) {
			nullableAppointmentSubscriptions = new SubscriptionStack();
		}
		return nullableAppointmentSubscriptions;
	}
	
	@Override
	public String toString() {
		return name.get();
	}
}
