package fwcd.timetable.view.sidebar.cau.univis;

import java.util.List;
import java.util.stream.Collectors;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.model.query.Query;
import fwcd.timetable.model.query.QueryOutputNode;
import fwcd.timetable.model.query.QueryResult;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.view.utils.calendar.QueryOutputTreeView;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

public class UnivISQueryOutputView implements FxView {
	private final BorderPane node;
	private final ProgressBar progressBar;
	private final WebView webView;
	private final QueryOutputTreeView treeView;
	
	private final CalendarCrateViewModel crate;
	private Option<List<AppointmentModel>> currentResults = Option.empty();
	
	public UnivISQueryOutputView(Localizer localizer, TemporalFormatters formatters, CalendarCrateViewModel crate) {
		this.crate = crate;
		
		node = new BorderPane();
		progressBar = new ProgressBar();
		webView = new WebView();
		treeView = new QueryOutputTreeView(localizer, formatters);
		
		progressBar.setProgress(0D);
		progressBar.setMaxWidth(Double.MAX_VALUE);
		
		node.setBottom(new HBox(FxUtils.buttonOf(localizer.localized("addtocalendars"), this::addToCalendars)));
	}

	public void perform(Query query) {
		progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
		node.setTop(progressBar);
		
		query.performAsync()
			.useProgress(progress -> progress.listen(percent -> Platform.runLater(() -> progressBar.setProgress(percent))))
			.thenAccept((result, progress) -> {
				Platform.runLater(() -> {
					if (result.isSuccess()) {
						QueryResult queryResult = result.unwrap();
						Option<String> raw = queryResult.getRaw();
						if (raw.isPresent()) {
							webView.getEngine().loadContent(raw.unwrap());
							node.setCenter(new ScrollPane(webView));
						} else {
							QueryOutputNode outputTree = queryResult.getOutputTree();
							currentResults = Option.of(outputTree.streamAppointments().collect(Collectors.toList()));
							
							treeView.setRoot(outputTree);
							FxUtils.expandSingleNodes(treeView.getNode());
							node.setCenter(treeView.getNode());
						}
					} else { // result failed
						FxUtils.showExceptionAlert("Fehler", result.unwrapFailure());
					}
					// Remove progress bar
					node.setTop(null);
				});
			});
	}
	
	private void addToCalendars() {
		currentResults.ifPresent(results -> {
			int calendarId = crate.add(new CalendarModel("UnivIS Query"));
			for (AppointmentModel appointment : results) {
				crate.add(appointment.with().calendarId(calendarId).build());
			}
		});
	}
	
	@Override
	public Node getNode() { return node; }
}
