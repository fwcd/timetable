package com.fwcd.timetable.view.utils;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.ReadOnlyObservable;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

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
	
	/**
	 * Creates a button with the provided action and an unidirectional
	 * data binding supplying the label.
	 */
	public static Button buttonOf(ReadOnlyObservable<String> label, Runnable action) {
		Button button = new Button();
		label.listenAndFire(button::setText);
		button.setOnAction(e -> action.run());
		return button;
	}
	
	public static Label labelOf(ReadOnlyObservable<String> text) {
		return labelOf(text, "");
	}
	
	/**
	 * Creates a label with an unidirectional data binding
	 * to the provided {@link ReadOnlyObservable}.
	 */
	public static Label labelOf(ReadOnlyObservable<String> text, String appended) {
		Label label = new Label();
		text.listenAndFire(it -> label.setText(it + appended));
		return label;
	}
	
	private static class Flag {
		boolean value = false;
	}
	
	/**
	 * Creates a text field with a bidirectional data binding
	 * to the provided {@link Observable}.
	 */
	public static TextField textFieldOf(Observable<String> text) {
		Flag updating = new Flag();
		TextField textField = new TextField();
		text.listenAndFire(it -> {
			if (!updating.value) {
				updating.value = true;
				textField.setText(it);
				updating.value = false;
			}
		});
		textField.textProperty().addListener((obs, old, newValue) -> {
			if (!updating.value) {
				updating.value = true;
				text.set(newValue);
				updating.value = false;
			}
		});
		return textField;
	}
}
