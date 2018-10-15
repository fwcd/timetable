package com.fwcd.timetable.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
		File file = fileChooser.showOpenDialog(node.getScene().getWindow());
		if (file != null) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				viewModel.getCalendars().getModel().loadFromJsonIn(reader);
			} catch (IOException | JsonIOException e) {
				FxUtils.showExceptionAlert("Exception", e);
			}
		}
	}
	
	private void showSaveDialog() {
		File file = fileChooser.showSaveDialog(node.getScene().getWindow());
		if (file != null) {
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
				viewModel.getCalendars().getModel().saveAsJsonTo(writer);
			} catch (IOException | JsonIOException e) {
				FxUtils.showExceptionAlert("Exception", e);
			}
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
