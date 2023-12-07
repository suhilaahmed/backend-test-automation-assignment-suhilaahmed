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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Verify that the Delete new issues API is working correctly")
public class DeleteIssuesTests extends IssuesAPI {

    SoftAssertions softAssertions = new SoftAssertions();
    static ConfigReader configReader = new ConfigReader();
    static Faker faker = new Faker();
    static int internalIssueId;

    @BeforeTest
    public static void initialize(){
        Response response = addNewIssueToProject(faker.name().title(), "bug", Integer.parseInt(configReader.getProperty("projectId")));
        Map<String, ?> issue = response.getBody().jsonPath().get();
        internalIssueId = Integer.parseInt(issue.get("iid").toString());
    }

    @Test(description = "To delete an issue from a project with valid issue Id", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can delete an issue using valid internal issue Id")
    public void deleteIssueFromProjectWithValidId() {
        Response response = deleteIssueFromProject(Integer.parseInt(configReader.getProperty("projectId")), internalIssueId);

        softAssertions.assertThat(response.getBody()).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(204);

        //Verify issue does not exist
        Response responseNotFound = getIssueById(internalIssueId);
        softAssertions.assertThat(responseNotFound.getBody()).isNotNull();
        softAssertions.assertThat(responseNotFound.getStatusCode()).as("The status code is equal to: ").isEqualTo(404);
        softAssertions.assertAll();
    }

    @Test(description = "Edge cases - To delete an issue project without a valid issue Id", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user cannot delete an issue with invalid internal issue Id")
    public void deleteIssueFromProjectWithInvalidIssueId() {
        Response response = deleteIssueFromProject(Integer.parseInt(configReader.getProperty("projectId")), faker.idNumber().hashCode());

        assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(404);
    }

    @Test(description = "Edge cases - To delete an issue project without a valid project Id", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user cannot delete an issue with invalid project Id")
    public void deleteIssueFromProjectWithInvalidProjectId() {
        Response response = deleteIssueFromProject(faker.idNumber().hashCode(), internalIssueId);

        assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(404);
    }

    @Test(description = "Edge cases - To delete an issue project without a private token", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user cannot delete an issue from project without private token")
    public void deleteIssueFromProjectWithoutPrivateToken() {
        Response response = deleteIssueFromProjectWithoutToken(faker.idNumber().hashCode(), internalIssueId);

        assertThat(response.getStatusCode()).as("The status code is equal to: ").isEqualTo(401);
    }



}
