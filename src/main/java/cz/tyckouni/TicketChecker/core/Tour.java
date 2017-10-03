package cz.tyckouni.TicketChecker.core;

import com.sun.istack.internal.NotNull;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class Tour {
    private String arival;
    private String depart;
    private int spaces;

    public Tour(@NotNull String depart,@NotNull String arival) {
        assert depart != null;
        assert arival != null;

        this.arival = arival;
        this.depart = depart;
    }

    public Tour(@NotNull String depart, @NotNull String arival, String spaces) {
        assert depart != null;
        assert arival != null;

        this.arival = arival;
        this.depart = depart;

        spaces = spaces.replaceAll("\\s*", "");
        if(spaces.matches("[0-9]+")) {
            this.spaces = Integer.parseInt(spaces.replaceAll("\\s*", ""));
        } else {
            throw new IllegalArgumentException("Invalid spaces argument: '" + spaces + "'");
        }
    }

    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }

    public String getArival() {
        return arival;
    }

    public String getDepart() {
        return depart;
    }

    public int getSpaces() {
        return spaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tour tour = (Tour) o;

        return arival.equals(tour.arival) && depart.equals(tour.depart);
    }

    @Override
    public int hashCode() {
        int result = arival.hashCode();
        result = 31 * result + depart.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Arrival: " + arival + ", Departure: " + depart + ", Spaces: " + spaces;
    }
}
