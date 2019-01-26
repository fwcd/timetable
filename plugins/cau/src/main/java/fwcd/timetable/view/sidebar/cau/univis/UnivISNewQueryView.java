package fwcd.timetable.view.sidebar.cau.univis;

import java.util.stream.Stream;

import fwcd.fructose.EventListenerList;
import fwcd.timetable.model.query.Parameter;
import fwcd.timetable.model.query.Query;
import fwcd.timetable.model.query.cau.UnivISDepartmentNode;
import fwcd.timetable.model.query.cau.UnivISParameterKeyProvider;
import fwcd.timetable.model.query.cau.UnivISQuery;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.view.utils.ParametersBuilderView;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class UnivISNewQueryView implements FxView {
	private static final String DEPARTMENT_BY_NR_KEY = "department";
	private final Pane node;
	private final UnivISDepartmentChooserView departmentChooser;
	private final ParametersBuilderView parametersBuilder;
	
	private final EventListenerList<Query> queryListeners = new EventListenerList<>();
	
	public UnivISNewQueryView(TimeTableAppApi api) {
		departmentChooser = new UnivISDepartmentChooserView();
		parametersBuilder = new ParametersBuilderView(new UnivISParameterKeyProvider().getKeys());
		node = new VBox(
			departmentChooser.getNode(),
			parametersBuilder.getNode(),
			new HBox(
				FxUtils.buttonOf(context.localized("search"), this::performNewQuery),
				FxUtils.buttonOf(context.localized("reset"), parametersBuilder::reset)
			)
		);
		node.setPadding(new Insets(4, 4, 4, 4));
	}

	private void performNewQuery() {
		Stream.Builder<Parameter> additionalParameters = Stream.builder();
		boolean allowsNumberParameter = parametersBuilder.getRootRow()
			.getPossibleParameters()
			.stream()
			.anyMatch(it -> it.equals(DEPARTMENT_BY_NR_KEY));
		
		if (allowsNumberParameter) {
			departmentChooser.getSelected()
				.map(UnivISDepartmentNode::getOrgNumber)
				.map(orgNumber -> new Parameter(DEPARTMENT_BY_NR_KEY, orgNumber))
				.ifPresent(additionalParameters::add);
		}
		
		queryListeners.fire(new UnivISQuery(parametersBuilder.buildParameters(additionalParameters.build())));
	}
	
	public EventListenerList<Query> getQueryListeners() { return queryListeners; }
	
	@Override
	public Node getNode() { return node; }
}
