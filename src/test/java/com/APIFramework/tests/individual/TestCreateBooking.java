package com.APIFramework.tests.individual;

import com.APIFramework.base.BaseTest;
import com.APIFramework.endpoints.APIConstants;
import com.APIFramework.pojos.restfulbook.responsepojo.BookingResponse;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class TestCreateBooking extends BaseTest {

    @Test(groups = "reg", priority = 1)
    @Description("TC#1 - Verify that the Booking can be Created")
    public void testCreateBookingPOST_Positive() {
        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);

        response = RestAssured.given(requestSpecification)
                .when()
                .body(payloadManager.createPayloadBookingAsString_Serialization())
                .log().all()
                .post();

        BookingResponse bookingResponse = payloadManager.bookingResponseJava_DeSerialization(response.asString());

        assertActions.verifyStatusCode(response, 200);
        assertActions.verifyStringKeyNotNull(bookingResponse.getBookingid());
        assertActions.verifyStringKey(bookingResponse.getBooking().getFirstname(), "Lucky");
    }

    @Test(groups = "reg", priority = 2)
    @Description("TC#2 - Verify that the Booking can't be Created, When Payload is null")
    public void testCreateBookingPOST_Negative() {
        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);

        response = RestAssured.given(requestSpecification)
                .when()
                .body("{}")
                .log().all()
                .post();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(500);
    }

    @Test(groups = "reg", priority = 3)
    @Description("TC#3 - Verify that the Booking can be Created, When Payload is CHINESE")
    public void testCreateBookingPOST_POSITIVE_CHINESE() {
        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);

        response = RestAssured.given(requestSpecification)
                .when()
                .body(payloadManager.createPayloadBookingAsStringWrongBody())
                .log().all()
                .post();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        BookingResponse bookingResponse = payloadManager.bookingResponseJava_DeSerialization(response.asString());
        assertActions.verifyStringKeyNotNull(bookingResponse.getBookingid());
    }

    @Test(groups = "reg", priority = 4)
    @Description("TC#4 - Verify that the Booking can be Created, When Payload is RANDOM")
    public void testCreateBookingPOST_POSITIVE_FAKER_RANDOM_DATA() {
        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);

        response = RestAssured.given(requestSpecification)
                .when()
                .body(payloadManager.createPayloadBookingFakerJS())
                .log().all()
                .post();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        BookingResponse bookingResponse = payloadManager.bookingResponseJava_DeSerialization(response.asString());
        assertActions.verifyStringKeyNotNull(bookingResponse.getBookingid());
        assertActions.verifyStringKeyNotNull(bookingResponse.getBooking().getFirstname());
    }

    @Test(groups = "reg", priority = 5)
    @Description("TC#6 - Verify that the Booking can be Created, URL is wrong")
    public void testCreateBookingPOST_NEGATIVE_BASE_URL() {
        // Base Global Spec ko override karne ke bajaye chain mein requestSpecification use kiya hai
        response = RestAssured.given()
                .spec(requestSpecification)
                .baseUri(APIConstants.APP_VWO_URL)
                .basePath(APIConstants.CREATE_UPDATE_BOOKING_URL)
                .contentType(ContentType.HTML)
                .when()
                .body(payloadManager.createPayloadBookingFakerJS())
                .log().all()
                .post();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(404);
    }
}