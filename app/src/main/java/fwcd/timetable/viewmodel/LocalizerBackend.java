package fwcd.timetable.viewmodel;

import java.util.HashMap;
import java.util.Map;

import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.model.language.Language;

public class LocalizerBackend implements Localizer {
	private final Map<String, ReadOnlyObservable<String>> cachedLocalizations = new HashMap<>();
	private final ReadOnlyObservable<Language> language;
	
	public LocalizerBackend(ReadOnlyObservable<Language> language) {
		this.language = language;
	}
	
	@Override
	public String localize(String unlocalized) {
		return language.get().localize(unlocalized);
	}
	
	@Override
	public ReadOnlyObservable<String> localized(String unlocalized) {
		ReadOnlyObservable<String> result = cachedLocalizations.get(unlocalized);
		if (result == null) {
			result = language.mapStrongly(it -> it.localize(unlocalized));
			cachedLocalizations.put(unlocalized, result);
		}
		return result;
	}
	
	@Override
	public Language getLanguage() {
		return language.get();
	}
}
