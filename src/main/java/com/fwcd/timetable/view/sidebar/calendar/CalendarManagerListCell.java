package com.fwcd.timetable.view.sidebar.calendar;

import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxUtils;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

public class CalendarManagerListCell extends ListCell<CalendarModel> {
	private static final int ICON_RADIUS = 5;
	private final Pane iconNode;
	private final Pane textNode;
	
	private final Label label;
	private final TextField textField;
	
	public CalendarManagerListCell() {
		label = new Label();
		label.setTextAlignment(TextAlignment.LEFT);
		
		textField = new TextField();
		textField.setOnAction(e -> commitEdit(getItem()));
		textField.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				cancelEdit();
			}
		});
		
		iconNode = new Pane();
		textNode = new Pane();
		
		HBox node = new HBox(iconNode, textNode);
		node.setAlignment(Pos.CENTER_LEFT);
		
		setGraphic(node);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		setEditModeEnabled(false);
	}
	
	@Override
	public void updateItem(CalendarModel item, boolean empty) {
		super.updateItem(item, empty);
		
		if ((item == null) || empty) {
			clearContents();
		} else {
			setIconColor(FxUtils.toFxColor(item.getColor().get()));
			if (isEditing()) {
				textField.setText(item.getName().get());
				setEditModeEnabled(true);
			} else {
				label.setText(item.getName().get());
				setEditModeEnabled(false);
			}
		}
	}
	
	@Override
	public void startEdit() {
		super.startEdit();
		
		textField.setText(getItem().getName().get());
		setEditModeEnabled(true);
		
		textField.selectAll();
		textField.requestFocus();
	}
	
	@Override
	public void cancelEdit() {
		super.cancelEdit();
		
		label.setText(getItem().getName().get());
		setEditModeEnabled(false);
	}
	
	@Override
	public void commitEdit(CalendarModel newValue) {
		super.commitEdit(newValue);
		
		String text = textField.getText();
		newValue.getName().set(text);
		label.setText(text);
		setEditModeEnabled(false);
	}
	
	private void setIconColor(Color color) {
		iconNode.getChildren().setAll(new Circle(ICON_RADIUS, color));
	}
	
	private void removeIcon() {
		iconNode.getChildren().clear();
	}
	
	private void setEditModeEnabled(boolean enabled) {
		if (enabled) {
			textNode.getChildren().setAll(textField);
		} else {
			textNode.getChildren().setAll(label);
		}
	}
	
	private void clearContents() {
		textField.setText("");
		label.setText("");
		setEditModeEnabled(false);
		removeIcon();
	}
}
