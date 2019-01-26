package fwcd.timetable;

import fwcd.timetable.model.utils.ObservableUtils;
import fwcd.timetable.plugin.PluginManager;
import fwcd.timetable.plugin.git.GitPlugin;
import fwcd.timetable.plugin.timer.TimerPlugin;
import fwcd.timetable.view.TimeTableAppView;
import fwcd.timetable.view.utils.FxParentView;
import fwcd.timetable.viewmodel.TimeTableApiBackend;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.TimeTableAppViewModel;
import fwcd.timetable.viewmodel.utils.FileSaveState;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TimeTableMain extends Application {
	private static final String NAME = "TimeTable";
	private static final String VERSION = "0.1";
	private static final String BASE_TITLE = NAME + " " + VERSION;
	
	private final TimeTableAppContext context = new TimeTableAppContext();
	private final TimeTableAppViewModel viewModel = new TimeTableAppViewModel();
	private final TimeTableApiBackend api = new TimeTableApiBackend();
	
	public TimeTableMain() {
		wireApi();
		loadDefaultPlugins();
	}
	
	@Override
	public void start(Stage stage) {
		FxParentView appView = new TimeTableAppView(context, viewModel, api);
		Scene scene = new Scene(appView.getNode(), 800, 500);
		FileSaveState state = context.getFileSaveState();
		
		ObservableUtils.dualListenAndFire(state.getCurrentFileName(), state.hasUnsavedData(), (fileName, unsaved) -> {
			StringBuilder title = new StringBuilder(BASE_TITLE);
			
			fileName.ifPresent(it -> title.append(" - ").append(it));
			if (unsaved) {
				title.append('*');
			}
			
			stage.setTitle(title.toString());
		});
		
		context.getTheme().listenAndFire(it -> scene.getStylesheets().setAll("/css/styles.css", it.getCssResourcePath()));
		
		stage.setScene(scene);
		stage.show();
	}
	
	private void loadDefaultPlugins() {
		PluginManager pluginManager = context.getPluginManager();
		pluginManager.add(new GitPlugin(), api);
		pluginManager.add(new TimerPlugin(), api);
	}
	
	private void wireApi() {
		api.setCurrentPath(context.getFileSaveState().getCurrentPath());
		api.setCalendarCrate(viewModel.getCalendars().getModel());
		api.setLocalizer(context.getLocalizer());
		api.setFormatters(context.getFormatters());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
