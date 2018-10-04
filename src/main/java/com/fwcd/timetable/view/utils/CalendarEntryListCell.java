package com.fwcd.timetable.view.utils;

import java.util.ArrayDeque;
import java.util.Queue;

import com.fwcd.fructose.function.Subscription;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CalendarEntryListCell extends ListCell<CalendarEntryModel> {
	private final Label titleLabel = new Label();
	private final Label subtitleLabel = new Label();
	private final Label descriptionLabel = new Label();
	private final Queue<Subscription> itemSubscriptions = new ArrayDeque<>();
	
	public CalendarEntryListCell() {
		titleLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		subtitleLabel.setFont(Font.font(12));
		descriptionLabel.setFont(Font.font(12));
		
		getChildren().add(new VBox(
			titleLabel,
			subtitleLabel,
			descriptionLabel
		));
	}
	
	@Override
	protected void updateItem(CalendarEntryModel item, boolean empty) {
		super.updateItem(item, empty);
		
		while (!itemSubscriptions.isEmpty()) {
			itemSubscriptions.poll().unsubscribe();
		}
		
		CalendarEntryInfoProvider infoProvider = new CalendarEntryInfoProvider();
		
		itemSubscriptions.offer(item.getName().subscribeAndFire(titleLabel::setText));
		itemSubscriptions.offer(infoProvider.getInfo().subscribeAndFire(subtitleLabel::setText));
		itemSubscriptions.offer(item.getDescription().subscribeAndFire(descriptionLabel::setText));
		infoProvider.getSubscriptions().forEach(itemSubscriptions::offer);
	}
}
