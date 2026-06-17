package com.APIFramework.base;

import com.APIFramework.asserts.AssertActions;
import com.APIFramework.endpoints.APIConstants;
import com.APIFramework.modules.vwo.PayloadManager;
import com.APIFramework.modules.vwo.VWOPayloadManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    // Common to all test cases
    public RequestSpecification requestSpecification;
    public Response response;
    public ValidatableResponse validatableResponse;

    public AssertActions assertActions;
    public PayloadManager payloadManager;
    public VWOPayloadManager vwoPayloadManager;
    public JsonPath jsonPath;

    @BeforeClass
    public void setup() {
        logger.info("=== Starting Test Setup ===");
        logger.info("Initializing PayloadManager, AssertActions, and VWOPayloadManager");

        payloadManager = new PayloadManager();
        assertActions = new AssertActions();
        vwoPayloadManager = new VWOPayloadManager();


        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(APIConstants.BASE_URL)
                .addHeader("Content-Type", "application/json")
                .build();

        requestSpecification.log().all();

        logger.info("Base URL set to: {}", APIConstants.BASE_URL);
        logger.info("=== Test Setup Complete ===");
    }

    @AfterClass
    public void tearDown() {
        logger.info("=== Test Teardown Complete ===");
    }


    public String getToken() {
        logger.info("Generating authentication token");

        RequestSpecification authSpec = RestAssured.given()
                .baseUri(APIConstants.BASE_URL)
                .basePath(APIConstants.AUTH_URL)
                .contentType(ContentType.JSON);

        String payload = payloadManager.setAuthPayload();
        logger.debug("Auth payload: {}", payload);

        Response authResponse = authSpec.body(payload).when().post();
        String token = payloadManager.getTokenFromJSON(authResponse.asString());

        logger.info("Token generated successfully");
        return token;
    }
}