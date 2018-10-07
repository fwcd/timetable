package com.fwcd.timetable.view.sidebar.utils;

import java.util.ArrayDeque;
import java.util.Queue;

import com.fwcd.fructose.function.Subscription;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CalendarEntryCell implements FxView {
	private final Label titleLabel = new Label();
	private final Label subtitleLabel = new Label();
	private final VBox node;
	private final Queue<Subscription> itemSubscriptions = new ArrayDeque<>();
	
	public CalendarEntryCell() {
		titleLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		subtitleLabel.setFont(Font.font(12));
		
		node = new VBox(titleLabel);
	}
	
	public void updateItem(CalendarEntryModel item) {
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
	
	@Override
	public Node getNode() { return node; }
}
