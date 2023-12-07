package accelators;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


import java.util.List;
import java.util.Map;


public class Accelerators {

    public static  List<Map<String,?>> getPayloadAsList(Response response) {
        JsonPath jsonPath = response.getBody().jsonPath();
        return jsonPath.getList("$");
    }
}
