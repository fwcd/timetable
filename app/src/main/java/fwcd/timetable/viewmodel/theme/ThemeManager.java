package fwcd.timetable.viewmodel.theme;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fwcd.fructose.Option;

public class ThemeManager {
	private final Map<String, Theme> themes = new HashMap<>();
	
	public ThemeManager() {
		addTheme(new Theme(ThemeKey.LIGHT, "/css/lightTheme.css"));
		addTheme(new Theme(ThemeKey.DARK, "/css/darkTheme.css"));
	}
	
	public Option<Theme> getTheme(String key) { return Option.ofNullable(themes.get(key)); }
	
	public Set<String> getThemeKeys() { return themes.keySet(); }
	
	public void addTheme(Theme theme) {
		themes.put(theme.getKey(), theme);
	}
}
