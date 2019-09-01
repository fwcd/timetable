package fwcd.timetable.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fwcd.fructose.Option;
import fwcd.timetable.view.plugin.PluginManagerView;
import fwcd.timetable.view.print.CalendarPrinter;
import fwcd.timetable.view.settings.SettingsView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.view.utils.RetentionFileChooser;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.TimeTableAppViewModel;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import fwcd.timetable.viewmodel.utils.FileSaveManager;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class MenuBarView implements FxView {
	private final MenuBar node;
	private final TimeTableAppContext context;
	private final TimeTableAppViewModel viewModel;
	
	private final RetentionFileChooser fileChooser = new RetentionFileChooser();
	private final FileSaveManager fileSaveManager;
	private final CalendarPrinter printer;
	
	private final SettingsView settingsView;
	private final PluginManagerView pluginManagerView;
	
	public MenuBarView(TimeTableAppContext context, TimeTableAppViewModel viewModel, TimeTableAppApi api) {
		this.context = context;
		this.viewModel = viewModel;
		
		printer = new CalendarPrinter(context);
		fileSaveManager = new FileSaveManager(
			reader -> {
				CalendarCrateViewModel crate = viewModel.getCalendars();
				crate.loadCrate(reader);
				return crate.getChangeListeners();
			},
			writer -> viewModel.getCalendars().saveCrate(writer),
			this::showSaveDialog,
			this::showOpenDialog,
			context.getFileSaveState(),
			StandardCharsets.UTF_8
		);
		settingsView = new SettingsView(context);
		pluginManagerView = new PluginManagerView(context, api);
		node = new MenuBar(
			FxUtils.menuOf(context.localized("filemenu"),
				FxUtils.menuItemOf(context.localized("new"), this::createNew, new KeyCodeCombination(KeyCode.N, FxUtils.CTRL_OR_CMD_DOWN)),
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
			),
			FxUtils.menuOf(context.localized("thememenu"),
				context.getThemeManager().getThemeKeys().stream()
					.map(key -> FxUtils.menuItemOf(context.localized(key), () -> context.setTheme(key)))
			),
			FxUtils.menuOf(context.localized("plugins"),
				FxUtils.menuItemOf(context.localized("pluginmanager"), this::showPluginManager)
			),
			FxUtils.menuOf(context.localized("debugmenu"),
				FxUtils.menuItemOf(context.localized("reloadcss"), this::reloadCss)
			)
		);
		
		if (FxUtils.isMacOS()) {
			node.setUseSystemMenuBar(true);
		}
	}
	
	private Option<Path> showOpenDialog() {
		return fileChooser.showOpenDialog(node.getScene().getWindow());
	}
	
	private Option<Path> showSaveDialog() {
		return fileChooser.showSaveDialog(node.getScene().getWindow());
	}
	
	private void reloadCss() {
		ObservableList<String> stylesheets = node.getScene().getStylesheets();
		List<String> cssPaths = new ArrayList<>(stylesheets);
		
		stylesheets.clear();
		stylesheets.addAll(cssPaths);
	}
	
	private void createNew() {
		viewModel.getCalendars().createDefaultCalendars();
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
		dialog.getDialogPane().setContent(settingsView.getNode());
		dialog.show();
	}
	
	private void showPluginManager() {
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.setTitle(context.localize("pluginmanager"));
		dialog.setHeaderText(context.localize("pluginmanager"));
		dialog.getDialogPane().setContent(pluginManagerView.getNode());
		dialog.show();
	}
	
	@Override
	public Node getNode() { return node; }
}
