package com.fwcd.timetable.view.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.ReadOnlyObservable;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class NavigableTabPane implements FxView {
	private final StackPane node;
	private final AnchorPane navigationPane;
	private final TabPane tabPane;
	private final Map<Tab, Node> navigatorBars = new HashMap<>();
	
	public NavigableTabPane() {
		navigationPane = new AnchorPane();
		navigationPane.setPickOnBounds(false);
		
		tabPane = new TabPane();
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, old, selectedTab) -> updateNavigation(selectedTab));
		
		node = new StackPane(tabPane, navigationPane);
	}
	
	private void updateNavigation(Tab selectedTab) {
		Node navigatorBar = navigatorBars.get(selectedTab);
		if (navigatorBar == null) {
			navigationPane.getChildren().clear();
		} else {
			navigationPane.getChildren().setAll(navigatorBar);
		}
	}
	
	public void addTab(String name, FxNavigableView navigableView) {
		addTab(name, navigableView, FxUtils::tabOf);
	}
	
	public void addTab(ReadOnlyObservable<String> unlocalized, FxNavigableView navigableView) {
		addTab(unlocalized, navigableView, FxUtils::tabOf);
	}
	
	public <T> void addTab(T name, FxNavigableView navigableView, BiFunction<? super T, ? super Node, ? extends Tab> tabFactory) {
		Tab tab = tabFactory.apply(name, navigableView.getContentNode());
		tabPane.getTabs().add(tab);
		
		Option<Node> navigator = navigableView.getNavigatorNode();
		if (navigator.isPresent()) {
			Pane navigatorBar = new Pane(navigator.unwrap());
			AnchorPane.setTopAnchor(navigatorBar, 0D);
			AnchorPane.setRightAnchor(navigatorBar, 0D);
			navigatorBars.put(tab, navigatorBar);
		}
		
		if (tabPane.getTabs().size() == 1) {
			updateNavigation(tab);
		}
	}
	
	public Tab getSelectedTab() { return tabPane.getSelectionModel().getSelectedItem(); }
	
	@Override
	public Node getNode() { return node; }
}
