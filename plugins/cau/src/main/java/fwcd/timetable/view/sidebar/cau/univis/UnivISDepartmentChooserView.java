package fwcd.timetable.view.sidebar.cau.univis;

import java.io.IOException;

import fwcd.fructose.Option;
import fwcd.timetable.model.query.cau.UnivISDepartmentNode;
import fwcd.timetable.model.query.cau.UnivISDepartmentProvider;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class UnivISDepartmentChooserView implements FxView {
	private final TreeView<UnivISDepartmentNode> node;
	private final UnivISDepartmentProvider provider = new UnivISDepartmentProvider();
	
	public UnivISDepartmentChooserView() {
		node = new TreeView<>(toFxTree(queryDepartments()));
		FxUtils.expandSingleNodes(node);
	}
	
	private TreeItem<UnivISDepartmentNode> toFxTree(UnivISDepartmentNode node) {
		TreeItem<UnivISDepartmentNode> item = new TreeItem<>(node);
		node.getSubDepartments().stream()
			.map(this::toFxTree)
			.forEach(item.getChildren()::add);
		return item;
	}
	
	public Option<UnivISDepartmentNode> getSelected() {
		// TODO: Support multi-item selections
		return Option.ofNullable(node.getSelectionModel())
			.flatMap(it -> Option.ofNullable(it.getSelectedItem()))
			.flatMap(it -> Option.ofNullable(it.getValue()));
	}
	
	private UnivISDepartmentNode queryDepartments() {
		try {
			return provider.getDepartments();
		} catch (IOException e) {
			// TODO: Display error alert
			return new UnivISDepartmentNode("Abteilungen", "");
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
