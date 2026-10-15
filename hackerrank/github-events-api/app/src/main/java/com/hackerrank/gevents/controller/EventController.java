package com.hackerrank.gevents.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.gevents.model.Event;
import com.hackerrank.gevents.repository.EventRepository;

@RestController
public class EventController {
	@Autowired
	EventRepository eventRepository;

	@PostMapping("/events")
	public ResponseEntity<Event> create(@RequestBody Event event) {
		event.setId(null);
		if (!isValidType(event.getType())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Event saved = eventRepository.save(event);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@GetMapping("/events")
	public ResponseEntity<List<Event>> getAllSortedById() {
		List<Event> events = eventRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@GetMapping("/repos/{repoId}/events")
	public ResponseEntity<List<Event>> getEventsByRepo(@PathVariable long repoId) {
		List<Event> events = eventRepository.findByRepoIdOrderByIdAsc(repoId);
		if (events.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@GetMapping("/users/{userId}/events")
	public ResponseEntity<List<Event>> getEventsByUserId(@PathVariable long userId) {
		List<Event> events = eventRepository.findByActorIdOrderByIdAsc(userId);
		if (events.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@GetMapping("/events/{eventId}")
	public ResponseEntity<Event> getByEventId(@PathVariable long eventId) {
		Event events = eventRepository.findById(eventId);
		if (events == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	private boolean isValidType(String type) {
		if (type == null)
			return false;
		return type.equals("PushEvent") || type.equals("ReleaseEvent") || type.equals("WatchEvent");
	}
}
