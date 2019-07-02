package hudson.plugins.cppncss;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Descriptor;
import hudson.plugins.helpers.AbstractPublisherImpl;
import hudson.plugins.helpers.Ghostwriter;
import hudson.plugins.helpers.health.HealthMetric;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 08-Jan-2008 21:24:06
 */
public class CppNCSSPublisher extends AbstractPublisherImpl {

    private String reportFilenamePattern;
    private Integer functionCcnViolationThreshold = 10;
    private Integer functionNcssViolationThreshold = 100;
    private CppNCSSHealthTarget[] targets;

    @DataBoundConstructor
    public CppNCSSPublisher(String reportFilenamePattern, Integer functionCcnViolationThreshold, Integer functionNcssViolationThreshold, CppNCSSHealthTarget[] targets) {
		reportFilenamePattern.getClass();
        this.reportFilenamePattern = reportFilenamePattern;
        this.functionCcnViolationThreshold = functionCcnViolationThreshold;
        this.functionNcssViolationThreshold = functionNcssViolationThreshold;

        this.targets = targets == null ? new CppNCSSHealthTarget[0] : targets;
    }

    public String getReportFilenamePattern() {
        return reportFilenamePattern;
    }

    @DataBoundSetter
    public void setReportFilenamePattern(String reportFilenamePattern) {
        this.reportFilenamePattern = reportFilenamePattern;
    }

	public Integer getFunctionCcnViolationThreshold() {
		return functionCcnViolationThreshold;
	}

    @DataBoundSetter
    public void setFunctionCcnViolationThreshold(Integer functionCcnViolationThreshold) {
        this.functionCcnViolationThreshold = functionCcnViolationThreshold;
    }

	public Integer getFunctionNcssViolationThreshold() {
		return functionNcssViolationThreshold;
	}

    @DataBoundSetter
    public void setFunctionNcssViolationThreshold(Integer functionNcssViolationThreshold) {
        this.functionNcssViolationThreshold = functionNcssViolationThreshold;
    }

	// TODO: replace by lists
	@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Legacy code, suppressed due to the performance reasons")
	public CppNCSSHealthTarget[] getTargets() {
        return targets;
    }

    @DataBoundSetter
    public void setTargets(CppNCSSHealthTarget[] targets) {
        this.targets = targets == null ? new CppNCSSHealthTarget[0] : targets.clone();
    }

    /**
     * {@inheritDoc}
     */
    public boolean needsToRunAfterFinalized() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new CppNCSSProjectIndividualReport(project, functionCcnViolationThreshold, functionNcssViolationThreshold);
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    protected Ghostwriter newGhostwriter() {
        return new CppNCSSGhostwriter(reportFilenamePattern, functionCcnViolationThreshold, functionNcssViolationThreshold, targets);
    }

    @Symbol("publishCppNCSS")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        /**
         * {@inheritDoc}
         */
        public String getDisplayName() {
            return "Publish " + PluginImpl.DISPLAY_NAME;
        }

        static {
            ConvertUtils.register(CppNCSSHealthMetrics.CONVERTER, CppNCSSHealthMetrics.class);
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public HealthMetric[] getMetrics() {
            return CppNCSSHealthMetrics.values();
        }
    }

}
