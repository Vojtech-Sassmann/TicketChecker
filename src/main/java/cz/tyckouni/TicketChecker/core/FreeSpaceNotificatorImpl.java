package cz.tyckouni.TicketChecker.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class FreeSpaceNotificatorImpl implements FreeSpaceNotificator {

    private final static Logger log = LoggerFactory.getLogger(FreeSpaceNotificatorImpl.class);

    private AtomicBoolean keepLooking = new AtomicBoolean(false);

    private TourWatch tourWatch;

    private boolean started;

    private Runnable onFreeSpaceAction;

    private Runnable onErrorAction;

    private Runnable onWaitingAction;

    private Runnable checkForFreeSpaceTask = () -> {
        while(keepLooking.get()) {

            Integer currentFreeSpaces;

            synchronized (this) {
                currentFreeSpaces = tourWatch.getCurrentState();
            }

            // On Error
            if (currentFreeSpaces == null) {
                log.error("An error occurred during getting tourWatch state");

                stopService();

                Utils.getExecutor().execute(onErrorAction);
            }

            // On Success
            else if (currentFreeSpaces > 0) {
                log.info("Found free spaces for tour: {}, number of free spaces: {}", tourWatch, currentFreeSpaces);

                stopService();

                Utils.getExecutor().execute(onFreeSpaceAction);

            // On Waiting
            } else {
                log.info("Waiting for free space.");
                if(onWaitingAction != null) {
                    Utils.getExecutor().execute(onWaitingAction);
                }
                try {
                    Thread.sleep(Integer.parseInt(Utils.getProperty("notificatorRefreshTime")));
                } catch (InterruptedException e) {
                    //should not happen
                    throw new AssertionError(e);
                }
            }
        }
    };

    public FreeSpaceNotificatorImpl(TourWatch tourWatch) {
        assert tourWatch != null;

        this.tourWatch = tourWatch;
        this.started = false;
    }

    @Override
    public void setActionOnFreeSpace(Runnable action) {
        this.onFreeSpaceAction = action;
    }

    @Override
    public void setActionOnError(Runnable action) {
        this.onErrorAction = action;
    }

    @Override
    public void setActionOnWaiting(Runnable action) {
        this.onWaitingAction = action;
    }

    @Override
    public void start() {
        if(onFreeSpaceAction == null) {
            throw new IllegalStateException("OnFreeSpaceAction must be set before starting.");
        }
        if(onErrorAction == null) {
            throw new IllegalStateException("OnErrorAction must be set before starting.");
        }
        if(started) {
            throw new IllegalStateException("Notificator already started.");
        }
        started = true;

        try {
            tourWatch.startWatch();
        } catch (InvalidWebElementsReceived e) {
            Utils.getExecutor().execute(onErrorAction);
            return;
        }

        keepLooking.set(true);
        Utils.getExecutor().execute(checkForFreeSpaceTask);
    }

    @Override
    public void stop() {
        this.stopService();
    }

    private void stopService() {
        if(keepLooking.get()) {
            log.info("Service stopped");
            keepLooking.set(false);
            synchronized (this) {
                tourWatch.stopWatch();
            }
        }
    }
}
