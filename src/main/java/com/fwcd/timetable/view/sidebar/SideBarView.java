package com.fwcd.timetable.view.sidebar;

import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class SideBarView implements FxView {
	private final Pane pane;
	
	public SideBarView() {
		pane = new Pane();
	}
	
	@Override
	public Node getNode() { return pane; }
}
