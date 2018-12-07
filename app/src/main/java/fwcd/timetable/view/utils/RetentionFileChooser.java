package fwcd.timetable.view.utils;

import java.io.File;
import java.nio.file.Path;

import fwcd.fructose.Option;

import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * A {@link FileChooser} that remembers the last directory
 * and uses {@link Path} instead of {@link File}.
 */
public class RetentionFileChooser {
	private final FileChooser fileChooser = new FileChooser();
	
	public Option<Path> showOpenDialog(Window window) {
		return Option.ofNullable(fileChooser.showOpenDialog(window))
			.peek(this::updateDirectory)
			.map(File::toPath);
	}
	
	public Option<Path> showSaveDialog(Window window) {
		return Option.ofNullable(fileChooser.showSaveDialog(window))
			.peek(this::updateDirectory)
			.map(File::toPath);
	}
	
	private void updateDirectory(File file) {
		fileChooser.setInitialDirectory(file.getParentFile());
	}
}
