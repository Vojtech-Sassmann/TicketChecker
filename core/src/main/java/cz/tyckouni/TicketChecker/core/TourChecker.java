package cz.tyckouni.TicketChecker.core;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface TourChecker {

    /**
     * Try to find number of free spaces for given tour.
     *
     * @param tour searched tour
     * @return count of free spaces or null if tour was not found
     * @throws InvalidWebElementsReceived when the number of arrivals, departs and free spaces are not the same
     */
    Integer getTourFreeSpaces(Tour tour) throws InvalidWebElementsReceived;

    void quitWebDriver();
}
