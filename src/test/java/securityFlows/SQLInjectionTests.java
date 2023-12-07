package securityFlows;

import api.endpoints.IssuesAPI;
import config.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.util.Map;

//Providing an example on how we can test any API against SQL injections

@Feature("Verify that IssuesAPI will inject a SQL script while processing data")
public class SQLInjectionTests extends IssuesAPI {
    SoftAssertions softAssertions = new SoftAssertions();
    ConfigReader configReader = new ConfigReader();
    int internalIssueId;

    @Test(description = "To add an issue with injected SQL script in title", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user can pass a SQL script to the API without causing vulnerabilities")
    public void addNewIssueWithSQLScriptInTitle() {
        Response response = addNewIssueToProject(configReader.getProperty("script"), labels[0], Integer.parseInt(configReader.getProperty("projectId")));
        Map<String, ?> issue = response.getBody().jsonPath().get();

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(201);

        softAssertions.assertThat(issue.get("iid")).isNotNull();
        softAssertions.assertThat(issue.get("title")).isEqualTo(configReader.getProperty("script"));
        softAssertions.assertAll();
        internalIssueId = (int) issue.get("iid");
    }

    @Test(description = "To update an existing project issue : provide a SQL script in title ", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that the user can pass a SQL script in the title without causing vulnerabilities")
    public void updateIssueWithXSSInTitle() {
        Response response = updateAnIssue(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId, "title", configReader.getProperty("script"));
        Map<String, ?> issue = response.getBody().jsonPath().get();

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

        softAssertions.assertThat(issue.get("title")).isNotNull();
        softAssertions.assertThat(issue.get("title")).isEqualTo(configReader.getProperty("script"));
        softAssertions.assertAll();
    }

}
