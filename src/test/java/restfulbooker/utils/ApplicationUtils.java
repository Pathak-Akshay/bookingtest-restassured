package restfulbooker.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restfulbooker.requests.CreateBookingRequest;

public class ApplicationUtils {
	
	private final Logger log = LoggerFactory.getLogger(ApplicationUtils.class);

	RestAssuredUtils restAssuredUtils = new RestAssuredUtils();

	/* Creates a new booking and returns Response object 
	 * 
	 * @param request
	 * @param endPoint
	 * @return Response
	 */
	public Response createBooking(CreateBookingRequest request, String endPoint) {

		Response response;

		try {

			response = restAssuredUtils.postRequest(endPoint, request);
			log.info("Request logged for " + endPoint);
			return response;
		}

		catch (Exception e) {

			log.error("Exception while creating booking, failed with error " + e.getStackTrace());
			return null;
		}
	}

	/* Returns bookingID from the JSON response 
	 * 
	 * @param response
	 * @return bookingID 
	 */
	public int getBookingID(Response response) {

		int bookingID = 0;

		if (response != null) {

			String jsonString = response.asString();
			bookingID = JsonPath.from(jsonString).get("bookingid");
		}

		return bookingID;
	}

	/* Returns all bookings from the getBookings end point 
	 * 
	 * @param endPoint
	 * @return Response 
	 */
	public Response getAllBookings(String endPoint) {

		Response response;

		try {

			response = restAssuredUtils.getRequest(endPoint);
			log.info("Request logged for " + endPoint);
			return response;
		}

		catch (Exception e) {

			log.error("Exception while fetching all bookings, failed with error " + e.getStackTrace());
			return null;
		}
	}

	/* Returns all bookings based on query parameters
	 * 
	 * @param endPoint
	 * @param params
	 * @return Response 
	 */
	public Response getAllBookingsWithFilters(String endPoint, Map<String, ?> params) {

		Response response;

		try {

			response = restAssuredUtils.getRequest(endPoint, params);
			log.info("Request logged for " + endPoint);
			return response;
		}

		catch (Exception e) {

			log.error("Exception while fetching all bookings, failed with error " + e.getStackTrace());
			return null;
		}
	}

	/* Returns all bookings in List format
	 * 
	 * @param response
	 * @return List 
	 */
	public List<Integer> getBookingsList(Response response) {

		List<Integer> list = new ArrayList<Integer>();

		if (response != null) {

			List<String> jsonResponse = response.jsonPath().getList("$");

			for (int i = 0; i < jsonResponse.size(); i++) {

				String id = response.jsonPath().getString("bookingid[" + i + "]");
				list.add(Integer.parseInt(id));
			}
		}

		return list;
	}

	/* Returns Response object from getBookingByID 
	 * 
	 * @param endPoint
	 * @param bookingID
	 * @return Response 
	 */
	public Response getBookingByID(String endPoint, int bookingID) {

		Response response;

		try {

			response = restAssuredUtils.getRequest(endPoint, bookingID);
			log.info("Request logged for " + endPoint);
			return response;
		}

		catch (Exception e) {

			log.error("Exception while fetching booking by ID, failed with error " + e.getStackTrace());
			return null;
		}
	}

	/* Returns Response object from partialUpdateBooking  
	 * 
	 * @param endPoint
	 * @param request
	 * @param username
	 * @param password
	 * @param bookingID
	 * @return Response 
	 */
	public Response partialUpdateBooking(String endPoint, Object request, String username, String password,
			int bookingID) {

		Response response;

		try {

			response = restAssuredUtils.patchRequest(username, password, endPoint, request, bookingID);
			log.info("Request logged for " + endPoint);
			return response;
		}

		catch (Exception e) {

			log.error("Exception while partially updating booking, failed with error " + e.getStackTrace());
			return null;
		}
	}

	/* Returns Response object from deleteBooking  
	 * 
	 * @param endPoint
	 * @param username
	 * @param password
	 * @param bookingID
	 * @return Response 
	 */
	public Response deleteBooking(String endPoint, String username, String password, int bookingID) {

		Response response;

		try {

			response = restAssuredUtils.deleteRequest(username, password, endPoint, bookingID);
			log.info("Request logged for " + endPoint);
			return response;
		}

		catch (Exception e) {

			log.error("Exception while deleting booking, failed with error " + e.getStackTrace());
			return null;
		}
	}
}
