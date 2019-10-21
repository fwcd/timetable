package fwcd.timetable.view.print;

import java.util.Set;

import fwcd.timetable.view.calendar.weekview.WeekView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

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
	
	public void showPrintDialog(Window window, CalendarCrateViewModel calendars) {
		PrinterJob job = PrinterJob.createPrinterJob();
		
		if (job == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(context.localize("warning"));
			alert.setContentText(context.localize("noprinterfound"));
			alert.show();
		} else {
			if (job.showPrintDialog(window)) {
				if (!job.printPage(new WeekView(context, calendars).getContentNode())) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle(context.localize("error"));
					alert.setContentText(context.localize("printingfailed"));
					alert.show();
				}
			}
		}
	}
}
