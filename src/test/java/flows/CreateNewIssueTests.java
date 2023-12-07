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
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * Module
 */
// This suite is intended to create a new issue under a project with the required parameters
@Feature("Verify that the Create new issues API is working correctly")
public class CreateNewIssueTests extends IssuesAPI {
    static SoftAssertions softAssertions = new SoftAssertions();
    static ConfigReader configReader = new ConfigReader();
    Faker faker = new Faker();
    static int internalIssueId;

    // Delete the created issue as a cleanup step
    @AfterAll
    public static void cleanup() {
        Response response = deleteIssueFromProject(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId);
        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(204);
    }


    @Test(description = "To add a new issue to a project with required payload", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can add a new issue to the project")
    public void addNewIssueToProjectWithValidId() {
        Response response = addNewIssueToProject(faker.name().title(), labels[0], Integer.parseInt(configReader.getProperty("projectId")));
        Map<String, ?> issue = response.getBody().jsonPath().get();

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(201);

        softAssertions.assertThat(issue.get("project_id")).isNotNull();
        softAssertions.assertThat(issue.get("project_id").toString()).isEqualTo(configReader.getProperty("projectId"));
        softAssertions.assertThat(issue.get("id")).isNotNull();
        softAssertions.assertThat(issue.get("iid")).isNotNull();
        softAssertions.assertThat(issue.get("title")).isNotNull();
        softAssertions.assertThat(issue.get("state")).isEqualTo(states[0]);
        softAssertions.assertThat(issue.get("created_at")).isNotNull();
        softAssertions.assertThat(issue.get("updated_at")).isNotNull();
        softAssertions.assertThat(issue.get("labels")).asList().containsAnyElementsOf(Arrays.asList(labels));
        softAssertions.assertThat(issue.get("assignees")).asList().isEmpty();
        softAssertions.assertThat(issue.get("type")).isEqualTo("ISSUE");
        softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("name", "Suhila Ahmed");
        softAssertions.assertThat(issue.get("author")).hasFieldOrPropertyWithValue("state", "active");
        softAssertions.assertThat(issue.get("has_tasks")).isEqualTo(true);
        softAssertions.assertAll();
        internalIssueId = (int) issue.get("iid");
    }

    @Test(description = "Edge cases - To add a new issue to a project with a missing title", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot add a new issue to project when title is missing")
    public void addNewIssueToProjectWithoutTitle() {
        Response response = addNewIssueToProjectWithoutTitle(labels[0], Integer.parseInt(configReader.getProperty("projectId")));
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(400);
        softAssertions.assertThat(response.getBody().jsonPath().getString("error")).isEqualTo("title is missing");
        softAssertions.assertAll();

    }

    @Test(description = "Edge cases - To add a new issue to a project with empty title string", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot add a new issue without a title string")
    public void addNewIssueToProjectWithoutTitleString() {
        Response response = addNewIssueToProject("", labels[0], Integer.parseInt(configReader.getProperty("projectId")));
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(400);
        softAssertions.assertThat(response.getBody().jsonPath().getString("message")).isEqualTo("[title:[can't be blank]]");
        softAssertions.assertAll();

    }

    @Test(description = "Edge cases - To add a new issue to a project with invalid Id", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot add a new issue to project with invalid Id")
    public void addNewIssueToProjectWithInvalidId() {
        Response response = addNewIssueToProject(faker.name().title(), labels[0], faker.idNumber().hashCode());
        assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(404);

    }

    @Test(description = "Edge cases - To add a new issue to a project without private token", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the user cannot add a new issue to a project without private token")
    public void addNewIssueToProjectWithoutPrivateToken() {
        Response response = addNewIssueToProject(Integer.parseInt(configReader.getProperty("projectId")));

        assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(401);
    }
}

