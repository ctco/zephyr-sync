package lv.ctco.zephyr.mojo;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.ZephyrSyncService;
import lv.ctco.zephyr.enums.ConfigProperty;

@Mojo( name = "sync" )
public class ZephyrSyncMojo
    extends AbstractMojo
    implements Config.Loader
{

    /**
     * User name used to connect to JIRA.
     */
    @Parameter( required = true )
    private String username;

    /**
     * Password for the user to connect to JIRA.
     */
    @Parameter( required = true )
    private String password;

    /**
     * Type of report that will be synchronized to Zephyr. One of `cucumber`, `allure`, `junit` or `nunit`.
     */
    @Parameter( required = true )
    private String reportType;

    /**
     * Key of project in JIRA.
     */
    @Parameter( required = true )
    private String projectKey;

    /**
     * FixVersion of a project to link Test results to.
     */
    @Parameter( required = true )
    private String releaseVersion;

    /**
     * Zephyr test cycle where the results will be linked to.
     */
    @Parameter( required = true )
    private String testCycle;

    /**
     * URL of JIRA (it's RESTful API endpoint), eg "http://your.jira.server/jira/rest/".
     */
    @Parameter( required = true )
    private String jiraUrl;

    /**
     * Path on the file system where reports are stored, eg "${project.build.directory}/cucumber-report/report.json".
     */
    @Parameter( required = true )
    private String reportPath;

    /**
     * If set to true, numerical prefix for test steps will be put (hierarchical).
     */
    @Parameter( defaultValue = "false" )
    private Boolean orderedSteps;

    /**
     * If set to true, sync will be failed in case at least one test doesn't have @Stories=ABC-XXX annotation.
     */
    @Parameter( defaultValue = "true" )
    private Boolean forceStoryLink;

    /**
     * 
     */
    @Parameter( defaultValue = "false" )
    private Boolean generateTestCaseUniqueId;

    /**
     * Name of JIRA attribute that stores 'Severity' attribute.
     */
    @Parameter
    private String severityAttribute;

    /**
     * Should new test cycle be created automatically?
     */
    @Parameter( defaultValue = "true" )
    private Boolean autoCreateTestCycle;

    /**
     * Specify an Assignee.
     */
    @Parameter
    private String assignee;

    /**
     * Link type between Test issue and related story (used in combination with `@Stories` annotation).
     */
    @Parameter
    private String linkType;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        Config config = new Config( this );

        ZephyrSyncService syncService = new ZephyrSyncService( config );
        try
        {
            syncService.execute();
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Cannot sync test results into zephyr", e );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }

    public void execute( Config config )
    {
        config.setValue( ConfigProperty.USERNAME, username );
        config.setValue( ConfigProperty.PASSWORD, password );
        config.setValue( ConfigProperty.REPORT_TYPE, reportType );
        config.setValue( ConfigProperty.PROJECT_KEY, projectKey );
        config.setValue( ConfigProperty.RELEASE_VERSION, releaseVersion );
        config.setValue( ConfigProperty.TEST_CYCLE, testCycle );
        config.setValue( ConfigProperty.JIRA_URL, jiraUrl );
        config.setValue( ConfigProperty.REPORT_PATH, reportPath );
        config.setValue( ConfigProperty.ORDERED_STEPS, orderedSteps );
        config.setValue( ConfigProperty.FORCE_STORY_LINK, forceStoryLink );
        config.setValue( ConfigProperty.GENERATE_TEST_CASE_UNIQUE_ID, generateTestCaseUniqueId );
        config.setValue( ConfigProperty.SEVERITY, severityAttribute );
        config.setValue( ConfigProperty.AUTO_CREATE_TEST_CYCLE, autoCreateTestCycle );
    }
}
