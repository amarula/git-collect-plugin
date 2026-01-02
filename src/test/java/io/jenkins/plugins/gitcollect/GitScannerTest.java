package io.jenkins.plugins.gitcollect;

import org.eclipse.jgit.lib.ObjectId;
import org.jenkinsci.plugins.gitclient.GitClient;
import org.junit.Test;
import org.mockito.Mockito;

import hudson.plugins.git.Branch;
import hudson.plugins.git.Revision;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


import static org.junit.Assert.assertEquals;

public class GitScannerTest {

    @Test
    public void testInvokeResolvesRevisionsAndUrl() throws IOException, InterruptedException, Exception {
        GitClient git = Mockito.mock(GitClient.class);

        ObjectId resolved = ObjectId.fromString("3333333333333333333333333333333333333333");
        ObjectId head = ObjectId.fromString("4444444444444444444444444444444444444444");

        Mockito.when(git.getRemoteUrl("origin")).thenReturn("ssh://git@github.com:org/repo.git");
        Mockito.when(git.revParse("master")).thenReturn(resolved);
        Mockito.when(git.revParse("HEAD")).thenReturn(head);

        GitScanner scanner = new GitScanner(git, "master", "origin");

        File tmp = java.nio.file.Files.createTempDirectory("gitscanner-test").toFile();

        LocalGitInfo info = scanner.invoke(tmp, null);

        assertEquals("ssh://git@github.com:org/repo.git", info.getRemoteUrl());
        assertEquals(head.getName(), info.getShaRevision());
        // reference head is 'master' and the branch set on builtRevision is referenceHead
        assertEquals("master", info.getBranch());
    }
}
