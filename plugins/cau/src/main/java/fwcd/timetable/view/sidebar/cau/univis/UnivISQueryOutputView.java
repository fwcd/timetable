package fwcd.timetable.view.sidebar.cau.univis;

import java.util.stream.Collectors;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarCrateModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.model.query.Query;
import fwcd.timetable.model.query.QueryOutputNode;
import fwcd.timetable.model.query.QueryResult;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.view.utils.FxView;
import fwcd.timetable.view.utils.calendar.QueryOutputTreeView;
import fwcd.timetable.viewmodel.TimeTableAppContext;

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
	
	private final CalendarCrateModel calendars;
	private Option<CalendarModel> currentCalendar = Option.empty();
	
	public UnivISQueryOutputView(TimeTableAppContext context, CalendarCrateModel calendars) {
		this.calendars = calendars;
		
		node = new BorderPane();
		progressBar = new ProgressBar();
		webView = new WebView();
		treeView = new QueryOutputTreeView(context);
		
		progressBar.setProgress(0D);
		progressBar.setMaxWidth(Double.MAX_VALUE);
		
		node.setBottom(new HBox(FxUtils.buttonOf(context.localized("addtocalendars"), this::addToCalendars)));
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
							CalendarModel calendar = new CalendarModel("UnivIS Query");
							currentCalendar = Option.of(calendar);
							
							QueryOutputNode outputTree = queryResult.getOutputTree();
							calendar.getAppointments()
								.set(outputTree.streamAppointments().collect(Collectors.toList()));
							
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
		currentCalendar.ifPresent(calendars.getCalendars()::add);
	}
	
	@Override
	public Node getNode() { return node; }
}
