package cz.tyckouni.TicketChecker.core;

import com.sun.istack.internal.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class TourCheckerImpl implements TourChecker {

    private final static Logger log = LoggerFactory.getLogger(TourCheckerImpl.class);

    private String url;

    private static WebDriver webDriver;

    // init driver
    static {
        String driverName = Utils.getProperty("driverName");
        String driverPath = Utils.getProperty("driverPath");

        System.setProperty(driverName, driverPath);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");

        webDriver = new ChromeDriver(chromeOptions);
    }

    public TourCheckerImpl(@NotNull String url) {
        if(url == null || url.isEmpty()) {
            throw new IllegalArgumentException("url can not be null or empty");
        }
        this.url = url;
    }

    @Override
    public void quitWebDriver() {
        webDriver.quit();
    }

    @Override
    public Integer getTourFreeSpaces(Tour tour) throws InvalidWebElementsReceived {
        try {
            webDriver.get(url);
        } catch (Exception e) {
            log.error("error");
        }
        log.debug("Get tour free space called");
        List<Tour> tours = parseTours(webDriver);

        //webDriver.quit();

        if(tours.contains(tour)) {
            return tours.get(tours.indexOf(tour)).getSpaces();
        } else {
            return null;
        }
    }

    private List<Tour> parseTours(WebDriver driver) throws InvalidWebElementsReceived {
        assert driver != null;

        List<Tour> tours = new ArrayList<>();

        String departColumn = Utils.getProperty("departColumn");
        String arivalColumn = Utils.getProperty("arivalColumn");
        String spaceColumn = Utils.getProperty("spaceColumn");

        List<WebElement> departElements = driver.findElements(By.className(departColumn));
        List<WebElement> arivalElements = driver.findElements(By.className(arivalColumn));
        List<WebElement> spaceElements = driver.findElements(By.className(spaceColumn));

        List<String> departTimes = getStringsFromWebElements(departElements);
        List<String> arivalTimes = getStringsFromWebElements(arivalElements);
        List<String> spaces = getStringsFromWebElements(spaceElements);

        if (!(departTimes.size() == arivalTimes.size() &&
              departTimes.size() == spaces.size())) {
            log.error("Invalid web elements received: departs={}, arrivals={}, free spaces={}",
                    departTimes, arivalTimes, spaces);
            throw new InvalidWebElementsReceived("Invalid web elements received");
        }

        for (int i = 0; i < departElements.size(); i++) {
            log.debug("Obtained a tour from site: arival: '{}', depart: '{}', spaces: '{}'",
                    arivalElements.get(i).getText(),
                    departElements.get(i).getText(),
                    spaceElements.get(i).getText());
            tours.add(new Tour(
                    departElements.get(i).getText(),
                    arivalElements.get(i).getText(),
                    spaceElements.get(i).getText()
            ));
        }

        return tours;
    }

    private List<String> getStringsFromWebElements(List<WebElement> elements) {
        List<String> strings = new ArrayList<>();
        elements.forEach((depart) -> {
            if (!depart.getText().replaceAll("\\s+", "").isEmpty()) {
                strings.add(depart.getText());
            }
        });
        return strings;
    }
}
