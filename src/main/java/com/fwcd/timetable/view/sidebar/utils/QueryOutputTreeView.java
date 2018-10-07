package com.fwcd.timetable.view.sidebar.utils;

import com.fwcd.timetable.model.query.QueryOutputNode;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class QueryOutputTreeView implements FxView {
	private final TreeView<QueryOutputNode> node;
	
	public QueryOutputTreeView() {
		node = new TreeView<>();
		node.setShowRoot(false);
		node.setCellFactory(tree -> new QueryOutputTreeCell());
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
