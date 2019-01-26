package fwcd.timetable.view.sidebar.cau;

import java.util.Objects;

import fwcd.timetable.view.FxView;
import fwcd.timetable.view.sidebar.cau.campus.CampusView;
import fwcd.timetable.view.sidebar.cau.univis.UnivISView;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class CauView implements FxView {
	private Accordion node;
	
	public void initialize(TimeTableAppApi api) {
		CampusView campusView = new CampusView();
		TitledPane univIS = new TitledPane("UnivIS", new UnivISView(api).getNode());
		TitledPane campus = new TitledPane("Campus", campusView.getNode());
		
		node = new Accordion(
			univIS,
			campus
		);
		node.setExpandedPane(univIS);
		node.expandedPaneProperty().addListener((obs, old, newV) -> {
			if (Objects.equals(newV, campus)) {
				// Lazily loads the campus map
				campusView.load();
			}
		});
	}
	
	@Override
	public Node getNode() { return Objects.requireNonNull(node, "CauView.node has not been initialized"); }
}
