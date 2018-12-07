package fwcd.timetable.view.plugin;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import fwcd.timetable.plugin.TimeTableAppPlugin;
import fwcd.timetable.view.FxView;
import fwcd.timetable.model.utils.IOUtils;
import fwcd.timetable.plugin.JarPluginBundle;
import fwcd.timetable.plugin.PluginManager;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import fwcd.timetable.viewmodel.TimeTableAppContext;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class PluginManagerView implements FxView {
	private final BorderPane node;
	private final FileChooser fileChooser = new FileChooser();
	private final PluginManager model;
	private final TimeTableAppApi api;
	
	public PluginManagerView(TimeTableAppContext context, TimeTableAppApi api) {
		this.api = api;
		
		model = context.getPluginManager();
		node = new BorderPane();
		
		Pane top = new HBox(
			FxUtils.buttonOf(context.localized("addplugins"), this::showAddPluginsDialog)
		);
		node.setTop(top);
		
		ListView<TimeTableAppPlugin> plugins = new ListView<>();
		plugins.setCellFactory(it -> new ListCell<TimeTableAppPlugin>() {
			@Override
			protected void updateItem(TimeTableAppPlugin item, boolean empty) {
				super.updateItem(item, empty);
				if ((item != null) && !empty) {
					setText(item.getName());
				} else {
					setText("");
				}
			}
		});
		model.getLoadedPlugins().listenAndFire(plugins.getItems()::setAll);
		node.setCenter(plugins);
	}
	
	private void showAddPluginsDialog() {
		List<File> files = fileChooser.showOpenMultipleDialog(node.getScene().getWindow());
		if ((files != null) && (files.size() > 0)) {
			model.add(new JarPluginBundle(files.stream()
				.map(File::toURI)
				.map(IOUtils.wrap(URI::toURL))
				.toArray(URL[]::new)), api);
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
