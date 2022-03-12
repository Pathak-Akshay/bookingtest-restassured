package restfulbooker.bookingtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class GetAllBookings extends BaseTest {

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

	/* Test all the booking IDs could be fetched, when no filters are provided */

	@Test
	public void testA_getAllBookingIDs() {

		SoftAssertions assertions = new SoftAssertions();

		List<Integer> list = new ArrayList<Integer>();

		Response response = applicationUtils.getAllBookings(BookingEndpoints.BOOKING_URL.getKey());

		list = applicationUtils.getBookingsList(response);

		assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		assertions.assertThat(list).contains(bookingID);

		assertions.assertAll();
	}

	/*
	 * Test all the booking IDs could be fetched based on filters provided as query
	 * params
	 */
	@Test
	public void testB_getBookingIDsWithFilters() {

		SoftAssertions assertions = new SoftAssertions();

		List<Integer> list = new ArrayList<Integer>();

		Map<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("firstname", "Alice");
		queryParams.put("totalprice", 5000);

		Response response = applicationUtils.getAllBookingsWithFilters(BookingEndpoints.BOOKING_URL.getKey(),
				queryParams);

		list = applicationUtils.getBookingsList(response);

		assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		assertions.assertThat(list).contains(bookingID);

		assertions.assertAll();
	}

	/* Test booking IDs should not be fetched for invalid query parameters */

	@Test
	public void testC_getBookingIDswithInvalidFilters() {

		SoftAssertions assertions = new SoftAssertions();

		List<Integer> list = new ArrayList<Integer>();

		Map<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("invalid", "invalid");

		Response response = applicationUtils.getAllBookingsWithFilters(BookingEndpoints.BOOKING_URL.getKey(),
				queryParams);

		list = applicationUtils.getBookingsList(response);

		assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		assertions.assertThat(list.size()).isEqualTo(0);

		assertions.assertAll();
	}

	/* Delete the booking created */

	@AfterClass
	public static void tearDown() {

		applicationUtils.deleteBooking(BookingEndpoints.BOOKING_PARAM_URL.getKey(), username, password, bookingID);
	}

}
