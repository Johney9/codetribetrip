package trip.util;

import trip.model.Trip;

import java.util.Comparator;

public class TripLengthComparator implements Comparator<Trip> {

    @Override
    public int compare(Trip trip, Trip t1) {

        Long trip1Duration = trip.getEndDate().getTime() - trip.getStartDate().getTime();
        Long trip2Duration = t1.getEndDate().getTime() - t1.getStartDate().getTime();

        return trip1Duration.compareTo(trip2Duration);
    }
}
