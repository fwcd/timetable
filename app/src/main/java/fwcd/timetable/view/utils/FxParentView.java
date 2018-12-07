package fwcd.timetable.view.utils;

import fwcd.timetable.view.FxView;

import javafx.scene.Parent;

public interface FxParentView extends FxView {
	@Override
	Parent getNode();
}
