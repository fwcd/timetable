package com.fwcd.timetable.view.git;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.git.GitRepositoryModel;
import com.fwcd.timetable.view.FxView;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * The Git plugin view class.
 */
public class GitView implements FxView {
	private final Pane node;
	
	public GitView(Observable<Option<GitRepositoryModel>> model) {
		node = new Pane();
		model.listenAndFire(repo -> repo.ifPresent(it -> node.getChildren().setAll(new Label(it.getRepositoryFolder().toString())))); // FIXME: Proof-of-concept
	}
	
	@Override
	public Node getNode() { return node; }
}
