package fwcd.timetable.view.utils.calendar;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.query.QueryOutputNode;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TreeCell;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class QueryOutputTreeCell extends TreeCell<QueryOutputNode> {
	private final CalendarEntryCell cell;
	private boolean isBold = false;
	
	public QueryOutputTreeCell(Localizer localizer, TemporalFormatters formatters) {
		cell = new CalendarEntryCell(localizer, formatters);
	}
	
	@Override
	protected void updateItem(QueryOutputNode item, boolean empty) {
		super.updateItem(item, empty);
		
		if ((item == null) || empty) {
			setText("");
			setGraphic(null);
		} else {
			Option<AppointmentModel> appointment = item.asAppointment();
			if (appointment.isPresent()) {
				cell.setShowTitle(item.exposesAppointmentTitle());
				cell.updateItem(appointment.unwrap());
		
				setText("");
				setGraphic(cell.getNode());
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			} else {
				setBold(item.isHighlighted());
				setText(item.getName());
				setGraphic(null);
				setContentDisplay(ContentDisplay.TEXT_ONLY);
			}
		}
	}
	
	private void setBold(boolean bold) {
		if (bold != isBold) {
			double fontSize = getFont().getSize();
			setFont(Font.font(null, bold ? FontWeight.BOLD : FontWeight.NORMAL, fontSize));
		}
		isBold = bold;
	}
}
