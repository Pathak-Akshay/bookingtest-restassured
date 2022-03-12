package restfulbooker.bookingtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {

	protected static Properties properties;

	protected static String username;

	protected static String password;

	private final static Logger log = LoggerFactory.getLogger(BaseTest.class);

	public static void initialize() {

		loadProperties();

		username = properties.getProperty("ADMIN_USER");

		password = properties.getProperty("ADMIN_PASSWORD");

	}

	public static void loadProperties() {

		File src = new File(System.getProperty("user.dir") + "//config//config.properties");

		try {

			FileInputStream fis = new FileInputStream(src);

			properties = new Properties();

			properties.load(fis);
			
			log.info("Properties loaded from config");

		} catch (IOException e) {

			log.error(e.getMessage());
		}
	}
}
