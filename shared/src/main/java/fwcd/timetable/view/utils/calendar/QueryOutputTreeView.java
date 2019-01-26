package fwcd.timetable.view.utils.calendar;

import fwcd.timetable.model.query.QueryOutputNode;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class QueryOutputTreeView implements FxView {
	private final TreeView<QueryOutputNode> node;
	
	public QueryOutputTreeView(Localizer localizer, TemporalFormatters formatters) {
		node = new TreeView<>();
		node.setShowRoot(false);
		node.setCellFactory(tree -> new QueryOutputTreeCell(localizer, formatters));
	}
	
	public void setRoot(QueryOutputNode root) {
		node.setRoot(toFxTree(root));
	}
	
	private TreeItem<QueryOutputNode> toFxTree(QueryOutputNode treeNode) {
		TreeItem<QueryOutputNode> treeItem = new TreeItem<>(treeNode);
		treeNode.getChilds().stream()
			.map(this::toFxTree)
			.forEach(treeItem.getChildren()::add);
		return treeItem;
	}
	
	@Override
	public Node getNode() { return node; }
}
