package fwcd.timetable.view.utils.calendar;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.utils.Contained;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppContext;

import org.controlsfx.control.PopOver;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;

public class CalendarEntryListCell extends ListCell<Contained<CalendarEntryModel>> {
	private final CalendarEntryCell cell;
	
	public CalendarEntryListCell(TimeTableAppContext context) {
		cell = new CalendarEntryCell(context);
		
		setGraphic(cell.getNode());
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		
		setContextMenu(new ContextMenu(
			FxUtils.menuItemOf(context.localized("editmenu"), () -> {
				getEntry().ifPresent(entry -> {
					CalendarEntryEditProvider editProvider = new CalendarEntryEditProvider(context, entry.getContainer());
					entry.getValue().accept(editProvider);
					editProvider.getView().ifPresent(view -> {
						PopOver popOver = FxUtils.newPopOver(view);
						editProvider.setOnDelete(popOver::hide);
						FxUtils.showIndependentPopOver(popOver, this);
					});
				});
			}),
			FxUtils.menuItemOf(context.localized("delete"), () -> {
				getEntry().ifPresent(entry -> {
					entry.getContainer().remove(entry.getValue());
				});
			})
		));
	}
	
	private Option<Contained<CalendarEntryModel>> getEntry() {
		return Option.ofNullable(getItem());
	}
	
	@Override
	protected void updateItem(Contained<CalendarEntryModel> item, boolean empty) {
		super.updateItem(item, empty);
		if ((item == null) || empty) {
			setGraphic(null);
		} else {
			cell.updateItem(item.getValue());
			setGraphic(cell.getNode());
		}
	}
}
