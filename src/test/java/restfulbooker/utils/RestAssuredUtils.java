package restfulbooker.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import restfulbooker.requests.AuthenticationRequest;
import restfulbooker.utils.BookingUtils.BookingEndpoints;

public class RestAssuredUtils {
	
	private final static Logger log = LoggerFactory.getLogger(RestAssuredUtils.class);
	
	/* Generates generic RequestSpecificaion object for all the request methods
	 * 
	 * @return RequestSpecification
	 * 
	 */ 
	public RequestSpecification getRequestSpecification() {

		RequestSpecification requestSpecification = RestAssured.given().baseUri(BookingEndpoints.BASE_URI.getKey())
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.filter(new RequestLoggingFilter())
				.filter(new ResponseLoggingFilter());

		return requestSpecification;
	}

	/* Generates RequestSpecification for calls that require auth 
	 * 
	 * @param username
	 * @param password
	 * @return RequestSpecification
	 */
	
	public RequestSpecification getRequestSpecificationWithAuth(String username, String password) {

		RequestSpecification requestSpecification = getRequestSpecification();
		requestSpecification.header("Cookie", "token=" + tokenAuthentication(username, password));

		return requestSpecification;
	}

	/* Get token for authentication based on username and password 
	 * 
	 * @param username
	 * @param password
	 */
	
	public String tokenAuthentication(String username, String password) {

		Response response;
		String jsonString = "";

		AuthenticationRequest auth = new AuthenticationRequest(username, password);

		try {

			response = postRequest(BookingEndpoints.AUTHENTICATION_URL.getKey().trim(), auth);
			jsonString = response.asString();
		}

		catch (Exception e) {

			log.error("Token Authentication failed with error " + e.getStackTrace());
		}

		return JsonPath.from(jsonString).get("token");
	}

	/* POST request with endpoint and without any body
	 * 
	 *@param endPoint
	 *@return Response
	 */
	
	public Response postRequest(String endPoint) {
		
		return getRequestSpecification()
				.when()
				.post(endPoint)
				.then()
				.extract()
				.response();
	}
	
	/* POST request with endpoint and body
	 * 
	 * @param endPoint
	 * @param body
	 * @return Response*/
	
	public Response postRequest(String endPoint, Object body) {
		
		return getRequestSpecification().
				body(body).
				when()
				.post(endPoint)
				.then()
				.extract()
				.response();
	}
	
	/* GET request with endpoint
	 * 
	 * @param endPoint
	 * @return Response
	 */
	
	public Response getRequest(String endPoint) {
		
		return getRequestSpecification()
				.when()
				.get(endPoint)
				.then()
				.extract()
				.response();
	}
	
	/* GET request with endpoint and path parameter 
	 * 
	 * @param endPoint
	 * @param pathParam
	 * @return Response
	 */
	public Response getRequest(String endPoint, int pathParam) {
		
		return getRequestSpecification()
				.when()
				.get(endPoint, pathParam)
				.then()
				.extract()
				.response();
	}
	
	/* GET request with endpoint and query parameters 
	 * 
	 * @param endPoint
	 * @param queryParams
	 * @return Response
	 */
	public Response getRequest(String endPoint, Map<String,?> queryParams) {
		
		return getRequestSpecification()
				.when()
				.queryParams(queryParams)
				.get(endPoint)
				.then()
				.extract()
				.response();
				
	}
	
	/* PUT request with endpoint and path parameters 
	 * 
	 * @param username
	 * @param password
	 * @param endPoint
	 * @param body
	 * @param pathParam
	 * @return Response
	 */
	public Response putRequest(String username, String password, String endPoint, Object body, int pathParam) {
		
		return getRequestSpecificationWithAuth(username, password)
				.body(body)
				.when()
				.put(endPoint, pathParam)
				.then()
				.extract()
				.response();
	}
	
	/* PATCH request with endpoint and path parameters 
	 * 
	 * @param username
	 * @param password
	 * @param endPoint
	 * @param body
	 * @param pathParam
	 * @return Response
	 */
    public Response patchRequest(String username, String password, String endPoint, Object body, int pathParam) {
		
		return getRequestSpecificationWithAuth(username, password).
				body(body)
				.when().
				patch(endPoint, pathParam)
				.then()
				.extract()
				.response();
	}
    
    /* DELETE request with endpoint and path parameters 
	 * 
	 * @param username
	 * @param password
	 * @param endPoint
	 * @param pathParam
	 * @return Response
	 */
    public Response deleteRequest(String username, String password, String endPoint, int pathParam) {
		
		return getRequestSpecificationWithAuth(username, password)
				.when()
				.delete(endPoint, pathParam)
				.then()
				.extract()
				.response();
	}

}
