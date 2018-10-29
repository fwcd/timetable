package com.fwcd.timetable.api.view;

import javafx.scene.Node;

public class NamedFxView implements FxView {
	private final String name;
	private final FxView delegate;
	
	public NamedFxView(String name, FxView delegate) {
		this.name = name;
		this.delegate = delegate;
	}
	
	public String getName() { return name; }
	
	@Override
	public Node getNode() { return delegate.getNode(); }
}
