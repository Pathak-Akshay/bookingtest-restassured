# bookingtest-restassured

This repository contains API tests for restful-booker. Underlying test framework is based on Rest Assured, Maven and Junit.

**Framework Structure**

1. The test code is written in src/test/java folder
2. requests folder consists of POJO classes for Authentication and Booking to be passed as payload
3. utils folder consists of 3 classes - ApplicationUtils, BookingUtils and RestAssuredUtils.
4. RestAssuredUtils class consists of wrappers for HTTP verb calls.
5. ApplicationUtils class consists of utility method for use cases in the APIs, like create booking, update booking, etc
6. BookingUtils class consists of an enum that stores the end points.
7. builder folder consists of BookingDataBuilder class that reads test data from booking.yaml 
8. bookingtest folder consists of test classes used for testing the requests
9. All test classes extend BaseTest class, that consists of code for loading the credentails from config.properties
10. slf4j is used for logging purposes in the framework and AssertJ is used for Soft Assertions.

**Test Scenario Details**

Below test scenarios are covered as part of this framework :

*GetAllBookings Class*

1. Test the request to get all bookings, without any filters
2. Test the request to get all bookings, based on some filters
3. Test booking IDs are not fetched for invalid filters

*PartialUpdateBookings Class*

1. Test that a booking could be successfully updated partially
2. Verify that the booking is actually updated in the system
3. Verify status code and proper error message for invalid booking ID
4. Test that unauthenticated user is not allowed to partially update the booking

*DeleteBookings Class*

1. Verify that unauthenticated user is not allowed to delete any booking
2. Test proper status code and message for successful deletion of booking
3. Verify that the deleted booking no longer exists in the system
4. Verify proper status code and message for invalid booking ID

**Executing Test Cases**

All the test classes use Junit, and should be run as Junit tests
