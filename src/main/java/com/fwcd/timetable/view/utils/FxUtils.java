package com.fwcd.timetable.view.utils;

import java.util.List;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.fructose.draw.DrawColor;
import com.fwcd.fructose.io.DelegatePrintStream;
import com.fwcd.fructose.structs.ReadOnlyObservableList;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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
	
	public static Button buttonOf(String label, Runnable action) {
		Button button = new Button(label);
		button.setOnAction(e -> action.run());
		return button;
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
		TextField textField = new TextField();
		bindBidirectionally(text, textField.textProperty());
		return textField;
	}
	
	public static <T> void bindBidirectionally(Observable<T> fructoseObservable, Property<T> fxProperty) {
		Flag updating = new Flag();
		fructoseObservable.listenAndFire(it -> {
			if (!updating.value) {
				updating.value = true;
				fxProperty.setValue(it);
				updating.value = false;
			}
		});
		fxProperty.addListener((obs, old, newValue) -> {
			if (!updating.value) {
				updating.value = true;
				fructoseObservable.set(newValue);
				updating.value = false;
			}
		});
	}
	
	public static <T> ComboBox<T> comboBoxOf(List<T> values) {
		ComboBox<T> box = new ComboBox<>(FXCollections.observableList(values));
		return box;
	}
	
	public static <T> ComboBox<T> comboBoxOfObservable(ReadOnlyObservable<List<T>> values) {
		ComboBox<T> box = new ComboBox<>();
		values.listenAndFire(box.getItems()::setAll);
		return box;
	}
	
	public static <T> ComboBox<T> comboBoxOfObservable(ReadOnlyObservableList<T> values) {
		ComboBox<T> box = new ComboBox<>();
		values.listenAndFire(box.getItems()::setAll);
		return box;
	}

	public static Color toFxColor(DrawColor drawColor) {
		return new Color(
			(double) drawColor.getR() / 255D,
			(double) drawColor.getG() / 255D,
			(double) drawColor.getB() / 255D,
			(double) drawColor.getAlpha() / 255D
		);
	}
	
	public static void showExceptionAlert(String title, Throwable e) {
		Alert alert = new Alert(AlertType.ERROR);
		StringBuilder msg = new StringBuilder(e.getMessage() + System.lineSeparator());
		
		e.printStackTrace(new DelegatePrintStream(msg::append));
		msg.delete(600, msg.length());
		
		alert.setTitle(title);
		alert.setHeaderText(e.getClass().getName());
		alert.setContentText(msg.toString());
		
		alert.showAndWait();
	}
}
