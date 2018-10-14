package com.fwcd.timetable.view.utils.calendar;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.CalendarSerializationUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.view.utils.SubscriptionStack;
import com.google.gson.Gson;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CalendarEntryCell implements FxView {
	private static final Gson GSON = CalendarSerializationUtils.newGson();
	
	private final Label titleLabel = new Label();
	private final Label subtitleLabel = new Label();
	private final VBox node;
	
	private final SubscriptionStack itemSubscriptions = new SubscriptionStack();
	private Option<CalendarEntryModel> currentItem = Option.empty();
	
	public CalendarEntryCell() {
		titleLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		subtitleLabel.setFont(Font.font(12));
		
		node = new VBox(titleLabel);
		node.setOnDragDetected(e -> {
			if (currentItem.isPresent()) {
				Dragboard dragboard = node.startDragAndDrop(TransferMode.COPY);
				ClipboardContent content = new ClipboardContent();
				content.putString(GSON.toJson(currentItem.unwrap()));
				dragboard.setContent(content);
			}
		});
	}
	
	public void updateItem(CalendarEntryModel item) {
		currentItem = Option.of(item);
		itemSubscriptions.unsubscribeAll();
		
		if (item != null) {
			CalendarEntryInfoProvider infoProvider = new CalendarEntryInfoProvider();
			item.accept(infoProvider);
			
			itemSubscriptions.push(item.getName().subscribeAndFire(name -> titleLabel.setText(titlePrefixOf(item) + name)));
			itemSubscriptions.push(infoProvider.getInfo().subscribeAndFire(info -> {
				subtitleLabel.setText(info);
				if (info.isEmpty()) {
					node.getChildren().setAll(titleLabel);
				} else {
					node.getChildren().setAll(titleLabel, subtitleLabel);
				}
			}));
			itemSubscriptions.moveAll(infoProvider.getSubscriptions());
		}
	}
	
	private String titlePrefixOf(CalendarEntryModel entry) {
		return "";
	}
	
	@Override
	public Node getNode() { return node; }
}
