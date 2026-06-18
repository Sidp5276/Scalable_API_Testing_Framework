package com.APIFramework.tests.e2e_integration;

import com.APIFramework.base.BaseTest;
import com.APIFramework.endpoints.APIConstants;
import com.APIFramework.pojos.restfulbook.requestPOJO.Booking;
import com.APIFramework.pojos.restfulbook.responsepojo.BookingResponse;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.testng.ITestContext;
import org.testng.annotations.Test;


    public class TestIntegrationFlow1 extends BaseTest {

        // Test E2E Scenario 1
        // 1. Create a Booking -> bookingID
        // 2. Verify that the Create Booking is working - GET Request to bookingID
        // 3. Update the booking (bookingID, Token)
        // 4. Delete the Booking

        @Test(groups = "qa", priority = 1)
        @Description("TC#INT1 - Step 1. Verify that the Booking can be Created")
        public void testCreateBooking(ITestContext iTestContext) {
            logger.info("=== TC#INT1 - Step 1: Creating Booking ===");

            requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);
            response = RestAssured.given(requestSpecification)
                    .when().body(payloadManager.createPayloadBookingAsString_Serialization())
                    .post();

            validatableResponse = response.then().log().all();
            validatableResponse.statusCode(200);

            // Validate response schema
            assertActions.verifyBookingResponseSchema(response);

            BookingResponse bookingResponse = payloadManager.bookingResponseJava_DeSerialization(response.asString());
            assertActions.verifyStringKey(bookingResponse.getBooking().getFirstname(), "James");
            assertActions.verifyStringKeyNotNull(bookingResponse.getBookingid());

            Integer bookingid = bookingResponse.getBookingid();
            iTestContext.setAttribute("bookingid", bookingid);

            logger.info("Booking created successfully with ID: {}", bookingid);
        }

        @Test(groups = "qa", priority = 2)
        @Description("TC#INT1 - Step 2. Verify that the Booking By ID")
        public void testVerifyBookingId(ITestContext iTestContext) {
            Integer bookingid = (Integer) iTestContext.getAttribute("bookingid");
            logger.info("=== TC#INT1 - Step 2: Verifying Booking ID: {} ===", bookingid);

            String basePathGET = APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingid;

            requestSpecification.basePath(basePathGET);
            response = RestAssured.given(requestSpecification)
                    .when().get();

            validatableResponse = response.then().log().all();
            validatableResponse.statusCode(200);

            // Validate response schema
            assertActions.verifyBookingSchema(response);

            Booking booking = payloadManager.getResponseFromJSON(response.asString());
            assertActions.verifyStringKeyNotNull(booking.getFirstname());

            logger.info("Booking verified successfully for ID: {}", bookingid);
        }

        @Test(groups = "qa", priority = 3)
        @Description("TC#INT1 - Step 3. Verify Updated Booking by ID")
        public void testUpdateBookingByID(ITestContext iTestContext) {
            Integer bookingid = (Integer) iTestContext.getAttribute("bookingid");
            logger.info("=== TC#INT1 - Step 3: Updating Booking ID: {} ===", bookingid);

            String token = getToken();
            iTestContext.setAttribute("token", token);

            String basePathPUTPATCH = APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingid;

            requestSpecification.basePath(basePathPUTPATCH);

            response = RestAssured.given(requestSpecification)
                    .cookie("token", token)
                    .when().body(payloadManager.createPayloadBookingAsString_Serialization()).put();

            validatableResponse = response.then().log().all();
            validatableResponse.statusCode(200);

            Booking booking = payloadManager.getResponseFromJSON(response.asString());
            assertActions.verifyStringKeyNotNull(booking.getFirstname());
            assertActions.verifyStringKey(booking.getFirstname(), "James");

            logger.info("Booking updated successfully for ID: {}", bookingid);
        }

        @Test(groups = "qa", priority = 4)
        @Description("TC#INT1 - Step 4. Delete the Booking by ID")
        public void testDeleteBookingById(ITestContext iTestContext) {
            Integer bookingid = (Integer) iTestContext.getAttribute("bookingid");
            String token = (String) iTestContext.getAttribute("token");
            logger.info("=== TC#INT1 - Step 4: Deleting Booking ID: {} ===", bookingid);

            String basePathDELETE = APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingid;

            requestSpecification.basePath(basePathDELETE).cookie("token", token);
            validatableResponse = RestAssured.given().spec(requestSpecification)
                    .when().delete().then().log().all();

            validatableResponse.statusCode(201);

            logger.info("Booking deleted successfully for ID: {}", bookingid);
        }
}
