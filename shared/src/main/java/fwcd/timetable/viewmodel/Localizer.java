package fwcd.timetable.viewmodel;

import fwcd.fructose.ReadOnlyObservable;

public interface Localizer {
	String localize(String unlocalized);
	
	ReadOnlyObservable<String> localized(String unlocalized);
}
