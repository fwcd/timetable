package fwcd.timetable.model.query.cau;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.query.QueryOutputNode;

public abstract class UnivISQueryOutputNode implements QueryOutputNode {
	private final String name;
	private final boolean exposesTitle;
	private final boolean highlighted;
	private final Option<AppointmentModel> appointment;
	
	protected UnivISQueryOutputNode(String name, boolean highlighted, boolean exposesTitle, Option<AppointmentModel> appointment) {
		this.name = name;
		this.appointment = appointment;
		this.highlighted = highlighted;
		this.exposesTitle = exposesTitle;
	}

	@Override
	public boolean isHighlighted() { return highlighted; }
	
	@Override
	public boolean exposesAppointmentTitle() { return exposesTitle; }
	
	@Override
	public String getName() { return name; }
	
	@Override
	public Option<AppointmentModel> asAppointment() { return appointment; }
	
	/** A node holding non-leaf childs. */
	public static class Parent extends UnivISQueryOutputNode {
		private final Map<String, Parent> childParents = new HashMap<>();
		private final List<UnivISQueryOutputNode> otherChilds = new ArrayList<>();
		
		public Parent(String name) { this(name, /* highlighted */ false); }
		
		public Parent(String name, boolean highlighted) { super(name, highlighted, /* exposesTitle */ true, Option.empty()); }
		
		public void addChildParent(Parent childParent) { childParents.put(childParent.getName(), childParent); }
		
		public void addChild(UnivISQueryOutputNode otherChild) { otherChilds.add(otherChild); }
	
		public void addChildIfNotEmpty(Parent childParent) {
			if (!childParent.getChilds().isEmpty()) {
				addChildParent(childParent);
			}
		}
		
		public void addChildsIfNotEmpty(Parent... addedChildParents) {
			for (Parent child : addedChildParents) {
				addChildIfNotEmpty(child);
			}
		}
		
		public Parent getOrAddChildParent(String name, boolean highlighted) {
			Parent child = childParents.get(name);
			if (child == null) {
				child = new Parent(name, highlighted);
				addChildParent(child);
			}
			return child;
		}
		
		public boolean hasNoChildren() { return otherChilds.isEmpty() && childParents.isEmpty(); }
		
		@Override
		public Collection<? extends QueryOutputNode> getChilds() {
			if (otherChilds.isEmpty()) return childParents.values();
			if (childParents.isEmpty()) return otherChilds;
			return Stream.concat(childParents.values().stream(), otherChilds.stream())
				.collect(Collectors.toSet());
		}
	}
	
	/** A pre-leaf node that holds leaf nodes (with appointments). */
	public static class PreLeaf extends UnivISQueryOutputNode {
		private final Collection<? extends Leaf> childs;
		
		public PreLeaf(String name, Collection<? extends Leaf> childs) {
			super(name, /* highlighted */ true, /* exposesTitle */ true, Option.empty());
			this.childs = childs;
		}
		
		@Override
		public Collection<? extends QueryOutputNode> getChilds() { return childs; }
	}
	
	/** A leaf node that holds an appointment. */
	public static class Leaf extends UnivISQueryOutputNode {
		public Leaf(AppointmentModel appointment) { this(appointment, /* exposesTitle */ true); }
		
		public Leaf(AppointmentModel appointment, boolean exposesTitle) { super(appointment.getName().get(), /* highlighted */ false, exposesTitle, Option.of(appointment)); }
		
		@Override
		public Collection<? extends QueryOutputNode> getChilds() { return Collections.emptySet(); }
	}
	
	/** A node without childs that has no appointment. */
	public static class Empty extends UnivISQueryOutputNode {
		public Empty(String name) { super(name, false, false, Option.empty()); }
		
		@Override
		public Collection<? extends QueryOutputNode> getChilds() { return Collections.emptySet(); }
	}
}
