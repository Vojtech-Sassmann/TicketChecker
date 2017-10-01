package cz.tyckouni.TicketChecker.core;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class Main {

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

    private Properties appProperties;

    private void run(String[] args) throws Exception {

        initProperties();

        String urlString = args[0];

        WebDriver driver = initDriver();

        driver.get(urlString);

        List<Tour> tours = parseTours(driver);

        for (Tour tour : tours) {
            System.out.println(tour);
        }

        driver.quit();
    }

    private void initProperties() throws IOException {
        appProperties = new Properties();

        try(InputStream resourceStream = this.getClass().getResourceAsStream("/app.properties")) {
            appProperties.load(resourceStream);
        }
    }

    private WebDriver initDriver() {
        String driverName = appProperties.getProperty("driverName");
        String driverPath = appProperties.getProperty("driverPath");

        System.setProperty(driverName, driverPath);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");

        return new ChromeDriver(chromeOptions);
    }

    private List<Tour> parseTours(WebDriver driver) {
        assert driver != null;

        List<Tour> tours = new ArrayList<>();

        String departColumn = appProperties.getProperty("departColumn");
        String arivalColumn = appProperties.getProperty("arivalColumn");
        String spaceColumn = appProperties.getProperty("spaceColumn");

        List<WebElement> departElements = driver.findElements(By.className(departColumn));
        List<WebElement> arivalElements = driver.findElements(By.className(arivalColumn));
        List<WebElement> spaceElements = driver.findElements(By.className(spaceColumn));
        for (int i = 0; i < departElements.size(); i++) {
            tours.add(new Tour(
                    departElements.get(i).getText(),
                    arivalElements.get(i).getText(),
                    spaceElements.get(i).getText()
            ));
        }

        return tours;
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.run(args);
    }
}
