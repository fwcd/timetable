package fwcd.timetable.view.sidebar.cau;

import java.util.Objects;

import fwcd.timetable.view.sidebar.cau.campus.CampusView;
import fwcd.timetable.view.sidebar.cau.univis.UnivISView;
import fwcd.timetable.view.utils.FxView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class CauView implements FxView {
	private final Accordion node;
	
	public CauView(TimeTableAppContext context, CalendarsViewModel calendars) {
		CampusView campusView = new CampusView();
		TitledPane univIS = new TitledPane("UnivIS", new UnivISView(context, calendars).getNode());
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
	public Node getNode() { return node; }
}
