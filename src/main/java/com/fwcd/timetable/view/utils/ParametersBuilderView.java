package com.fwcd.timetable.view.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.query.Parameter;
import com.fwcd.timetable.model.query.ParameterKey;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class ParametersBuilderView implements FxView {
	private final GridPane node;
	private final List<ParameterKey> keys;
	private final List<ParametersBuilderRowView> rows = new ArrayList<>();
	private final ObservableList<ParameterKey> filteredKeys = new ObservableList<>();
	private ParametersBuilderRowView rootRow;
	
	public ParametersBuilderView(List<ParameterKey> keys) {
		this.keys = keys;
		
		node = new GridPane();
		node.setPadding(new Insets(0, 0, 4, 0));
		reset();
	}
	
	public void reset() {
		rows.clear();
		rootRow = new ParametersBuilderRowView(FxUtils.comboBoxOf(getRootKeys()));
		rootRow.getKeyProperty().addListener((s, o, n) -> maybeAddRow());
		rootRow.getValueLabelProperty().addListener((s, o, n) -> updateFilteredKeys());
		
		node.getChildren().clear();
		node.addRow(0, rootRow.getLeftNode(), rootRow.getRightNode());
	}
	
	private void addRow() {
		int index = rows.size() + 1;
		ParametersBuilderRowView row = new ParametersBuilderRowView(FxUtils.comboBoxOfObservable(filteredKeys));
		row.getKeyProperty().addListener((s, o, n) -> maybeAddRow());
		row.getValueLabelProperty().addListener((s, o, n) -> updateFilteredKeys());
		
		rows.add(row);
		node.addRow(index, row.getLeftNode(), row.getRightNode());
	}

	private void maybeAddRow() {
		if (!getLastRow().filter(it -> !it.isEdited()).isPresent()) {
			addRow();
		}
	}

	private Option<ParametersBuilderRowView> getLastRow() {
		if (rows.isEmpty()) {
			return Option.empty();
		} else {
			return Option.of(rows.get(rows.size() - 1));
		}
	}
	
	private List<ParameterKey> getRootKeys() {
		return keys.stream()
			.filter(it -> !it.isHidden())
			.collect(Collectors.toList());
	}
	
	private void updateFilteredKeys() {
		Set<String> possibleKeys = rootRow.getPossibleParameters();
		filteredKeys.set(keys.stream()
			.filter(it -> possibleKeys.contains(it.getName()))
			.collect(Collectors.toList()));
	}
	
	public ParametersBuilderRowView getRootRow() { return rootRow; }
	
	public List<Parameter> buildParameters() { return buildParameters(Stream.empty()); }
	
	public List<Parameter> buildParameters(Stream<Parameter> additionalParameters) {
		return Stream.concat(
			Stream.concat(Stream.of(rootRow), rows.stream())
				.map(ParametersBuilderRowView::buildParameter)
				.filter(Option::isPresent)
				.map(Option::unwrap),
			additionalParameters
		).collect(Collectors.toList());
	}
	
	@Override
	public Node getNode() { return node; }
}
