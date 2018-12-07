package fwcd.timetable.view.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import fwcd.fructose.Option;
import fwcd.timetable.model.query.Parameter;
import fwcd.timetable.model.query.ParameterKey;
import fwcd.timetable.model.query.ParameterValue;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class ParametersBuilderRowView implements FxDualView {
	private final ComboBox<ParameterKey> keyBox;
	private final ComboBox<String> valueBox;
	private boolean edited = false;
	
	public ParametersBuilderRowView(ComboBox<ParameterKey> keyBox) {
		this.keyBox = keyBox;
		valueBox = new ComboBox<>();
		
		keyBox.setEditable(false);
		keyBox.setMinWidth(80);
		keyBox.valueProperty().addListener((obs, old, selectedKey) -> {
			valueBox.getItems().setAll(
				selectedKey.getValues().stream()
					.map(ParameterValue::getLabel)
					.collect(Collectors.toList())
			);
			edited = true;
		});
		valueBox.setEditable(true);
		valueBox.setMinWidth(80);
		valueBox.getEditor().textProperty().addListener((obs, old, n) -> {
			edited = true;
		});
	}
	
	public Option<ParameterKey> getKey() { return Option.ofNullable(keyBox.getValue()); }
	
	public Option<ParameterValue> getValue() {
		String label = valueBox.valueProperty().get();
		return getKey().flatMap(key -> Option.of(key.getValues().stream()
			.filter(it -> it.getLabel().equals(label))
			.findAny()));
	}
	
	public Set<String> getPossibleParameters() {
		return getValue()
			.map(ParameterValue::getPossibleParameters)
			.<Set<String>>map(HashSet::new)
			.orElse(Collections.emptySet());
	}
	
	public Property<ParameterKey> getKeyProperty() {
		return keyBox.valueProperty();
	}
	
	public Property<String> getValueLabelProperty() {
		return valueBox.valueProperty();
	}
	
	public Option<Parameter> buildParameter() {
		return getKey()
			.map(ParameterKey::getName)
			.flatMap(key -> getValue()
				.map(v -> new Parameter(key, v.getValueString()))
				.or(() -> Option.ofNullable(valueBox.getValue()).filter(it -> !it.isEmpty()).map(v -> new Parameter(key, v))));
	}
	
	public boolean isEdited() { return edited; }
	
	@Override
	public Node getLeftNode() { return keyBox; }
	
	@Override
	public Node getRightNode() { return valueBox; }
}
