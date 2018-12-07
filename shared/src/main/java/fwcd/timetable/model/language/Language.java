package fwcd.timetable.model.language;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Language implements Serializable {
	private static final long serialVersionUID = -3794484912692629155L;
	private final String name;
	private final Map<String, String> mappings;
	
	public Language(String name, Map<String, String> mappings) {
		this.name = name;
		this.mappings = mappings;
	}
	
	public String getName() { return name; }
	
	public String unlocalize(String name) {
		List<String> result = mappings.entrySet()
			.stream()
			.map(Map.Entry::getKey)
			.filter(it -> it.equals(name))
			.collect(Collectors.toList());
		
		if (result.size() > 1) {
			throw new IllegalArgumentException("The localized language value '" + name + "' is ambiguous. Possible keys are: " + result);
		} else if (result.size() < 1) {
			throw new IllegalArgumentException("The localized language value '" + name + "' is not mapped by a key.");
		}
		
		return result.get(0);
	}
	
	/**
	 * Localizes a string, replacing {}s with the
	 * placeholder values.
	 */
	public String localize(String key, Object... placeholders) {
		String rawLocalized = mappings.get(key);
		if (rawLocalized == null) {
			throw new NoSuchElementException("Could not find language key '" + key + "' in language '" + name + "'");
		}
		if (placeholders.length > 0) {
			StringBuilder localized = new StringBuilder();
			int chars = rawLocalized.length();
			int i = 0;
			int placeholderIndex = 0;
			while (i < chars) {
				char c = rawLocalized.charAt(i);
				if ((c == '{') && (rawLocalized.charAt(i + 1) == '}')) {
					if (placeholderIndex >= placeholders.length) {
						throw new IllegalArgumentException("Too many placeholder values " + Arrays.toString(placeholders) + " for the localized string " + rawLocalized + " (key: " + key + ")");
					}
					localized.append(placeholders[placeholderIndex]);
					placeholderIndex++;
					i += 2;
				} else {
					localized.append(c);
					i++;
				}
			}
			return localized.toString();
		} else {
			return rawLocalized;
		}
	}
}
