package cz.tyckouni.ticketchecker.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class Utils {

    private final static Logger log = LoggerFactory.getLogger(Utils.class);

    private static Properties appProperties;

    private static Executor executor = Executors.newCachedThreadPool();

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

    public synchronized static Executor getExecutor() {
        return executor;
    }
}
