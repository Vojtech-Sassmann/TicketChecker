package cz.tyckouni.TicketChecker.core;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface FreeSpaceNotificator {

    /**
     * Defines the runnable that will be executed, when a free space will be found.
     *
     * @param action action that will be performed
     */
    void setActionOnFreeSpace(Runnable action);

    /**
     * Defines the runnable that will be executed, when an error occurres.
     *
     * @param action action that will be performed
     */
    void setActionOnError(Runnable action);

    /**
     * Defines the runnable that will be executed, when no free space is available.
     *
     * @param action action that will be performed
     */
    void setActionOnWaiting(Runnable action);

    /**
     * Begines to check for free spaces
     *
     */
    void start();

    /**
     * Stops the check
     */
    void stop();
}
