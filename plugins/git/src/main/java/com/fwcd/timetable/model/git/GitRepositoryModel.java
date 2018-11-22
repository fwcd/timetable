package com.fwcd.timetable.model.git;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import com.fwcd.fructose.Option;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * An abstract representation of a git repository.
 */
public class GitRepositoryModel {
	private final Repository repository;
	
	private GitRepositoryModel(Repository repository) {
		this.repository = repository;
	}
	
	/**
	 * Creates a GitRepositoryModel from a file in the repository.
	 */
	public static Option<GitRepositoryModel> ofFileInRepo(Path filePath) {
		FileRepositoryBuilder builder = new FileRepositoryBuilder().findGitDir(filePath.toFile());
		if (builder.getGitDir() == null) {
			return Option.empty();
		} else {
			try {
				return Option.of(new GitRepositoryModel(builder.build()));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}
	
	public Path getRepositoryFolder() { return repository.getWorkTree().toPath(); }
}