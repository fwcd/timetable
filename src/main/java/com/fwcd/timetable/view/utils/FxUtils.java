package com.fwcd.timetable.view.utils;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public final class FxUtils {
	private FxUtils() {}
	
	public static void setVerticalScrollSpeed(ScrollPane pane, int multiplier) {
		// Source: https://stackoverflow.com/questions/32739269/how-do-i-change-the-amount-by-which-scrollpane-scrolls
		Node content = pane.getContent();
		content.setOnScroll(event -> {
			double dy = event.getDeltaY() * multiplier;
			double width = content.getBoundsInLocal().getWidth();
			double vvalue = pane.getVvalue();
			pane.setVvalue(vvalue - (dy / width));
		});
	}
}
