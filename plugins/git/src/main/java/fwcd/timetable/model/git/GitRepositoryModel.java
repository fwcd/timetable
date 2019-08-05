package fwcd.timetable.model.git;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import fwcd.fructose.Option;
import fwcd.fructose.util.StreamUtils;
import fwcd.fructose.exception.Rethrow;
import fwcd.fructose.structs.ObservableList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * An abstract representation of a git repository.
 */
public class GitRepositoryModel implements AutoCloseable {
	private final Repository repository;
	private final Git git;
	private final ObservableList<String> commits = new ObservableList<>();
	
	private GitRepositoryModel(Repository repository) {
		this.repository = repository;
		git = new Git(repository);
		
		// TODO: Auto-update commits
		updateCommits();
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
	
	private void updateCommits() {
		try {
			List<String> log = StreamUtils.stream(git.log().call())
				.map(RevCommit::getShortMessage)
				.collect(Collectors.toList());
			commits.set(log);
		} catch (GitAPIException e) {
			throw new Rethrow(e);
		}
	}
	
	public ObservableList<String> getCommits() { return commits; }
	
	public Path getRepositoryFolder() { return repository.getWorkTree().toPath(); }
	
	@Override
	public void close() {
		git.close();
		repository.close();
	}
}
