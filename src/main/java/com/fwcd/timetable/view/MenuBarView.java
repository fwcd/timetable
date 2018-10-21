package com.fwcd.timetable.view;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarCrateModel;
import com.fwcd.timetable.view.print.CalendarPrinter;
import com.fwcd.timetable.view.settings.SettingsView;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.TimeTableAppViewModel;
import com.fwcd.timetable.viewmodel.utils.FileSaveManager;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

public class MenuBarView implements FxView {
	private final MenuBar node;
	private final TimeTableAppContext context;
	private final TimeTableAppViewModel viewModel;
	
	private final FileChooser fileChooser = new FileChooser();
	private final FileSaveManager fileSaveManager;
	private final CalendarPrinter printer;
	
	public MenuBarView(TimeTableAppContext context, TimeTableAppViewModel viewModel) {
		this.context = context;
		this.viewModel = viewModel;
		
		printer = new CalendarPrinter(context);
		fileSaveManager = new FileSaveManager(
			reader -> {
				CalendarCrateModel crate = viewModel.getCalendars().getModel();
				crate.loadFromJsonIn(reader);
				return crate.getChangeListeners();
			},
			writer -> viewModel.getCalendars().getModel().saveAsJsonTo(writer),
			this::showSaveDialog,
			this::showOpenDialog,
			context.getFileSaveState(),
			StandardCharsets.UTF_8
		);
		
		node = new MenuBar(
			FxUtils.menuOf(context.localized("filemenu"),
				FxUtils.menuItemOf(context.localized("open"), this::open, new KeyCodeCombination(KeyCode.O, FxUtils.CTRL_OR_CMD_DOWN)),
				FxUtils.menuItemOf(context.localized("save"), this::save, new KeyCodeCombination(KeyCode.S, FxUtils.CTRL_OR_CMD_DOWN)),
				FxUtils.menuItemOf(context.localized("saveas"), this::saveAs, new KeyCodeCombination(KeyCode.S, FxUtils.CTRL_OR_CMD_DOWN, KeyCombination.SHIFT_DOWN)),
				FxUtils.menuItemOf(context.localized("print"), this::print, new KeyCodeCombination(KeyCode.P, FxUtils.CTRL_OR_CMD_DOWN))
			),
			FxUtils.menuOf(context.localized("editmenu"),
				FxUtils.menuItemOf(context.localized("settings"), this::showSettings, new KeyCodeCombination(KeyCode.COMMA, FxUtils.CTRL_OR_CMD_DOWN))
			),
			FxUtils.menuOf(context.localized("languagemenu"),
				context.getLanguageManager().getLanguageKeys().stream()
					.map(key -> FxUtils.menuItemOf(key, () -> context.setLanguage(key)))
			)
		);
		
		if (FxUtils.isMacOS()) {
			node.setUseSystemMenuBar(true);
		}
	}
	
	private Option<Path> showOpenDialog() {
		return Option.ofNullable(fileChooser.showOpenDialog(node.getScene().getWindow())).map(File::toPath);
	}
	
	private Option<Path> showSaveDialog() {
		return Option.ofNullable(fileChooser.showSaveDialog(node.getScene().getWindow())).map(File::toPath);
	}
	
	private void open() {
		try {
			fileSaveManager.open();
		} catch (IOException e) {
			FxUtils.showExceptionAlert(context.localize("error"), e);
		}
	}
	
	private void save() {
		try {
			fileSaveManager.save();
		} catch (IOException e) {
			FxUtils.showExceptionAlert(context.localize("error"), e);
		}
	}
	
	private void saveAs() {
		try {
			fileSaveManager.saveAs();
		} catch (IOException e) {
			FxUtils.showExceptionAlert(context.localize("error"), e);
		}
	}
	
	private void print() {
		printer.showPrintDialog(node.getScene().getWindow(), viewModel.getCalendars());
	}
	
	private void showSettings() {
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.setTitle(context.localize("settings"));
		dialog.setHeaderText(context.localize("settings"));
		dialog.getDialogPane().setContent(new SettingsView(context).getNode());
		dialog.show();
	}
	
	@Override
	public Node getNode() { return node; }
}
