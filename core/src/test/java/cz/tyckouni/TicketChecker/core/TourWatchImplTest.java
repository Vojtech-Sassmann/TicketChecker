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
        Tour tour = new Tour("18:05", "18:55");
        this.tourWatch = new TourWatchImpl(url, tour);
    }

    @Test
    public void startTest() throws Exception {
        tourWatch.startWatch();
        Thread.sleep(60000);
    }
}
