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
import org.junit.jupiter.api.AfterAll;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;
@Feature("Verify that the Update issues API is working correctly")
public class UpdateIssueTests extends IssuesAPI {
    static SoftAssertions softAssertions = new SoftAssertions();
    static ConfigReader configReader = new ConfigReader();
    static Faker faker = new Faker();
    static int internalIssueId;

    @BeforeTest
    public static void setup() {
        Response response = addNewIssueToProject(faker.name().title(), "bug", Integer.parseInt(configReader.getProperty("projectId")));
        Map<String, ?> issue = response.getBody().jsonPath().get();
        internalIssueId = Integer.parseInt(issue.get("iid").toString());
    }

    @AfterAll
    public static void cleanup() {
        Response response = deleteIssueFromProject(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId);
        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(204);
    }

    @Test(description = "To update an existing project issue : set state_event to closed ", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can update the issue state_event")
    public void updateIssueStateEventToClosed() {
        Response response = updateAnIssue(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId, "state_event", "close");
        Map<String, ?> issue = response.getBody().jsonPath().get();

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

        softAssertions.assertThat(issue.get("project_id").toString()).isEqualTo(configReader.getProperty("projectId"));
        softAssertions.assertThat(issue.get("iid")).isEqualTo(internalIssueId);
        softAssertions.assertThat(issue.get("state")).isNotNull();
        softAssertions.assertThat(issue.get("state")).isEqualTo("closed");

        softAssertions.assertAll();
        internalIssueId = (int) issue.get("iid");
    }

    @Test(description = "To update an existing project issue : set confidential to true ", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can update the issue confidentiality parameter")
    public void updateIssueConfidentialityFlagToTrue() {
        Response response = updateAnIssue(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId, "confidential", true);
        Map<String, ?> issue = response.getBody().jsonPath().get();

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

        softAssertions.assertThat(issue.get("project_id").toString()).isEqualTo(configReader.getProperty("projectId"));
        softAssertions.assertThat(issue.get("iid")).isEqualTo(internalIssueId);
        softAssertions.assertThat(issue.get("confidential")).isNotNull();
        softAssertions.assertThat(issue.get("confidential")).isEqualTo(true);

        softAssertions.assertAll();
        internalIssueId = (int) issue.get("iid");
    }

    @Test(description = "To update an existing project issue : unassigned milestone", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can update the issue milestone Id")
    public void updateIssueMilestoneId() {
        int fakeMilestoneId = faker.idNumber().hashCode();
        Response response = updateAnIssue(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId, "milestone_id", 0 );
        Map<String, ?> issue = response.getBody().jsonPath().get();

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(200);

        softAssertions.assertThat(issue.get("project_id").toString()).isEqualTo(configReader.getProperty("projectId"));
        softAssertions.assertThat(issue.get("iid")).isEqualTo(internalIssueId);
        softAssertions.assertThat(issue.get("milestone_id")).isNull();
        softAssertions.assertThat(issue.get("milestone_id")).isEqualTo(null);

        softAssertions.assertAll();
        internalIssueId = (int) issue.get("iid");
    }

    @Test(description = "Edge Cases - To update an existing project issue state_event with incorrect value ", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot update state event of an issue with incorrect value")
    public void updateIssueStateEventToIncorrectValue() {
        Response response = updateAnIssue(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId, "state_event", faker.name().hashCode());

        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(400);
        softAssertions.assertThat(response.getBody().jsonPath().getString("error")).isEqualTo("state_event does not have a valid value");
        softAssertions.assertAll();;
    }

    @Test(description = "Edge Cases - To update an existing project issue confidentiality with incorrect value", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user cannot update the issue confidentiality parameter with incorrect value")
    public void updateIssueConfidentialityFlagToIncorrectValue() {
        Response response = updateAnIssue(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId, "confidential", faker.name().hashCode());

        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(400);
        softAssertions.assertThat(response.getBody().jsonPath().getString("error")).isEqualTo("confidential is invalid");
        softAssertions.assertAll();;
    }

    @Test(description = "Edge Cases - To update an existing project issue with invalid parameter ", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user cannot update the issue with non existing parameter")
    public void updateIssueWithNonExistingParameter() {
        Response response = updateAnIssue(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId, faker.name().title(), faker.name().hashCode());

        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(400);
        softAssertions.assertThat(response.getBody().jsonPath().getString("error"))
                .isEqualTo("assignee_id, assignee_ids, confidential, created_at, description, discussion_locked, " +
                        "due_date, labels, add_labels, remove_labels, milestone_id, state_event, title, issue_type, weight, " +
                        "epic_id, epic_iid are missing, at least one parameter must be provided");
        softAssertions.assertAll();;
    }

    @Test(description = "Edge Cases - To update an existing project issue with incorrect project Id", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot update the issue with incorrect project Id")
    public void updateIssueWithIncorrectProjectId() {
        Response response = updateAnIssue(faker.idNumber().hashCode(), internalIssueId, "state_event", "close");

        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(404);
        softAssertions.assertThat(response.getBody().jsonPath().getString("message"))
                .isEqualTo("404 Project Not Found");
        softAssertions.assertAll();;
    }

    @Test(description = "Edge cases - To update an existing project issue with incorrect issue Id", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot update the issue with incorrect issue Id")
    public void updateIssueWithIncorrectIssueId() {
        Response response = updateAnIssue(Integer.parseInt(configReader.getProperty("projectId")), faker.idNumber().hashCode(), "state_event", "close");

        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(404);
        softAssertions.assertThat(response.getBody().jsonPath().getString("message"))
                .isEqualTo("404 Not found");
        softAssertions.assertAll();;
    }

    @Test(description = "Edge cases - To update an existing project issue without a private token", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot update the issue without private token")
    public void updateIssueWithoutPrivateToken() {
        Response response = updateAnIssueWithoutToken(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId, "state_event", "close");

        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(401);
        softAssertions.assertThat(response.getBody().jsonPath().getString("message"))
                .isEqualTo("401 Unauthorized");
        softAssertions.assertAll();;
    }

}
