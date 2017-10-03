package cz.tyckouni.TicketChecker.core;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface TourWatch {

    void startWatch(Tour tour);

    Integer getCurrentState();

    void stopWatch();
}
