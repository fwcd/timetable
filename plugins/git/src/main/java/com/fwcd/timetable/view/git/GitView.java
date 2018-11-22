package com.fwcd.timetable.view.git;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.git.GitRepositoryModel;
import com.fwcd.timetable.view.FxView;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * The Git plugin view class.
 */
public class GitView implements FxView {
	private final Pane node;
	private final Label name;
	
	public GitView(Observable<Option<GitRepositoryModel>> model) {
		name = new Label("");
		node = new VBox(
			name
		);
		
		model.listenAndFire(optionalRepo -> {
			if (optionalRepo.isPresent()) {
				GitRepositoryModel repo = optionalRepo.unwrap();
				name.setText(repo.getRepositoryFolder().getFileName().toString());
			} else {
				name.setText("No Git repository found!");
			}
		}); // FIXME: Proof-of-concept
	}
	
	@Override
	public Node getNode() { return node; }
}
