package fwcd.timetable.model.query;

import java.util.Collection;
import java.util.Collections;

import fwcd.fructose.Option;

/**
 * A tree-structured parameter value wrapper.
 */
public class ParameterValueNode {
	private final Option<ParameterValue> value;
	private final Collection<ParameterValueNode> childs;
	
	public ParameterValueNode(ParameterValue value) {
		this.value = Option.of(value);
		childs = Collections.emptyList();
	}
	
	public ParameterValueNode(Collection<ParameterValueNode> childs) {
		this.childs = childs;
		value = Option.empty();
	}
	
	public boolean isLeaf() { return childs.isEmpty(); }
	
	public Option<ParameterValue> getValue() { return value; }
	
	public Collection<ParameterValueNode> getChilds() { return childs; }
}
