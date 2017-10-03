package cz.tyckouni.TicketChecker.core;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface TourWatch {

    void startWatch() throws InvalidWebElementsReceived;

    /**
     *
     * @return the current state, or null when an error happend and the tourwatch stoped searching
     */
    Integer getCurrentState();

    void stopWatch();
}
