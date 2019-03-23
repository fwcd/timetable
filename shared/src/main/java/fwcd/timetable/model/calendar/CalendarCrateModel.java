package fwcd.timetable.model.calendar;

import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.ListenerList;
import fwcd.fructose.structs.ObservableList;
import fwcd.timetable.model.utils.SubscriptionStack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CalendarCrateModel implements Serializable {
	private static final long serialVersionUID = -1914115703122727566L;
	private static final Gson GSON = CalendarSerializationUtils.newGson();
	private static final Type CALENDARS_TYPE = new TypeToken<List<CalendarModel>>() {}.getType();
	
	private final ObservableList<CalendarModel> calendars = new ObservableList<>();
	
	private transient EventListenerList<CalendarCrateModel> nullableChangeListeners;
	private transient EventListenerList<CalendarCrateModel> nullableStructuralChangeListeners;
	private transient ListenerList nullableOnLoadListeners;
	private transient SubscriptionStack nullableCalendarSubscriptions;
	
	public CalendarCrateModel() {
		setupChangeListeners();
	}
	
	private void setupChangeListeners() {
		calendars.listenAndFire(it -> {
			getChangeListeners().fire(this);
			getStructuralChangeListeners().fire(this);
			getCalendarSubscriptions().unsubscribeAll();
			getCalendarSubscriptions().subscribeAll(calendars, CalendarModel::getChangeListeners, cal -> getChangeListeners().fire(this));
			getCalendarSubscriptions().subscribeAll(calendars, CalendarModel::getStructuralChangeListeners, cal -> getStructuralChangeListeners().fire(this));
		});
	}
	
	public ObservableList<CalendarModel> getCalendars() { return calendars; }
	
	public void saveAsJsonTo(Appendable writer) {
		GSON.toJson(calendars.get(), CALENDARS_TYPE, writer);
	}
	
	public void loadFromJsonIn(Reader reader) {
		calendars.set(GSON.fromJson(reader, CALENDARS_TYPE));
		getOnLoadListeners().fire();
	}
	
	public void createNewCalendars() {
		calendars.clear();
		calendars.add(new CalendarModel("Calendar"));
		getOnLoadListeners().fire();
	}
	
	public EventListenerList<CalendarCrateModel> getChangeListeners() {
		if (nullableChangeListeners == null) {
			nullableChangeListeners = new EventListenerList<>();
		}
		return nullableChangeListeners;
	}
	
	public EventListenerList<CalendarCrateModel> getStructuralChangeListeners() {
		if (nullableStructuralChangeListeners == null) {
			nullableStructuralChangeListeners = new EventListenerList<>();
		}
		return nullableStructuralChangeListeners;
	}
	
	public SubscriptionStack getCalendarSubscriptions() {
		if (nullableCalendarSubscriptions == null) {
			nullableCalendarSubscriptions = new SubscriptionStack();
		}
		return nullableCalendarSubscriptions;
	}
	
	public ListenerList getOnLoadListeners() {
		if (nullableOnLoadListeners == null) {
			nullableOnLoadListeners = new ListenerList();
		}
		return nullableOnLoadListeners;
	}
}
