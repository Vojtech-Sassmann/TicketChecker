package cz.tyckouni.TicketChecker.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class Utils {

    private final static Logger log = LoggerFactory.getLogger(Utils.class);

    private static Properties appProperties;

    static {
        appProperties = new Properties();

        try(InputStream resourceStream = Utils.class.getResourceAsStream("/app.properties")) {
            appProperties.load(resourceStream);
        } catch (IOException e) {
            throw new InvalidStateException("Failed to load app properties", e);
        }
    }

    public static String getProperty(String propertyName) {
        return appProperties.getProperty(propertyName);
    }

    private WebDriver initDriver() {
        String driverName = appProperties.getProperty("driverName");
        String driverPath = appProperties.getProperty("driverPath");

        System.setProperty(driverName, driverPath);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");

        return new ChromeDriver(chromeOptions);
    }
}
