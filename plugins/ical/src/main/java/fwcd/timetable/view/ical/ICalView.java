package fwcd.timetable.view.ical;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import fwcd.timetable.model.DefaultICalConverter;
import fwcd.timetable.model.ICalConverter;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

public class ICalView implements FxView {
	private final Pane node;
	private final ICalConverter converter = new DefaultICalConverter();
	private final FileChooser fileChooser = new FileChooser();

	private CalendarCrateViewModel crate;
	private Localizer localizer;
	
	public ICalView() {
		node = new Pane();
	}
	
	public void initialize(TimeTableAppApi api) {
		localizer = api.getLocalizer();
		crate = api.getCalendarCrate();

		node.getChildren().setAll(
			FxUtils.buttonOf(localizer.localized("importfile"), this::showImportDialog)
		);
	}
	
	private void showImportDialog() {
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			try (FileInputStream in = new FileInputStream(file)) {
				CalendarBuilder builder = new CalendarBuilder();
				Calendar iCal = builder.build(in);
				int id = crate.add(new CalendarModel(file.getName().split("\\.")[0]));
				
				for (CalendarEntryModel entry : converter.toTimeTableEntries(iCal, id)) {
					crate.add(entry);
				}
			} catch (IOException | ParserException e) {
				FxUtils.showExceptionAlert(localizer.localize("error"), e);
			}
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
