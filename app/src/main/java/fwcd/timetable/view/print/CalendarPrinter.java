package fwcd.timetable.view.print;

import java.util.Set;

import fwcd.timetable.view.calendar.weekview.WeekView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;

public class CalendarPrinter {
	private final TimeTableAppContext context;
	
	public CalendarPrinter(TimeTableAppContext context) {
		this.context = context;
	}
	
	public void showPrintDialog(Window window, CalendarsViewModel calendars) {
		// TODO: Ability to choose a different printer
		PrinterJob job = createJob();
		
		if (job == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(context.localize("warning"));
			alert.setContentText(context.localize("noprinterfound"));
			alert.show();
		} else {
			if (job.showPrintDialog(window)) {
				job.printPage(new WeekView(context, calendars).getContentNode());
			}
		}
	}
	
	private PrinterJob createJob() {
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job == null) {
			Set<Printer> printers = Printer.getAllPrinters();
			if (printers.isEmpty()) {
				return null;
			} else {
				return PrinterJob.createPrinterJob(printers.iterator().next());
			}
		}
		return job;
	}
}