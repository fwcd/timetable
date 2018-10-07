package com.fwcd.timetable;

import com.fwcd.timetable.view.TimeTableAppView;
import com.fwcd.timetable.view.utils.FxParentView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TimeTableMain extends Application {
	private static final String TITLE = "TimeTable";
	private static final String VERSION = "0.1";
	
	@Override
	public void start(Stage stage) {
		FxParentView appView = new TimeTableAppView();
		Scene scene = new Scene(appView.getNode(), 800, 500);
		
		stage.setTitle(TITLE + " " + VERSION);
		stage.setScene(scene);
		scene.getStylesheets().add("/css/styles.css");
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
