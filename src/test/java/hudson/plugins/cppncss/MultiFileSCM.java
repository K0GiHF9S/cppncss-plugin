package hudson.plugins.cppncss;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.scm.NullSCM;
import hudson.scm.SCMRevisionState;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jvnet.hudson.test.SingleFileSCM;

/**
 *
 * @author Pascal Martin
 *
 */
public class MultiFileSCM extends NullSCM
{
	private List<SingleFileSCM> files = new ArrayList<SingleFileSCM>();

	public MultiFileSCM(List<SingleFileSCM> files)
	{
		this.files = files;
	}

	@SuppressWarnings("unchecked")
	@Override
    public void checkout(Run build, Launcher launcher, FilePath workspace, TaskListener listener, File changeLogFile, SCMRevisionState baseline) throws IOException, InterruptedException {
		for (SingleFileSCM file : this.files) {
			file.checkout(build, launcher, workspace, listener, changeLogFile, baseline);
		}
    }
}
