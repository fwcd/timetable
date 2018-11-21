package com.fwcd.timetable.view;

import javafx.scene.Node;

public class NamedFxView implements FxView {
	private final String name;
	private final boolean isLocalized;
	private final FxView delegate;
	
	private NamedFxView(String name, boolean isLocalized, FxView delegate) {
		this.name = name;
		this.isLocalized = isLocalized;
		this.delegate = delegate;
	}
	
	public static NamedFxView ofUnlocalized(String unlocalized, FxView delegate) {
		return new NamedFxView(unlocalized, false, delegate);
	}
	
	public static NamedFxView of(String name, FxView delegate) {
		return new NamedFxView(name, true, delegate);
	}
	
	public String getName() { return name; }
	
	public boolean isNameLocalized() { return isLocalized; }
	
	@Override
	public Node getNode() { return delegate.getNode(); }
}
