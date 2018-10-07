package com.fwcd.timetable.view.sidebar.utils;

import java.util.ArrayDeque;
import java.util.Queue;

import com.fwcd.fructose.function.Subscription;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CalendarEntryListCell extends ListCell<CalendarEntryModel> {
	private final Label titleLabel = new Label();
	private final Label subtitleLabel = new Label();
	private final VBox node;
	private final Queue<Subscription> itemSubscriptions = new ArrayDeque<>();
	
	public CalendarEntryListCell() {
		titleLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		subtitleLabel.setFont(Font.font(12));
		
		node = new VBox(titleLabel);
		setGraphic(node);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	}
	
	@Override
	protected void updateItem(CalendarEntryModel item, boolean empty) {
		super.updateItem(item, empty);
		
		while (!itemSubscriptions.isEmpty()) {
			itemSubscriptions.poll().unsubscribe();
		}
		
		if (item != null) {
			CalendarEntryInfoProvider infoProvider = new CalendarEntryInfoProvider();
			item.accept(infoProvider);
			
			itemSubscriptions.offer(item.getName().subscribeAndFire(name -> titleLabel.setText(titlePrefixOf(item) + name)));
			itemSubscriptions.offer(infoProvider.getInfo().subscribeAndFire(info -> {
				subtitleLabel.setText(info);
				if (info.isEmpty()) {
					node.getChildren().setAll(titleLabel);
				} else {
					node.getChildren().setAll(titleLabel, subtitleLabel);
				}
			}));
			infoProvider.getSubscriptions().forEach(itemSubscriptions::offer);
		}
	}
	
	private String titlePrefixOf(CalendarEntryModel entry) {
		return "";
	}
}
