package fwcd.timetable.view.sidebar.calendar;

import fwcd.fructose.Option;
import fwcd.fructose.OptionInt;
import fwcd.fructose.draw.DrawColor;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.model.utils.Identified;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
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

public class CalendarManagerListCell extends ListCell<Identified<CalendarModel>> {
	private static final int ICON_RADIUS = 5;
	private final CalendarCrateViewModel viewModel;
	
	private final StackPane checkBoxNode;
	private final StackPane iconNode;
	private final StackPane textNode;
	
	private final Label label;
	private final TextField textField;
	
	public CalendarManagerListCell(TimeTableAppContext context, CalendarCrateViewModel viewModel) {
		this.viewModel = viewModel;
		
		label = new Label();
		label.getStyleClass().add("entry-cell-label");
		label.setTextAlignment(TextAlignment.LEFT);
		
		textField = new TextField();
		textField.setOnAction(e -> {
			Identified<CalendarModel> newItem = getItem().map(item -> item.withName(textField.getText()));
			viewModel.setCalendarById(newItem.getId(), newItem.getValue());
			commitEdit(newItem);
		});
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
			FxUtils.menuItemOf(context.localized("delete"), () -> getCalendarId().ifPresent(viewModel::removeCalendarById)),
			FxUtils.menuItemOf(context.localized("changecolor"), () -> getIdentifiedCalendar().ifPresent(cal ->
				FxUtils.showColorPicker(
					this,
					cal.getValue().getColor(),
					newColor -> viewModel.setCalendarById(cal.getId(), cal.getValue().withColor(newColor))
				)
			))
		));
		
		setGraphic(node);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		setEditModeEnabled(false);
	}
	
	private Option<Identified<CalendarModel>> getIdentifiedCalendar() { return Option.ofNullable(getItem()); }
	
	private OptionInt getCalendarId() { return getIdentifiedCalendar().mapToInt(Identified::getId); }
	
	@Override
	public void updateItem(Identified<CalendarModel> item, boolean empty) {
		super.updateItem(item, empty);
		
		if ((item == null) || empty) {
			clearContents();
		} else {
			showCheckBox(item.getId());
			showIconColor(item.getValue().getColor());
			setEditModeEnabled(isEditing());
			if (isEditing()) {
				textField.setText(item.getValue().getName());
			} else {
				label.setText(item.getValue().getName());
			}
		}
	}
	
	@Override
	public void startEdit() {
		super.startEdit();
		
		textField.setText(getItem().getValue().getName());
		setEditModeEnabled(true);
		
		textField.selectAll();
		textField.requestFocus();
	}
	
	@Override
	public void cancelEdit() {
		super.cancelEdit();
		
		label.setText(getItem().getValue().getName());
		setEditModeEnabled(false);
	}
	
	@Override
	public void commitEdit(Identified<CalendarModel> newValue) {
		super.commitEdit(newValue);
		setEditModeEnabled(false);
	}
	
	private void showIconColor(DrawColor color) {
		Circle circle = new Circle(ICON_RADIUS, ICON_RADIUS, ICON_RADIUS);
		circle.setFill(FxUtils.toFxColor(color));
		iconNode.getChildren().setAll(circle);
	}
	
	private void removeIcon() {
		iconNode.getChildren().clear();
	}
	
	private void showCheckBox(int calendarId) {
		CheckBox checkBox = new CheckBox();
		checkBox.setSelected(viewModel.getSelectedCalendarIds().contains(calendarId));
		checkBox.selectedProperty().addListener((obs, old, selected) -> {
			if (selected) {
				viewModel.selectCalendar(calendarId);
			} else {
				viewModel.deselectCalendar(calendarId);
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
