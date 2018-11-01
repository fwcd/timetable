package com.fwcd.timetable.view.git;

import com.fwcd.timetable.api.view.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class GitView implements FxView {
	private final Pane node;
	
	public GitView() {
		node = new Pane();
		
	}
	
	@Override
	public Node getNode() { return node; }
}
