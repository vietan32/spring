package org.example.repository;

import org.example.model.Flight;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
  Optional<Flight> findById(long id);

  List<Flight> findByOrigin(String origin);
}
