[![wercker status](https://app.wercker.com/status/5f663d10cb826a4ebbbfaa7d2f6f1420/s/master "wercker status")](https://app.wercker.com/project/byKey/5f663d10cb826a4ebbbfaa7d2f6f1420)

# Zephyr Sync

## Overview

Zephyr Sync is a tool, that allows your project to perform synchronization of automated test results to Zephyr - a JIRA addon for Test Management. The advanced configuration of the tool supports multiple report types to work with, as well as some restrictions to be applied during the sync.

## Usage example

### Maven

All changes should be done inside `pom.xml`.

#### Using zephyr-sync-maven-plugin (recommended)

The configuration is very simple and should be done in `pom.xml`
(note that this example is given for `cucumber`, other reports like `allure` are configured in the similar way):

```
<plugin>
    <groupId>lv.ctco.zephyr</groupId>
    <artifactId>zephyr-sync-maven-plugin</artifactId>
    <version>${zephyr-sync.version}</version>
    <dependencies>
        <dependency>
            <groupId>lv.ctco.zephyr</groupId>
            <artifactId>zephyr-sync-report-cucumber</artifactId>
            <version>${zephyr-sync.version}</version>
        </dependency>
    </dependencies>
    <configuration>
        <username>TECXYZ01</username>
        <password>${env.TECXYZ01_PWD}</password>
        <reportType>cucumber</reportType>
        <projectKey>XYZ</projectKey>
        <jiraUrl>http://jira.yourcompany.com/rest/</jiraUrl>
        <releaseVersion>1.0</releaseVersion>
        <reportPath>${project.build.directory}/cucumber-report/report.json</reportPath>
    </configuration>
</plugin>
```

#### Using maven-exec-plugin (deprecated)
First of all - declare dependency to `zephyr-sync-core`:

```
<dependencies>
    ...
    <dependency>
        <groupId>lv.ctco.zephyr</groupId>
        <artifactId>zephyr-sync-core</artifactId>
        <version>${zephyr-sync.version}</version>
    </dependency>
</dependencies>
```

Also configure a Maven plugin that will trigger synchronization to JIRA:

```
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>1.3.1</version>
    <executions>
        <execution>
            <id>default-cli</id>
            <goals>
                <goal>java</goal>
            </goals>
            <configuration>
                <mainClass>lv.ctco.zephyr.Runner</mainClass>
                <arguments>
                    <argument>--username=TECXYZ01</argument>
                    <argument>--password=${env.TECXYZ01_PWD}</argument>
                    <argument>--reportType=cucumber</argument>
                    <argument>--projectKey=XYZ</argument>
                    <argument>--releaseVersion=1.0</argument>
                    <argument>--jiraUrl=http://jira.yourcompany.com/rest/</argument>
                    <argument>--reportPath=${project.build.directory}/cucumber-report/report.json</argument>
                </arguments>
            </configuration>
        </execution>
    </executions>
</plugin>

```

This example shows only minimal set of mandatory attributes.
For complete list of attributes refer to sections below.

### Command Line Interface

```
java -jar zephyr-sync-cli-${zephyr-sync.version}-all-in-one.jar --username=SPCABC --password=123456 --reportType=cucumber --projectKey=ABC --releaseVersion="Release 2.1" --jiraUrl=http://jira.yourcompany.com/rest/ --reportPath=build/cucumber-report/report.json
```

### Using Gradle (using CLI)
```
task zephyrSync {
         javaexec {
            main = "lv.ctco.zephyr.Runner"
            classpath = sourceSets.main.output + sourceSets.test.output
            args = ["--username=SPCABC", "--password=123456", "--reportType=cucumber", "--projectKey=ABC",
                    "--releaseVersion=Release 2.1", "--jiraUrl=http://jira.yourcompany.com/rest/", "--reportPath=build/cucumber-report/report.json"]
        }
    }
```

## Configuration properties

This is the list of possible configuration items:

Property | Meaning | Is mandatory? | Default value | Example
--- | --- | --- | --- | ---
username | User name used to connect to JIRA | yes | | `TECXYZ01`
password | Password for the user to connect to JIRA | yes | | `password`
reportType | Type of report that will be synchronized to Zephyr | yes/no | Default value could be detected in runtime | One of `cucumber`, `allure`, `junit` or `nunit`
projectKey | Key of project in JIRA | yes | | `XYZ`
releaseVersion | FixVersion of a project to link Test results to | yes | | `1.0`
testCycle | Zephyr test cycle where the results will be linked to | no | `Regression` |
jiraUrl | URL of JIRA (it's RESTful API endpoint) | yes | | `http://jira.yourcompany.com/rest/`
reportPath | Path on the file system where reports are stored | yes | | For cucumeber: `${project.build.directory}/cucumber-report/report.json`
orderedSteps | If set to true, numerical prefix for test steps will be put (hierarchical) | no | `false` |
forceStoryLink | If set to true, sync will be failed in case at least one test doesn't have @Stories=ABC-XXX annotation | no | `true` |
testCaseUniqueIdAttribute | Name of JIRA attribute that is used to store unique ID of test case (will be used for test case tracking, updates and linking) | no | `environment` |
severityAttribute | Name of JIRA attribute that stores 'Severity' attribute | no | `customfield_10067` |
autoCreateTestCycle | Should new test cycle be created automatically | no | `true` |
linkType | Link type between Test issue and related story (used in combination with `@Stories` annotation) | no | `Reference` |
