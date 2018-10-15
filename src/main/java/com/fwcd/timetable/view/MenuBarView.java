package com.fwcd.timetable.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppViewModel;
import com.google.gson.JsonIOException;

import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;

public class MenuBarView implements FxView {
	private final MenuBar node;
	private final FileChooser fileChooser = new FileChooser();
	private final TimeTableAppViewModel viewModel;
	
	public MenuBarView(TimeTableAppContext context, TimeTableAppViewModel viewModel) {
		this.viewModel = viewModel;
		node = new MenuBar(
			FxUtils.menuOf(context.localized("filemenu"),
				FxUtils.menuItemOf(context.localized("open"), this::showOpenDialog),
				FxUtils.menuItemOf(context.localized("save"), this::showSaveDialog)
			),
			FxUtils.menuOf(context.localized("editmenu")),
			FxUtils.menuOf(context.localized("languagemenu"),
				context.getLanguageManager().getLanguageKeys().stream()
					.map(key -> FxUtils.menuItemOf(key, () -> context.getLanguageManager().setLanguage(key)))
			)
		);
	}
	
	private void showOpenDialog() {
		Path file = fileChooser.showOpenDialog(node.getScene().getWindow()).toPath();
		if (file != null) {
			try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
				viewModel.getCalendars().getModel().loadFromJsonIn(reader);
			} catch (IOException | JsonIOException e) {
				FxUtils.showExceptionAlert("Exception", e);
			}
		}
	}
	
	private void showSaveDialog() {
		Path file = fileChooser.showSaveDialog(node.getScene().getWindow()).toPath();
		if (file != null) {
			try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
				viewModel.getCalendars().getModel().saveAsJsonTo(writer);
			} catch (IOException | JsonIOException e) {
				FxUtils.showExceptionAlert("Exception", e);
			}
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
