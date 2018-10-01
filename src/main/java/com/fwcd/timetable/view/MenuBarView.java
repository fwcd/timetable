package com.fwcd.timetable.view;

import java.util.Arrays;
import java.util.stream.Stream;

import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarView implements FxView {
	private final MenuBar node;
	private final TimeTableAppContext context;
	
	public MenuBarView(TimeTableAppContext context) {
		this.context = context;
		node = new MenuBar(
			menuOf(context.localized("filemenu")),
			menuOf(context.localized("editmenu")),
			menuOf(context.localized("querymenu")),
			menuOf(context.localized("languagemenu"),
				context.getLanguageManager().getLanguageKeys().stream()
					.map(key -> itemOf(key, () -> context.getLanguageManager().setLanguage(key)))
			)
		);
	}
	
	@Override
	public Node getNode() { return node; }
	
	private Menu menuOf(ReadOnlyObservable<String> name, MenuItem... items) {
		return menuOf(name, Arrays.stream(items));
	}
	
	private Menu menuOf(ReadOnlyObservable<String> name, Stream<? extends MenuItem> items) {
		Menu menu = new Menu();
		name.listenAndFire(menu::setText);
		items.forEach(menu.getItems()::add);
		return menu;
	}
	
	private MenuItem itemOf(String name, Runnable action) {
		MenuItem item = new MenuItem(name);
		item.setOnAction(e -> action.run());
		return item;
	}
}
