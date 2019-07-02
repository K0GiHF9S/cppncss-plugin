package hudson.plugins.helpers;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;

import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * An abstract Publisher that is designed to work with a Ghostwriter.
 *
 * @author Stephen Connolly
 * @since 28-Jan-2008 22:32:46
 */
public abstract class AbstractPublisherImpl extends Recorder  implements SimpleBuildStep {

    /**
     * Creates the configured Ghostwriter.
     *
     * @return returns the configured Ghostwriter.
     */
    protected abstract Ghostwriter newGhostwriter();

    /**
     * {@inheritDoc}
     */
    public void perform(@Nonnull Run<?, ?> build, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull final TaskListener listener)
            throws InterruptedException, IOException {
        BuildProxy.doPerform(newGhostwriter(), build, workspace, listener);
    }

    /**
     * {@inheritDoc}
     */
    public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
        return true;
    }
}
