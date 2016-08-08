package lv.ctco.zephyr.mojo;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.ZephyrSyncService;
import lv.ctco.zephyr.enums.ConfigProperty;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

@Mojo(
        name = "sync"
)
public class ZephyrSyncMojo extends AbstractMojo implements Config.Loader{

    @Parameter(required = true)
    String username;
    @Parameter(required = true)
    String password;
    @Parameter
    String reportType;
    @Parameter(required = true)
    String projectKey;
    @Parameter(required = true)
    String releaseVersion;
    @Parameter
    String testCycle;
    @Parameter(required = true)
    String jiraUrl;
    @Parameter(required = true)
    String reportPath;
    @Parameter
    Boolean orderedSteps;
    @Parameter
    Boolean forceStoryLink;
    @Parameter
    String testCaseUniqueIdAttribute;
    @Parameter
    String severityAttribute;



    public void execute() throws MojoExecutionException, MojoFailureException {
        Config config = new Config(this);

        ZephyrSyncService syncService = new ZephyrSyncService(config);
        try {
            syncService.execute();
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot sync test results into zephyr", e);
        }
    }

    public void execute(Config config) {
        config.setProperty(ConfigProperty.USERNAME, username);
        config.setProperty(ConfigProperty.PASSWORD, password);
        config.setProperty(ConfigProperty.REPORT_TYPE, reportType);
        config.setProperty(ConfigProperty.PROJECT_KEY, projectKey);
        config.setProperty(ConfigProperty.RELEASE_VERSION, releaseVersion);
        config.setProperty(ConfigProperty.TEST_CYCLE, testCycle);
        config.setProperty(ConfigProperty.JIRA_URL, jiraUrl);
        config.setProperty(ConfigProperty.REPORT_PATH, reportPath);
        config.setProperty(ConfigProperty.ORDERED_STEPS, orderedSteps == null ? null : orderedSteps.toString());
        config.setProperty(ConfigProperty.FORCE_STORY_LINK, forceStoryLink == null ? null : forceStoryLink.toString());
        config.setProperty(ConfigProperty.TEST_CASE_UNIQUE_ID, testCaseUniqueIdAttribute);
        config.setProperty(ConfigProperty.SEVERITY, severityAttribute);
    }
}
