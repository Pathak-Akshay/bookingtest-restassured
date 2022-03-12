package restfulbooker.utils;

public class BookingUtils {
	
	public enum BookingEndpoints {
		
		BASE_URI("https://restful-booker.herokuapp.com"),
		
		AUTHENTICATION_URL("/auth"),
		
		BOOKING_URL("/booking"),
		
		BOOKING_PARAM_URL("/booking/{param}");
		
		private String key;
		
		BookingEndpoints(String key) {
			
			this.key = key;
		}
		
		public String getKey() {
			
			return key;
		}
	}

}
