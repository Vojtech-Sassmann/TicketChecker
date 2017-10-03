package cz.tyckouni.TicketChecker.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class TourWatchImpl implements TourWatch {

    private final static Logger log = LoggerFactory.getLogger(TourWatchImpl.class);

    private AtomicBoolean keepWatching = new AtomicBoolean(false);

    private TourChecker tourChecker;

    private AtomicInteger state;

    private Tour tour;

    private Runnable watcher = () -> {
        while (keepWatching.get()) {
            try {
                Integer i;
                synchronized (this) {
                    i = tourChecker.getTourFreeSpaces(tour);
                    log.info("New state received: " + i);
                }
                state = new AtomicInteger(i);
                log.info("New state set: " + state);
            } catch (InvalidWebElementsReceived e) {
                log.error("An exception happened during getting new state, previous state was: {}", state, e);
                keepWatching.set(false);
                state = null;
            }
            try {
                Thread.sleep(Integer.parseInt(Utils.getProperty("webDriverRefreshTime")));
            } catch (InterruptedException e) {
                //should not happen
                throw new RuntimeException(e);
            }
        }
    };

    public TourWatchImpl(String url, Tour tour) {
        if(url == null) {
            throw new IllegalArgumentException("Url can not be null");
        }
        if(tour == null) {
            throw new IllegalArgumentException("Tour can not be null");
        }
        tourChecker = new TourCheckerImpl(url);
        this.tour = tour;
    }

    @Override
    public void startWatch() throws InvalidWebElementsReceived{
        if(tour == null) {
            throw new IllegalArgumentException("Tour argument can not be null.");
        }
        keepWatching.set(true);

        log.info("Watch started");

        state = new AtomicInteger(tourChecker.getTourFreeSpaces(tour));

        Utils.getExecutor().execute(watcher);
    }

    @Override
    public Integer getCurrentState() {
        return state.get();
    }

    @Override
    public void stopWatch() {
        log.info("Tour Watch stopped");
        keepWatching.set(false);
        synchronized (this) {
            tourChecker.quitWebDriver();
        }
    }
}
