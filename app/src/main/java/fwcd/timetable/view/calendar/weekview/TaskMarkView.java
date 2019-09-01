package fwcd.timetable.view.calendar.weekview;

import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.view.FxView;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class TaskMarkView implements FxView {
	private final StackPane node;
	
	public TaskMarkView(TaskModel model) {
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
		
		Text text = new Text();
		text.setText(model.getName());
		text.getStyleClass().add("task-mark-view");
		text.setManaged(false);
		text.setLayoutX(0);
		text.setLayoutY(0);
		childs.add(text);
	}
	
	@Override
	public Pane getNode() { return node; }
}
