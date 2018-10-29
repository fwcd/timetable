package com.fwcd.timetable.view.utils;

import com.fwcd.fructose.Observable;
import com.fwcd.timetable.api.view.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class HideableView implements FxView {
	private final Pane node;
	private Observable<Boolean> isShown = new Observable<>(false);
	
	public HideableView(FxView content) {
		this(content.getNode());
	}
	
	public HideableView(Node content) {
		node = new Pane();
		isShown.listenAndFire(shown -> {
			if (shown) {
				node.getChildren().setAll(content);
			} else {
				node.getChildren().clear();
			}
		});
	}
	
	public Observable<Boolean> isHidden() { return isShown; }
	
	public void show() { isShown.set(true); }
	
	public void hide() { isShown.set(false); }
	
	public void toggle() { isShown.set(!isShown.get()); } 
	
	@Override
	public Node getNode() { return node; }
}
