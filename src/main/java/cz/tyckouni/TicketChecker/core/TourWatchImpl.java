package cz.tyckouni.TicketChecker.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class TourWatchImpl implements TourWatch {

    private static Executor executor = Executors.newCachedThreadPool();

    private final static Logger log = LoggerFactory.getLogger(TourWatchImpl.class);

    private Runnable watcher = () -> {
        try {
            do {
                log.info("watch started");
                try {
                    synchronized (this) {
                        Integer i = this.tourChecker.getTourFreeSpaces(this.tour);
                        log.info("New state received: " + i);
                        this.state = new AtomicInteger(i);
                        log.info("New state set: " + this.state);
                    }
                } catch (Exception e) {
                    log.error("an exception", e);
                }
                try {
                    Thread.sleep(Integer.parseInt(Utils.getProperty("refreshTime")));
                } catch (InterruptedException e) {
                    //should not happen
                    throw new RuntimeException(e);
                }
            } while(this.keepWatching());
        } catch (Exception e) {
            log.error("an exception outside", e);
        }
    };

    private TourChecker tourChecker;

    private AtomicInteger state;

    private Tour tour;

    private AtomicBoolean keepWatching = new AtomicBoolean(false);

    public TourWatchImpl(String url) {
        if(url == null) {
            throw new IllegalArgumentException("Url can not be null");
        }
        tourChecker = new TourCheckerImpl(url);
    }

    @Override
    public void startWatch(Tour tour) {
        if(tour == null) {
            throw new IllegalArgumentException("Tour argument can not be null.");
        }
        this.tour = tour;

        this.state = new AtomicInteger(this.tourChecker.getTourFreeSpaces(tour));
        log.info("initial state: {}", this.state);
        this.keepWatching = new AtomicBoolean(true);

        //executor.execute(watcher);
        Thread t = new Thread(watcher);
        t.setDaemon(false);
        t.start();
    }

    @Override
    public Integer getCurrentState() {
        return this.state.get();
    }

    @Override
    public void stopWatch() {
        synchronized (this) {
            tourChecker.quitWebDriver();
        }
    }

    synchronized private boolean keepWatching() {
        return this.keepWatching.get();
    }

}
