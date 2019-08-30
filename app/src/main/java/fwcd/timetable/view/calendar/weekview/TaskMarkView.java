package fwcd.timetable.view.calendar.weekview;

import java.util.function.Consumer;

import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.calendar.task.TaskViewModel;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class TaskMarkView implements FxView, AutoCloseable {
	private final StackPane node;
	
	private final Consumer<TaskViewModel> viewModelListener;
	
	public TaskMarkView(TaskViewModel viewModel) {
		node = new StackPane();
		
		ObservableList<Node> childs = node.getChildren();
		
		Line line = new Line();
		line.getStyleClass().add("task-mark-view");
		line.setManaged(false);
		line.setStartX(0);
		line.setStartY(0);
		line.setEndY(0);
		line.endXProperty().bind(node.widthProperty());
		childs.add(line);
		
		Text text = new Text("Test");
		text.getStyleClass().add("task-mark-view");
		text.setManaged(false);
		text.setLayoutX(0);
		text.setLayoutY(0);
		childs.add(text);
		
		// Setup view model listener
		
		viewModelListener = vm -> {
			TaskModel model = vm.getModel();
			text.setText(model.getName());
		};
		viewModelListener.accept(viewModel);
		viewModel.getChangeListeners().addWeakListener(viewModelListener);
	}
	
	@Override
	public Pane getNode() { return node; }

	@Override
	public void close() {
		// Listeners are automatically cleaned up since they are weak
	}
}
