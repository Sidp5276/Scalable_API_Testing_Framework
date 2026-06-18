package com.APIFramework.tests.e2e_integration;

import com.APIFramework.base.BaseTest;
import com.APIFramework.endpoints.APIConstants;
import com.APIFramework.pojos.restfulbook.requestPOJO.Booking;
import com.APIFramework.pojos.restfulbook.responsepojo.BookingResponse;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

public class TestIntegrationFlow2 extends BaseTest {

    // Test E2E Scenario 2
    // 1. Create a Booking -> bookingID
    // 2. Verify the Booking (GET request)
    // 3. Update the Booking with PATCH (only firstname and lastname)
    // 4. Verify the PATCH was successful

    @Test(groups = "qa", priority = 1)
    @Description("TC#INT2 - Step 1. Verify that the Booking can be Created")
    public void testCreateBooking(ITestContext iTestContext) {
        logger.info("=== TC#INT2 - Step 1: Creating Booking ===");

        String payload = payloadManager.createPayloadBookingAsString_Serialization();

        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);
        response = RestAssured.given(requestSpecification)
                .when().body(payload)
                .post();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        io.restassured.path.json.JsonPath jsonPathEvaluator = response.jsonPath();

        String actualFirstName = jsonPathEvaluator.get("booking.firstname");
        String actualLastName = jsonPathEvaluator.get("booking.lastname");
        Integer bookingid = jsonPathEvaluator.get("bookingid");

        // Direct local string assertions
        Assert.assertEquals(actualFirstName, "James");
        Assert.assertEquals(actualLastName, "Brown");
        Assert.assertNotNull(bookingid);

        iTestContext.setAttribute("bookingid", bookingid);

        logger.info("Booking created successfully with ID: {}", bookingid);
    }

    @Test(groups = "qa", priority = 2, dependsOnMethods = "testCreateBooking")
    @Description("TC#INT2 - Step 2. Verify the Booking by ID")
    public void testVerifyBookingId(ITestContext iTestContext) {
        Integer bookingid = (Integer) iTestContext.getAttribute("bookingid");
        logger.info("=== TC#INT2 - Step 2: Verifying Booking ID: {} ===", bookingid);

        String basePathGET = APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingid;

        requestSpecification.basePath(basePathGET);
        response = RestAssured.given(requestSpecification)
                .when().get();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        Booking booking = payloadManager.getResponseFromJSON(response.asString());

        // 🟢 TestNG Assertions
        Assert.assertEquals(booking.getFirstname(), "James");
        Assert.assertEquals(booking.getLastname(), "Brown");

        logger.info("Booking verified successfully for ID: {}", bookingid);
    }

    @Test(groups = "qa", priority = 3, dependsOnMethods = "testVerifyBookingId")
    @Description("TC#INT2 - Step 3. Update Booking with PATCH (firstname and lastname only)")
    public void testPartialUpdateBookingByID(ITestContext iTestContext) {
        Integer bookingid = (Integer) iTestContext.getAttribute("bookingid");
        logger.info("=== TC#INT2 - Step 3: PATCH Booking ID: {} ===", bookingid);

        String token = getToken();
        iTestContext.setAttribute("token", token);

        String basePathPATCH = APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingid;
        requestSpecification.basePath(basePathPATCH);

        response = RestAssured.given(requestSpecification)
                .cookie("token", token)
                .when().body(payloadManager.createPartialUpdatePayload("James", "Brown"))
                .patch();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        Booking booking = payloadManager.getResponseFromJSON(response.asString());

        // 🟢 TestNG Assertions
        Assert.assertEquals(booking.getFirstname(), "James");
        Assert.assertEquals(booking.getLastname(), "Brown");

        logger.info("Booking patched successfully - firstname: James, lastname: Brown");
    }

    @Test(groups = "qa", priority = 4, dependsOnMethods = "testPartialUpdateBookingByID")
    @Description("TC#INT2 - Step 4. Verify PATCH was successful by GET request")
    public void testVerifyPatchUpdate(ITestContext iTestContext) {
        Integer bookingid = (Integer) iTestContext.getAttribute("bookingid");
        logger.info("=== TC#INT2 - Step 4: Verifying PATCH for Booking ID: {} ===", bookingid);

        String basePathGET = APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingid;

        requestSpecification.basePath(basePathGET);
        response = RestAssured.given(requestSpecification)
                .when().get();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        Booking booking = payloadManager.getResponseFromJSON(response.asString());

        // 🟢 TestNG Assertions
        Assert.assertEquals(booking.getFirstname(), "James");
        Assert.assertEquals(booking.getLastname(), "Brown");

        logger.info("PATCH verified successfully for ID: {}", bookingid);
    }
}