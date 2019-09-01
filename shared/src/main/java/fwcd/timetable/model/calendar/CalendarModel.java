package fwcd.timetable.model.calendar;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import fwcd.fructose.draw.DrawColor;

/**
 * Metadata information about a calendar. Since calendar
 * entries such as appointments each hold an ID reference
 * to their calendar, the entries are *not* stored in this class.
 */
public class CalendarModel implements Serializable {
	private static final long serialVersionUID = 831554590083407654L;
	private final String name;
	private final DrawColor color = randomColor();
	
	public CalendarModel() {
		name = "";
	}
	
	public CalendarModel(String name) {
		this.name = name;
	}
	
	public DrawColor getColor() { return color; }
	
	public String getName() { return name; }
	
	private DrawColor randomColor() {
		Random random = ThreadLocalRandom.current();
		return new DrawColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
	
	@Override
	public String toString() {
		return "Calendar { name = " + name + ", color = " + color + " }";
	}
}
