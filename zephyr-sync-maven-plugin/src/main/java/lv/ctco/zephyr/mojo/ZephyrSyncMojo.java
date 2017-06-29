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
    private String username;
    @Parameter(required = true)
    private String password;
    @Parameter
    private String reportType;
    @Parameter(required = true)
    private String projectKey;
    @Parameter(required = true)
    private String releaseVersion;
    @Parameter
    private String testCycle;
    @Parameter(required = true)
    private String jiraUrl;
    @Parameter(required = true)
    private String reportPath;
    @Parameter
    private Boolean orderedSteps;
    @Parameter
    private Boolean forceStoryLink;
    @Parameter
    private String testCaseUniqueIdAttribute;
    @Parameter
    private String severityAttribute;
    @Parameter
    private Boolean autoCreateTestCycle;


    public void execute() throws MojoExecutionException, MojoFailureException {
        Config config = new Config(this);

        ZephyrSyncService syncService = new ZephyrSyncService(config);
        try {
            syncService.execute();
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot sync test results into zephyr", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        config.setProperty(ConfigProperty.ORDERED_STEPS, orderedSteps);
        config.setProperty(ConfigProperty.FORCE_STORY_LINK, forceStoryLink);
        config.setProperty(ConfigProperty.TEST_CASE_UNIQUE_ID, testCaseUniqueIdAttribute);
        config.setProperty(ConfigProperty.SEVERITY, severityAttribute);
        config.setProperty(ConfigProperty.AUTO_CREATE_TEST_CYCLE, autoCreateTestCycle);
    }
}
