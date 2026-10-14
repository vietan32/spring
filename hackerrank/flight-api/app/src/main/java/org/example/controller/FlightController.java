package org.example.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.example.model.Flight;
import org.example.repository.FlightRepository;

@RestController
@RequestMapping(value = "/flight")
public class FlightController {
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FlightController.class);

  @Autowired
  private FlightRepository flightRepository;

  @PostMapping()
  public ResponseEntity<Flight> create(@RequestBody Flight flight) {
    Flight saved = flightRepository.save(flight);
    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @GetMapping()
  public ResponseEntity<List<Flight>> getFlights(@RequestParam(required = false) String origin,
      @RequestParam(required = false) String orderBy) {
    List<Flight> flights = origin != null ? flightRepository.findByOrigin(origin) : flightRepository.findAll();
    if (orderBy != null && orderBy.equals("destination")) {
      flights.sort((a, b) -> {
        if (a.getDestination().equals(b.getDestination())) {
          return a.getId() < b.getId() ? -1 : 1;
        }
        return a.getDestination().compareTo(b.getDestination());
      });
    } else if (orderBy != null && orderBy.equals("-destination")) {
      flights.sort((a, b) -> {
        if (a.getDestination().equals(b.getDestination())) {
          return a.getId() < b.getId() ? -1 : 1;
        }
        return b.getDestination().compareTo(a.getDestination());
      });
    }
    return new ResponseEntity<>(flights, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Flight> getFlightById(@PathVariable long id) {
    Optional<Flight> flight = flightRepository.findById(id);
    if (flight.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(flight.get(), HttpStatus.OK);
  }
}
