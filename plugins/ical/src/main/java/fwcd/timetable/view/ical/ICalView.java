package fwcd.timetable.view.ical;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import fwcd.fructose.Option;
import fwcd.timetable.model.DefaultICalConverter;
import fwcd.timetable.model.ICalConverter;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.model.utils.Identified;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

public class ICalView implements FxView {
	private final Pane node;
	private final ICalConverter converter = new DefaultICalConverter();
	private final FileChooser fileChooser = new FileChooser();

	private CalendarCrateViewModel crate;
	private Localizer localizer;
	
	public ICalView() {
		node = new VBox();
		node.getStyleClass().add("padded-view");
	}
	
	public void initialize(TimeTableAppApi api) {
		localizer = api.getLocalizer();
		crate = api.getCalendarCrate();

		node.getChildren().setAll(
			FxUtils.buttonOf(localizer.localized("importics"), this::showImportDialog),
			FxUtils.buttonOf(localizer.localized("exportics"), this::showExportDialog)
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
	
	private void showExportDialog() {
		showCalendarChooserDialog().ifPresent(calendar -> {
			File file = fileChooser.showSaveDialog(null);

			if (file != null) {
				String path = file.getAbsolutePath();
				if (!path.endsWith(".ics")) {
					file = new File(path + ".ics");
				}

				try (FileOutputStream out = new FileOutputStream(file)) {
					CalendarOutputter outputter = new CalendarOutputter();
					Collection<CalendarEntryModel> entries = crate.streamEntries()
						.filter(it -> it.getCalendarId() == calendar.getId())
						.collect(Collectors.toList());
					
					outputter.output(converter.toiCalCalendar(entries), out);
				} catch (IOException e) {
					FxUtils.showExceptionAlert(localizer.localize("error"), e);
				}
			}
		});
	}
	
	private Option<Identified<CalendarModel>> showCalendarChooserDialog() {
		ComboBox<Identified<CalendarModel>> dropDown = new ComboBox<>();
		dropDown.getItems().addAll(crate.getCalendars());
		dropDown.getSelectionModel().select(0);

		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.setTitle(localizer.localize("choosecalendar"));
		dialog.setHeaderText(localizer.localize("choosecalendar"));
		dialog.getDialogPane().setContent(dropDown);

		if (dialog.showAndWait().isPresent()) {
			return Option.of(dropDown.getValue());
		} else {
			return Option.empty();
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
