package com.fwcd.timetable.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.fwcd.timetable.view.settings.SettingsView;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.TimeTableAppViewModel;
import com.google.gson.JsonIOException;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;

public class MenuBarView implements FxView {
	private final MenuBar node;
	private final FileChooser fileChooser = new FileChooser();
	private final TimeTableAppViewModel viewModel;
	private final TimeTableAppContext context;
	
	public MenuBarView(TimeTableAppContext context, TimeTableAppViewModel viewModel) {
		this.viewModel = viewModel;
		this.context = context;
		
		node = new MenuBar(
			FxUtils.menuOf(context.localized("filemenu"),
				FxUtils.menuItemOf(context.localized("open"), this::showOpenDialog),
				FxUtils.menuItemOf(context.localized("save"), this::showSaveDialog)
			),
			FxUtils.menuOf(context.localized("editmenu"),
				FxUtils.menuItemOf(context.localized("settings"), this::showSettings)
			),
			FxUtils.menuOf(context.localized("languagemenu"),
				context.getLanguageManager().getLanguageKeys().stream()
					.map(key -> FxUtils.menuItemOf(key, () -> context.setLanguage(key)))
			)
		);
		
		if (System.getProperty("os.name").contains("Mac")) {
			node.setUseSystemMenuBar(true);
		}
	}
	
	private void showOpenDialog() {
		File file = fileChooser.showOpenDialog(node.getScene().getWindow());
		if (file != null) {
			try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
				viewModel.getCalendars().getModel().loadFromJsonIn(reader);
			} catch (IOException | JsonIOException e) {
				FxUtils.showExceptionAlert(context.localize("openerror"), e);
			}
		}
	}
	
	private void showSaveDialog() {
		File file = fileChooser.showSaveDialog(node.getScene().getWindow());
		if (file != null) {
			try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
				viewModel.getCalendars().getModel().saveAsJsonTo(writer);
			} catch (IOException | JsonIOException e) {
				FxUtils.showExceptionAlert(context.localize("saveerror"), e);
			}
		}
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
