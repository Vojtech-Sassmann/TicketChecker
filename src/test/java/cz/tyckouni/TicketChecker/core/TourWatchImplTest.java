package cz.tyckouni.TicketChecker.core;


import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class TourWatchImplTest {

    private TourWatchImpl tourWatch;

    @Before
    public void setUp() {
        //WARNING: This link must be manualy updated
        String url = "https://jizdenky.regiojet.cz/Booking/from/17904006/to/1060226000/tarif/REGULAR/departure/20171008/retdep/20171008/return/false?2#search-results";
        this.tourWatch = new TourWatchImpl(url);
    }

    @Test
    public void startTest() throws Exception {
        Tour tour = new Tour("18:05", "18:55");

        tourWatch.startWatch(tour);
        Thread.sleep(60000);
    }
}
