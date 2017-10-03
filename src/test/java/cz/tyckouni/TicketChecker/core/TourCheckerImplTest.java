package cz.tyckouni.TicketChecker.core;


import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class TourCheckerImplTest {

    private TourCheckerImpl tourChecker;

    @Before
    public void setUp() {
        //WARNING: This link must be manualy updated
        String url = "https://jizdenky.regiojet.cz/Booking/from/17904006/to/10202002/tarif/REGULAR/departure/20171008/retdep/20171008/return/false?1#search-results";
        this.tourChecker = new TourCheckerImpl(url);
    }

    @Test
    public void getTourFreeSpaces() {
        Tour tour = new Tour("10:45", "16:16");

        assertThat(tourChecker.getTourFreeSpaces(tour)).isGreaterThan(-1).isLessThan(101);
    }

    @Test
    public void getTourFreeSpacesRepeatedCheck() {
        Tour tour1 = new Tour("10:45", "16:16");
        Tour tour2 = new Tour("18:05", "20:45");

        assertThat(tourChecker.getTourFreeSpaces(tour1)).isGreaterThan(-1).isLessThan(101);
        assertThat(tourChecker.getTourFreeSpaces(tour2)).isGreaterThan(-1).isLessThan(101);
    }
}