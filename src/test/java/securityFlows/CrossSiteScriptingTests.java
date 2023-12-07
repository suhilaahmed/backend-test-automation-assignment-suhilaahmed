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

//Perform a request and if the response returns the submitted script, it's called a self-XSS.

@Feature("Verify that IssuesAPI can process a XSS as a normal text")
public class CrossSiteScriptingTests extends IssuesAPI {

    static SoftAssertions softAssertions = new SoftAssertions();
    static ConfigReader configReader = new ConfigReader();
    static int internalIssueId;

    @Test(description = "To add an issue with a XSS in title", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that the user can pass a XSS to the API without causing vulnerabilities")
    public void addNewIssueWithXSSInTitle() {
        Response response = addNewIssueToProject(configReader.getProperty("XSSText"), labels[0], Integer.parseInt(configReader.getProperty("projectId")));
        Map<String, ?> issue = response.getBody().jsonPath().get();

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(201);

        softAssertions.assertThat(issue.get("iid")).isNotNull();
        softAssertions.assertThat(issue.get("title")).isEqualTo(configReader.getProperty("XSSText"));
        softAssertions.assertAll();
        internalIssueId = (int) issue.get("iid");
    }

    @Test(description = "To update an existing project issue : provide a XSS in title ", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that the user can pass a XSS in the title without causing vulnerabilities")
    public void updateIssueWithXSSInTitle() {
        Response response = updateAnIssue(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId, "title", configReader.getProperty("XSSText"));
        Map<String, ?> issue = response.getBody().jsonPath().get();

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

        softAssertions.assertThat(issue.get("title")).isNotNull();
        softAssertions.assertThat(issue.get("title")).isEqualTo(configReader.getProperty("XSSText"));
        softAssertions.assertAll();
    }

}
