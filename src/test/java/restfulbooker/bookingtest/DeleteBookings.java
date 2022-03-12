package restfulbooker.bookingtest;

import org.assertj.core.api.SoftAssertions;
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
public class DeleteBookings extends BaseTest {

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

	/* Verify that unauthorized is not allowed to delete any existing booking */

	@Test
	public void testA_deleteBookingWithInvalidUser() {

		SoftAssertions assertions = new SoftAssertions();

		Response response = applicationUtils.deleteBooking(BookingEndpoints.BOOKING_PARAM_URL.getKey(), "invalid",
				"invalid", bookingID);

		assertions.assertThat(response.getStatusCode()).isEqualTo(403);
		assertions.assertThat(response.asString()).isEqualTo("Forbidden");

		assertions.assertAll();
	}

	/*
	 * Verify proper status code and response message for successful deletion of
	 * booking
	 */

	@Test
	public void testB_deleteBooking() {

		SoftAssertions assertions = new SoftAssertions();

		Response response = applicationUtils.deleteBooking(BookingEndpoints.BOOKING_PARAM_URL.getKey(), username,
				password, bookingID);

		assertions.assertThat(response.getStatusCode()).isEqualTo(201);
		assertions.assertThat(response.asString()).isEqualTo("Created");

		assertions.assertAll();
	}

	/* Verify that deleted booking no longer exists in the system */

	@Test
	public void testC_checkBookingNoLongerExists() {

		SoftAssertions assertions = new SoftAssertions();

		Response response = applicationUtils.getBookingByID(BookingEndpoints.BOOKING_PARAM_URL.getKey(), bookingID);

		assertions.assertThat(response.getStatusCode()).isEqualTo(404);
		assertions.assertThat(response.asString()).isEqualTo("Not Found");

		assertions.assertAll();
	}

	/* Verify proper status code and error message for invalid booking ID */

	@Test
	public void testD_checkWithInvalidBookingID() {

		SoftAssertions assertions = new SoftAssertions();

		Response response = applicationUtils.deleteBooking(BookingEndpoints.BOOKING_PARAM_URL.getKey(), username,
				password, 0);

		assertions.assertThat(response.getStatusCode()).isEqualTo(405);
		assertions.assertThat(response.asString()).isEqualTo("Method Not Allowed");

		assertions.assertAll();

	}

}
