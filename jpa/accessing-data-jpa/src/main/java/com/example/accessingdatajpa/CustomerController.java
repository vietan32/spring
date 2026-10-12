package com.example.accessingdatajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	@Autowired
	private CustomerRepository repository;

	@RequestMapping(method = RequestMethod.GET)
	public List<Customer> getAllCustomers() {
		return repository.findAll();
	}

	@GetMapping(value = "/{id}")
	public Customer getCustomer(@PathVariable("id") long id) {
		return repository.findById(id);
	}

	@PostMapping()
	public ResponseEntity<String> createCustomer(@RequestBody Customer newCustomer) {
		Customer savedCustomer = repository.save(newCustomer);
		return new ResponseEntity<>("Customer created with ID: " + savedCustomer.getId(),
				HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") long id) {
		if (!repository.existsById(id)) {
			return new ResponseEntity<>("Customer not found with ID: " + id, HttpStatus.NOT_FOUND);
		}
		repository.deleteById(id);
		return new ResponseEntity<>("Deleted customer with ID: " + id, HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Customer>> getCustomersByLastName(@RequestParam("lastName") String lastName) {
		List<Customer> customers = repository.findByLastName(lastName);
		if (customers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}
}
