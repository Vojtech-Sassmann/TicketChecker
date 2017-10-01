package cz.tyckouni.TicketChecker.core;

/**
 * Created by Vectoun on 1. 10. 2017.
 */
public class Tour {
    private String arival;
    private String depart;
    private int spaces;

    public Tour(String depart, String arival, String spaces) {
        this.arival = arival;
        this.depart = depart;
        if(!spaces.matches("-")) {
            this.spaces = Integer.parseInt(spaces.replaceAll("\\s*", ""));
        } else {
            this.spaces = 0;
        }
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

        if (spaces != tour.spaces) return false;
        if (arival != null ? !arival.equals(tour.arival) : tour.arival != null) return false;
        return depart != null ? depart.equals(tour.depart) : tour.depart == null;
    }

    @Override
    public int hashCode() {
        int result = arival != null ? arival.hashCode() : 0;
        result = 31 * result + (depart != null ? depart.hashCode() : 0);
        result = 31 * result + spaces;
        return result;
    }

    @Override
    public String toString() {
        return "Arrival: " + arival + ", Departure: " + depart + ", Spaces: " + spaces;
    }
}
