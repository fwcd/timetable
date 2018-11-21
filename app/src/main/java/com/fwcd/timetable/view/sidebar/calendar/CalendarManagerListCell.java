package com.fwcd.timetable.view.sidebar.calendar;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.fructose.draw.DrawColor;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.model.utils.SubscriptionStack;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

public class CalendarManagerListCell extends ListCell<CalendarModel> {
	private static final int ICON_RADIUS = 5;
	private final CalendarsViewModel viewModel;
	
	private final StackPane checkBoxNode;
	private final StackPane iconNode;
	private final StackPane textNode;
	
	private final Label label;
	private final TextField textField;
	
	private final SubscriptionStack itemSubscriptions = new SubscriptionStack();
	
	public CalendarManagerListCell(TimeTableAppContext context, CalendarsViewModel viewModel) {
		this.viewModel = viewModel;
		
		label = new Label();
		label.getStyleClass().add("entry-cell-label");
		label.setTextAlignment(TextAlignment.LEFT);
		
		textField = new TextField();
		textField.setOnAction(e -> commitEdit(getItem()));
		textField.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				cancelEdit();
			}
		});
		
		checkBoxNode = new StackPane();
		checkBoxNode.setAlignment(Pos.CENTER_LEFT);
		
		iconNode = new StackPane();
		iconNode.setAlignment(Pos.CENTER_LEFT);
		
		textNode = new StackPane();
		textNode.setAlignment(Pos.CENTER_LEFT);
		
		HBox node = new HBox(checkBoxNode, iconNode, textNode);
		node.getStyleClass().add("entry-cell");
		node.setSpacing(5);
		node.setAlignment(Pos.CENTER_LEFT);
		
		setContextMenu(new ContextMenu(
			FxUtils.menuItemOf(context.localized("delete"), () -> getCalendar().ifPresent(viewModel.getModel().getCalendars()::remove)),
			FxUtils.menuItemOf(context.localized("changecolor"), () -> getCalendar().ifPresent(it -> FxUtils.showColorPicker(this, it.getColor())))
		));
		
		setGraphic(node);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		setEditModeEnabled(false);
	}
	
	private Option<CalendarModel> getCalendar() {
		return Option.ofNullable(getItem());
	}
	
	@Override
	public void updateItem(CalendarModel item, boolean empty) {
		super.updateItem(item, empty);
		itemSubscriptions.unsubscribeAll();
		
		if ((item == null) || empty) {
			clearContents();
		} else {
			showCheckBox(item);
			setIconColor(item.getColor());
			if (isEditing()) {
				itemSubscriptions.push(item.getName().subscribeAndFire(textField::setText));
				textField.setText("");
				setEditModeEnabled(true);
			} else {
				itemSubscriptions.push(item.getName().subscribeAndFire(label::setText));
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
	
	private void setIconColor(ReadOnlyObservable<DrawColor> color) {
		Circle circle = new Circle(ICON_RADIUS, ICON_RADIUS, ICON_RADIUS);
		itemSubscriptions.push(color.subscribeAndFire(it -> circle.setFill(FxUtils.toFxColor(it))));
		iconNode.getChildren().setAll(circle);
	}
	
	private void removeIcon() {
		iconNode.getChildren().clear();
	}
	
	private void showCheckBox(CalendarModel calendar) {
		CheckBox checkBox = new CheckBox();
		itemSubscriptions.push(viewModel.getSelectedCalendars().subscribeAndFire(it -> {
			checkBox.setSelected(it.contains(calendar));
		}));
		checkBox.selectedProperty().addListener((obs, old, selected) -> {
			if (selected) {
				viewModel.getSelectedCalendars().add(calendar);
			} else {
				viewModel.getSelectedCalendars().remove(calendar);
			}
		});
		checkBoxNode.getChildren().setAll(checkBox);
	}
	
	private void removeCheckBox() {
		checkBoxNode.getChildren().clear();
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
		removeCheckBox();
	}
}
