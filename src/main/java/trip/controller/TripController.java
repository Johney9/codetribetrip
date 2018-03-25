package trip.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trip.model.ErrorResponse;
import trip.model.Trip;
import trip.repository.TripRepository;
import trip.util.TripLengthComparator;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trip")
@Api(value = "tripplanner", description = "REST endpoints for creating and viewing trips")
public class TripController {

    @Autowired
    TripRepository tripRepository;

    @ApiOperation(value = "Search for a Trip with ID")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Trip> trip(@PathVariable long id) {
        return tripRepository.findById(id);
    }

    @ApiOperation(value = "Create a Trip, and return it if successful. Trip destination is validated with Google Places", response = Trip.class)
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/create-trip", method = {RequestMethod.POST})
    public Trip createTrip(@Valid Trip trip) {
        return tripRepository.save(trip);
    }

    @ApiOperation(value = "Find future Trips that start before the given date", response = Iterable.class)
    @RequestMapping(value = "/show-future-trips-starting-before", method = {RequestMethod.GET})
    public Iterable<Trip> showFutureTripsStartingBefore(@RequestParam Date startDate) {
        Date currentDate = new Date(System.currentTimeMillis());
        List<Trip> resultList = tripRepository.findAllByStartDateGreaterThanEqualAndStartDateLessThanEqual(currentDate, startDate);
        resultList.sort(new TripLengthComparator());
        return resultList;
    }

    @ApiOperation(value = "Search for Trips by Destination", response = Iterable.class)
    @RequestMapping(value = "/search", method = {RequestMethod.GET})
    public Iterable<Trip> searchTrips(@RequestParam(value = "destination") String destination) {
        return tripRepository.findAllByDestinationContainingIgnoreCase(destination);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(org.springframework.validation.BindException exception) {

        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(exception.getMessage());

        return ErrorResponse.builder().message(errorMsg).build();
    }
}
