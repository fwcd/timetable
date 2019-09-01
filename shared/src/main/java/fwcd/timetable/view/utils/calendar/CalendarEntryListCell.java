package fwcd.timetable.view.utils.calendar;

import org.controlsfx.control.PopOver;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;

public class CalendarEntryListCell extends ListCell<CalendarEntryModel> {
	private final CalendarEntryCell cell;
	
	public CalendarEntryListCell(Localizer localizer, TemporalFormatters formatters) {
		cell = new CalendarEntryCell(localizer, formatters);
		
		setGraphic(cell.getNode());
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		
		setContextMenu(new ContextMenu(
			FxUtils.menuItemOf(localizer.localize("editmenu"), () -> {
				getEntry().ifPresent(entry -> {
					CalendarEntryEditProvider editProvider = new CalendarEntryEditProvider(localizer, formatters, entry.getContainer());
					entry.getValue().accept(editProvider);
					editProvider.getView().ifPresent(view -> {
						PopOver popOver = FxUtils.newPopOver(view);
						editProvider.setOnDelete(popOver::hide);
						FxUtils.showIndependentPopOver(popOver, this);
					});
				});
			}),
			FxUtils.menuItemOf(localizer.localize("delete"), () -> {
				getEntry().ifPresent(entry -> {
					entry.getContainer().remove(entry.getValue());
				});
			})
		));
	}
	
	private Option<CalendarEntryModel> getEntry() {
		return Option.ofNullable(getItem());
	}
	
	@Override
	protected void updateItem(CalendarEntryModel item, boolean empty) {
		super.updateItem(item, empty);
		if ((item == null) || empty) {
			setGraphic(null);
		} else {
			cell.updateItem(item.getValue());
			setGraphic(cell.getNode());
		}
	}
}
