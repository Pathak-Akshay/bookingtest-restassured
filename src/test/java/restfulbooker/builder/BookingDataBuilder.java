package restfulbooker.builder;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import restfulbooker.requests.CreateBookingRequest;

public class BookingDataBuilder {

	private final Logger log = LoggerFactory.getLogger(BookingDataBuilder.class);

	/*
	 * Generates POJO object for CreateBookingRequest based on data fetched from
	 * YAML
	 */
	public CreateBookingRequest generateBookingData() {

		try {

			File src = new File(System.getProperty("user.dir") + "//resources//booking.yaml");
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			CreateBookingRequest booking = mapper.readValue(src, CreateBookingRequest.class);

			log.info("CreateBookingRequest object created based on yam input");
			return booking;
		}

		catch (Exception e) {

			log.error("Error while reading data from yaml, failed with exception " + e.getStackTrace());
			return null;
		}
	}

}
