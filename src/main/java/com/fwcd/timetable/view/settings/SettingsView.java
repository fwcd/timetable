package com.fwcd.timetable.view.settings;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.fwcd.fructose.Observable;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.settings.TimeTableAppSettings;
import com.fwcd.timetable.viewmodel.settings.TimeTableAppSettings.Builder;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SettingsView implements FxView {
	private final GridPane node;
	private final Observable<TimeTableAppSettings> model;
	
	public SettingsView(TimeTableAppContext context) {
		model = context.getSettings();
		
		int rowIndex = 1;
		node = new GridPane();
		
		node.addRow(rowIndex++, localizedPropertyLabel("language", context), boundTextField(TimeTableAppSettings::getLanguage, Builder::language));
		node.addRow(rowIndex++, localizedPropertyLabel("dateformat", context), boundTextField(TimeTableAppSettings::getDateFormat, Builder::dateFormat));
		node.addRow(rowIndex++, localizedPropertyLabel("timeformat", context), boundTextField(TimeTableAppSettings::getTimeFormat, Builder::timeFormat));
		node.addRow(rowIndex++, localizedPropertyLabel("datetimeformat", context), boundTextField(TimeTableAppSettings::getDateTimeFormat, Builder::dateTimeFormat));
	}
	
	private TextField boundTextField(Function<TimeTableAppSettings, String> getter, BiFunction<Builder, String, Builder> setter) {
		TextField field = new TextField();
		FxUtils.bindBidirectionally(model, field.textProperty(), getter, newVal -> setter.apply(model.get().with(), newVal).build());
		return field;
	}

	private Label localizedPropertyLabel(String unlocalized, TimeTableAppContext context) {
		return FxUtils.labelOf(context.localized(unlocalized), ": ");
	}
	
	@Override
	public Node getNode() { return node; }
}
