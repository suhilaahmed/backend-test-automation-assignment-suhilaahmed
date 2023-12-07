/*
 * Module dependencies
 */
package api.endpoints;

import config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;


/*
 * Module : perform CRUD operations for issues API
 */

public class IssuesAPI {
    private static final ConfigReader configReader = new ConfigReader();
    protected static final String[] states = {"opened", "closed"};

    protected static final String[] labels = {"30 day trial access", "To do", "Novice", "bug"};


    private static String baseURL;
    private static String baseProjectURL;

    public IssuesAPI() {
        baseURL = configReader.getProperty("gitlabURL") + "api/" + configReader.getProperty("gitlabAPIVersion") + configReader.getProperty("gitlabAPI");
        baseProjectURL = configReader.getProperty("gitlabURL") + "api/" + configReader.getProperty("gitlabAPIVersion") + "projects/";
    }

    public static Response listAllIssues(String parameter, String value) {

        return RestAssured.given().queryParam(parameter, value).header("PRIVATE-TOKEN", configReader.getOauth2Token()).get(baseURL).thenReturn();
    }

    public static Response listAllIssues() {

        return RestAssured.given().get(baseURL).thenReturn();
    }

    public static Response getIssueById(int issueId) {

        return RestAssured.given().header("PRIVATE-TOKEN", configReader.getOauth2Token()).get(baseURL + "/issues/" + issueId).thenReturn();
    }

    public static Response listAllIssuesUnderProject(int projectId) {

        return RestAssured.given().header("PRIVATE-TOKEN", configReader.getOauth2Token()).get(baseProjectURL + projectId + "/issues").thenReturn();
    }


    public static Response addNewIssueToProject(String title, String label, int projectId) {
        String addNewIssueURL = baseProjectURL + projectId + "/issues?title=" + title + "&labels=" + label;
        return RestAssured.given().header("PRIVATE-TOKEN", configReader.getOauth2Token()).post(addNewIssueURL).thenReturn();
    }

    public static Response addNewIssueToProject(int projectId) {
        String addNewIssueURL = baseProjectURL + projectId + "/issues?title=text" + "&labels=";
        return RestAssured.given().post(addNewIssueURL).thenReturn();
    }

    public static Response addNewIssueToProjectWithoutTitle(String label, int projectId) {
        String addNewIssueURL = baseProjectURL + projectId + "/issues?" + "labels=" + label;
        return RestAssured.given().header("PRIVATE-TOKEN", configReader.getOauth2Token()).post(addNewIssueURL).thenReturn();
    }

    public static Response deleteIssueFromProject(int projectId, int issueId){
        String deleteIssueURL = baseProjectURL + projectId + "/issues/" + issueId;
        return RestAssured.given().header("PRIVATE-TOKEN", configReader.getOauth2Token()).delete(deleteIssueURL).thenReturn();
    }

    public static Response deleteIssueFromProjectWithoutToken(int projectId, int issueId){
        String deleteIssueURL = baseProjectURL + projectId + "/issues/" + issueId;
        return RestAssured.given().delete(deleteIssueURL).thenReturn();
    }
    public static Response updateAnIssue(int projectId, int issueId, String parameter, String value){
        String updateIssueURL = baseProjectURL + projectId + "/issues/" + issueId + "?" + parameter + "=" + value;
        return RestAssured.given().header("PRIVATE-TOKEN", configReader.getOauth2Token()).put(updateIssueURL).thenReturn();


    }

    public static Response updateAnIssue(int projectId, int issueId, String parameter, Boolean value){
        String updateIssueURL = baseProjectURL + projectId + "/issues/" + issueId + "?" + parameter + "=" + value;
        return RestAssured.given().header("PRIVATE-TOKEN", configReader.getOauth2Token()).put(updateIssueURL).thenReturn();

    }

    public static Response updateAnIssue(int projectId, int issueId, String parameter, int value){
        String updateIssueURL = baseProjectURL + projectId + "/issues/" + issueId + "?" + parameter + "=" + value;
        return RestAssured.given().header("PRIVATE-TOKEN", configReader.getOauth2Token()).put(updateIssueURL).thenReturn();

    }

    public static Response updateAnIssueWithoutToken(int projectId, int issueId, String parameter, String value){
        String updateIssueURL = baseProjectURL + projectId + "/issues/" + issueId + "?" + parameter + "=" + value;
        return RestAssured.given().put(updateIssueURL).thenReturn();

    }

}
