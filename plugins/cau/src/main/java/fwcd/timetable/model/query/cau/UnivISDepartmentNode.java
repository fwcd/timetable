package fwcd.timetable.model.query.cau;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fwcd.fructose.Option;

/**
 * A department tree node with an immutable description
 * (name/orgNumber) and mutable child nodes.
 */
public class UnivISDepartmentNode {
	private final String name;
	private final String orgNumber;
	private final Map<String, UnivISDepartmentNode> subDepartments = new HashMap<>();
	
	public UnivISDepartmentNode(String name, String orgNumber) {
		this.name = name;
		this.orgNumber = orgNumber;
	}
	
	public String getName() { return name; }
	
	public String getOrgNumber() { return orgNumber; }
	
	public boolean isLeaf() { return subDepartments.isEmpty(); }
	
	public void addSubDepartment(UnivISDepartmentNode department) {
		subDepartments.put(department.getName(), department);
	}
	
	public Option<UnivISDepartmentNode> getSubDepartmentByName(String name) {
		return Option.ofNullable(subDepartments.get(name));
	}
	
	public UnivISDepartmentNode getOrAddSubDepartmentByName(String name, String orgNumber) {
		UnivISDepartmentNode subDepartment = subDepartments.get(name);
		if (subDepartment == null) {
			subDepartment = new UnivISDepartmentNode(name, orgNumber);
			subDepartments.put(name, subDepartment);
		}
		return subDepartment;
	}
	
	public Collection<UnivISDepartmentNode> getSubDepartments() { return subDepartments.values(); }
	
	@Override
	public String toString() {
		return name;
	}
	
	public String toStringWithChilds() {
		StringBuilder builder = new StringBuilder();
		toStringWithChilds(builder, "", "  ", System.lineSeparator());
		return builder.toString();
	}
	
	private void toStringWithChilds(StringBuilder builder, String indent, String indentDelta, String newline) {
		builder.append(indent).append(name).append(newline);
		
		for (UnivISDepartmentNode child : subDepartments.values()) {
			child.toStringWithChilds(builder, indent + indentDelta, indentDelta, newline);
		}
	}
}
