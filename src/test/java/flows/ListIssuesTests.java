package flows;

import api.endpoints.IssuesAPI;
import com.github.javafaker.Faker;
import config.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static accelators.Accelerators.getPayloadAsList;
import static org.assertj.core.api.Assertions.assertThat;


@Feature("Verify that the Get issues API is working correctly")
public class ListIssuesTests extends IssuesAPI {

    //To be used when there are more than 3 assertions to not fail the test
    SoftAssertions softAssertions = new SoftAssertions();
    ConfigReader configReader = new ConfigReader();
    List<Integer> issuesIds = new ArrayList<>();
    Faker faker = new Faker();

    @Test(description = "To get the list of available issues to the user", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can get the list of gitlab IssuesAPI")
    public void checkListOfAvailableIssuesToTheUser() {
        Response response = listAllIssues("", "");

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

        for (Map<String, ?> issue : getPayloadAsList(response)) {
            softAssertions.assertThat(issue.get("project_id")).isNotNull();
            softAssertions.assertThat(issue.get("id")).isNotNull();
            softAssertions.assertThat(issue.get("iid")).isNotNull();
            softAssertions.assertThat(issue.get("title")).isNotNull();
            softAssertions.assertThat(states).contains((String) issue.get("state"));
            softAssertions.assertThat(issue.get("created_at")).isNotNull();
            softAssertions.assertThat(issue.get("updated_at")).isNotNull();
            softAssertions.assertThat(issue.get("labels")).asList().containsAnyElementsOf(Arrays.asList(labels));
            softAssertions.assertThat(issue.get("assignees")).asList().isEmpty();
            softAssertions.assertThat(issue.get("type")).isEqualTo("ISSUE");
            softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("name", "Suhila Ahmed");
            softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("state", "active");
            softAssertions.assertThat(issue.get("has_tasks")).isEqualTo(true);
            issuesIds.add((Integer) issue.get("id"));
            softAssertions.assertAll();
        }
    }

    @Test(description = "To get the list of available opened issues to the user", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can get the list of opened gitlab issues")
    public void checkListOfAvailableIssuesWithOpenedState() {
        Response response = listAllIssues("state", "opened");

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

        for (Map<String, ?> issue : getPayloadAsList(response)) {
            softAssertions.assertThat(issue.get("project_id")).isNotNull();
            softAssertions.assertThat(issue.get("id")).isNotNull();
            softAssertions.assertThat(issue.get("iid")).isNotNull();
            softAssertions.assertThat(issue.get("title")).isNotNull();
            softAssertions.assertThat(issue.get("created_at")).isNotNull();
            softAssertions.assertThat(issue.get("updated_at")).isNotNull();

            softAssertions.assertThat(issue.get("state")).isEqualTo("opened");
            softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("name", "Suhila Ahmed");
            softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("state", "active");
            softAssertions.assertAll();
        }
    }

    @Test(description = "To get the list of available closed issues to the user", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can get the list of closed gitlab issues")
    public void checkListOfAvailableIssuesWithClosedState() {
        Response response = listAllIssues("state", "closed");

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

        for (Map<String, ?> issue : getPayloadAsList(response)) {
            softAssertions.assertThat(issue.get("project_id")).isNotNull();
            softAssertions.assertThat(issue.get("id")).isNotNull();
            softAssertions.assertThat(issue.get("iid")).isNotNull();
            softAssertions.assertThat(issue.get("title")).isNotNull();
            softAssertions.assertThat(issue.get("created_at")).isNotNull();
            softAssertions.assertThat(issue.get("updated_at")).isNotNull();
            softAssertions.assertThat(issue.get("state")).isEqualTo("closed");
            softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("name", "Suhila Ahmed");
            softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("state", "active");
            softAssertions.assertAll();
        }
    }

    @Test(description = "Edge cases - To get the list of available issues when state is set to active", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot get issue list with incorrect parameter value : state = active")
    public void checkAPIResponseWithIncorrectParameterValue() {
        Response response = listAllIssues("state", "active");

        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(400);
        softAssertions.assertThat(response.getBody().jsonPath().getString("error")).isEqualTo("state does not have a valid value");
        softAssertions.assertAll();
    }

    @Test(description = "Edge cases - To get the list of available issues when weight is null", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot get issue list with null parameter value : weight = null")
    public void checkAPIResponseWithNullParameter() {
        Response response = listAllIssues("weight", "null");

        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(400);
        softAssertions.assertThat(response.getBody().jsonPath().getString("error")).isEqualTo("weight should be an integer, none, or any, however got null");
        softAssertions.assertAll();
    }

    @Test(description = "Edge cases - To get the list of available issues when confidential is not true", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot get issue list with incorrect boolean parameter value - confidential = abc")
    public void checkAPIResponseWithIncorrectBooleanValue() {
        Response response = listAllIssues("confidential", "abn");

        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(400);
        softAssertions.assertThat(response.getBody().jsonPath().getString("error")).isEqualTo("confidential is invalid");
        softAssertions.assertAll();
    }

    @Test(description = "Edge cases - To get the list of available issues without a private token", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot get issue list without private token")
    public void checkAPIResponseWithoutPrivateToken() {
        Response response = listAllIssues();

        assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(401);
    }

    //
    //** Gitlab bug - Failing -
    //
    @Test(description = "Edge cases - To get the list of available issues when the scope is set to all", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user can get list of all available issues : scope=all")
    public void checkAPIResponseWhenTheScopeIsIncludingAllIssues() {
        Response response = listAllIssues("scope", "all");

        assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(500);
    }

    @Test(description = "To get issue by a valid issue Id", priority = -1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify that the user can get the issue by it's Id")
    public void getIssueWithCorrectId() {

        for (int issueId : issuesIds) {
            Response response = getIssueById(issueId);

            softAssertions.assertThat(response.getBody()).isNotNull();
            softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

            for (Map<String, ?> issue : getPayloadAsList(response)) {
                softAssertions.assertThat(issue.get("project_id")).isNotNull();
                softAssertions.assertThat(issue.get("id")).isEqualTo(issueId);
                softAssertions.assertThat(issue.get("iid")).isNotNull();
                softAssertions.assertThat(issue.get("title")).isNotNull();
                softAssertions.assertThat(states).contains((String) issue.get("state"));
                softAssertions.assertThat(issue.get("created_at")).isNotNull();
                softAssertions.assertThat(issue.get("updated_at")).isNotNull();
                softAssertions.assertThat(issue.get("labels")).asList().containsAnyElementsOf(Arrays.asList(labels));
                softAssertions.assertThat(issue.get("assignees")).asList().isEmpty();
                softAssertions.assertThat(issue.get("type")).isEqualTo("ISSUE");
                softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("name", "Suhila Ahmed");
                softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("state", "active");
                softAssertions.assertThat(issue.get("has_tasks")).isEqualTo(true);
                softAssertions.assertAll();
            }
        }
    }

    @Test(description = "Edge cases - To get issue by incorrect issue Id", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify that the user cannot get the issue by incorrect Id")
    public void getIssueWithIncorrectId() {

        Response response = getIssueById(faker.idNumber().hashCode());

        assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(404);
    }

    @Test(description = "To get a list specific project issues", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify that the user can get the issue within a project")
    public void getIssuesUnderAnExistingProject() {

        Response response = listAllIssuesUnderProject(Integer.parseInt(configReader.getProperty("projectId")));

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

        for (Map<String, ?> issue : getPayloadAsList(response)) {
            softAssertions.assertThat(issue.get("project_id").toString()).isEqualTo(configReader.getProperty("projectId"));
            softAssertions.assertThat(issue.get("id")).isNotNull();
            softAssertions.assertThat(issue.get("iid")).isNotNull();
            softAssertions.assertThat(issue.get("title")).isNotNull();
            softAssertions.assertThat(states).contains((String) issue.get("state"));
            softAssertions.assertThat(issue.get("created_at")).isNotNull();
            softAssertions.assertThat(issue.get("updated_at")).isNotNull();
            softAssertions.assertThat(issue.get("labels")).asList().containsAnyElementsOf(Arrays.asList(labels));
            softAssertions.assertThat(issue.get("assignees")).asList().isEmpty();
            softAssertions.assertThat(issue.get("type")).isEqualTo("ISSUE");
            softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("name", "Suhila Ahmed");
            softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("state", "active");
            softAssertions.assertThat(issue.get("has_tasks")).isEqualTo(true);
            softAssertions.assertAll();
        }
    }

    @Test(description = "Edge cases - To get list specific project issues with incorrect project Id", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify that the user cannot get the issue under an incorrect project Id")
    public void getIssueWithIncorrectProjectId() {

        Response response = listAllIssuesUnderProject(faker.idNumber().hashCode());

        assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(404);
    }
}
