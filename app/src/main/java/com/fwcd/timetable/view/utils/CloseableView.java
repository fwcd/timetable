package com.fwcd.timetable.view.utils;

import com.fwcd.fructose.Observable;
import com.fwcd.timetable.api.view.FxView;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class CloseableView implements FxView {
	private final HBox node;
	private final Pane contentWrapper;
	private final Observable<Boolean> isClosed = new Observable<>(true);
	
	public CloseableView(FxView content) {
		this(content.getNode());
	}
	
	public CloseableView(Node content) {
		contentWrapper = new Pane();
		node = new HBox(createCloseButton(), contentWrapper);
		node.setAlignment(Pos.CENTER_LEFT);
		
		isClosed.listenAndFire(closed -> {
			if (closed) {
				contentWrapper.getChildren().clear();
			} else {
				contentWrapper.getChildren().add(content);
			}
		});
	}
	
	private Button createCloseButton() {
		Button button = new Button();
		button.setOnMouseClicked(v -> isClosed.set(!isClosed.get()));
		isClosed.listenAndFire(closed -> {
			if (closed) {
				button.setText("<");
			} else {
				button.setText(">");
			}
		});
		return button;
	}
	
	@Override
	public Node getNode() { return node; }
}
