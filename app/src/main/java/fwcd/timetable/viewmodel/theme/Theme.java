package fwcd.timetable.viewmodel.theme;

public class Theme {
	private final String key;
	private final String cssResourcePath;

	public Theme(String key, String cssResourcePath) {
		this.key = key;
		this.cssResourcePath = cssResourcePath;
	}

	public String getCssResourcePath() { return cssResourcePath; }

	public String getKey() { return key; }
}
