package fwcd.timetable.viewmodel;

import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.model.language.Language;

public interface Localizer {
	String localize(String unlocalized);
	
	ReadOnlyObservable<String> localized(String unlocalized);
	
	Language getLanguage();
}
