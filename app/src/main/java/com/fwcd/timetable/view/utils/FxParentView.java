package com.fwcd.timetable.view.utils;

import com.fwcd.timetable.api.view.FxView;

import javafx.scene.Parent;

public interface FxParentView extends FxView {
	@Override
	Parent getNode();
}
