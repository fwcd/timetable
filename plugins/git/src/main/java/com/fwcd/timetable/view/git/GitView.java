package com.fwcd.timetable.view.git;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.git.GitRepositoryModel;
import com.fwcd.timetable.model.utils.SubscriptionStack;
import com.fwcd.timetable.view.FxView;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * The Git plugin view class.
 */
public class GitView implements FxView, AutoCloseable {
	private final VBox node;
	private final Label name;
	private final ListView<String> commitsView;
	private final SubscriptionStack subscriptions = new SubscriptionStack();
	
	public GitView(Observable<Option<GitRepositoryModel>> model) {
		name = new Label("");
		name.setFont(Font.font(14));
		
		commitsView = new ListView<>();
		
		node = new VBox(
			name,
			commitsView
		);
		node.getStyleClass().add("padded-view");
		
		model.listenAndFire(optionalRepo -> {
			subscriptions.unsubscribeAll();
			if (optionalRepo.isPresent()) {
				GitRepositoryModel repo = optionalRepo.unwrap();
				subscriptions.push(repo.getCommits().subscribeAndFire(commitsView.getItems()::setAll));
				name.setText(repo.getRepositoryFolder().getFileName().toString());
			} else {
				name.setText("No Git repository found!");
			}
		});
	}
	
	@Override
	public Node getNode() { return node; }
	
	@Override
	public void close() {
		subscriptions.unsubscribeAll();
	}
}
