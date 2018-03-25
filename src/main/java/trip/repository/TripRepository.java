package trip.repository;

import org.springframework.data.repository.CrudRepository;
import trip.model.Trip;

import java.sql.Date;
import java.util.List;

public interface TripRepository extends CrudRepository<Trip, Long> {

    List<Trip> findAllByDestinationContainingIgnoreCase(String destinaton);

    List<Trip> findAllByStartDateGreaterThanEqualAndStartDateLessThanEqual(Date currentDate, Date startDate);
}