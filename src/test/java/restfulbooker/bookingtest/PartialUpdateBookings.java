package restfulbooker.bookingtest;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.restassured.response.Response;
import restfulbooker.builder.BookingDataBuilder;
import restfulbooker.requests.CreateBookingRequest;
import restfulbooker.utils.ApplicationUtils;
import restfulbooker.utils.RestAssuredUtils;
import restfulbooker.utils.BookingUtils.BookingEndpoints;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PartialUpdateBookings extends BaseTest {

	static RestAssuredUtils restAssuredUtils;
	static ApplicationUtils applicationUtils;
	static BookingDataBuilder bookingDataBuilder;

	static int bookingID;

	@BeforeClass
	public static void setup() {

		initialize();

		restAssuredUtils = new RestAssuredUtils();
		applicationUtils = new ApplicationUtils();
		bookingDataBuilder = new BookingDataBuilder();

		CreateBookingRequest bookingRequest = bookingDataBuilder.generateBookingData();

		Response response = applicationUtils.createBooking(bookingRequest, BookingEndpoints.BOOKING_URL.getKey());

		bookingID = applicationUtils.getBookingID(response);
	}

	/* Test that any existing booking can be partially updated */

	@Test
	public void testA_partialUpdateBooking() {

		SoftAssertions assertions = new SoftAssertions();

		Map<String, Object> jsonBody = new HashMap<String, Object>();

		jsonBody.put("totalprice", 6000);
		jsonBody.put("additionalneeds", "lunch");

		Response response = applicationUtils.partialUpdateBooking(BookingEndpoints.BOOKING_PARAM_URL.getKey(), jsonBody,
				username, password, bookingID);

		CreateBookingRequest booking = response.as(CreateBookingRequest.class);

		assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		assertions.assertThat(booking.getTotalprice()).isEqualTo(6000);
		assertions.assertThat(booking.getAdditionalneeds()).isEqualTo("lunch");

		assertions.assertAll();
	}

	/*
	 * Verify that the booking is actually updated from the getBookingByID response
	 */

	@Test
	public void testB_checkUpdatedBooking() {

		SoftAssertions assertions = new SoftAssertions();

		Response response = applicationUtils.getBookingByID(BookingEndpoints.BOOKING_PARAM_URL.getKey(), bookingID);

		CreateBookingRequest updatedBooking = response.as(CreateBookingRequest.class);

		assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		assertions.assertThat(updatedBooking.getTotalprice()).isEqualTo(6000);
		assertions.assertThat(updatedBooking.getAdditionalneeds()).isEqualTo("lunch");

		assertions.assertAll();
	}

	/*
	 * Verify proper status code and response message when invalid booking ID is
	 * provided
	 */
	@Test
	public void testC_checkPartialUpdateWithInvalidBookingID() {

		SoftAssertions assertions = new SoftAssertions();

		Map<String, Object> jsonBody = new HashMap<String, Object>();

		jsonBody.put("totalprice", 7000);
		jsonBody.put("additionalneeds", "dinner");

		Response response = applicationUtils.partialUpdateBooking(BookingEndpoints.BOOKING_PARAM_URL.getKey(), jsonBody,
				username, password, 0);

		assertions.assertThat(response.getStatusCode()).isEqualTo(405);
		assertions.assertThat(response.asString()).isEqualTo("Method Not Allowed");

		assertions.assertAll();
	}

	/* Verify that unauthorized user is not allowed to update the booking */

	@Test
	public void testD_checkPartialUpdateWithInvalidCredentials() {

		SoftAssertions assertions = new SoftAssertions();

		Map<String, Object> jsonBody = new HashMap<String, Object>();

		jsonBody.put("totalprice", 7000);
		jsonBody.put("additionalneeds", "dinner");

		Response response = applicationUtils.partialUpdateBooking(BookingEndpoints.BOOKING_PARAM_URL.getKey(), jsonBody,
				"invalid", "invalid", bookingID);

		assertions.assertThat(response.getStatusCode()).isEqualTo(403);
		assertions.assertThat(response.asString()).isEqualTo("Forbidden");

		assertions.assertAll();
	}

	/* Delete the booking created */

	@AfterClass
	public static void tearDown() {

		applicationUtils.deleteBooking(BookingEndpoints.BOOKING_PARAM_URL.getKey(), username, password, bookingID);
	}

}
