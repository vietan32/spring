package com.example.accessingdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ActiveProfiles("test")
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerRepository customerRepository;

	private Customer createCustomerWithId(Long id, String firstName, String lastName) {
		Customer customer = new Customer(firstName, lastName);
		ReflectionTestUtils.setField(customer, "id", id);
		return customer;
	}

	@Test
	void shouldReturnAllCustomers() throws Exception {
		Customer c1 = createCustomerWithId(1L, "Jack", "Bauer");
		Customer c2 = createCustomerWithId(2L, "Chloe", "O'Brian");

		given(customerRepository.findAll()).willReturn(List.of(c1, c2));

		mockMvc.perform(get("/api/customers"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].firstName").value("Jack"))
				.andExpect(jsonPath("$[0].lastName").value("Bauer"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].firstName").value("Chloe"))
				.andExpect(jsonPath("$[1].lastName").value("O'Brian"));

		verify(customerRepository).findAll();
	}

	@Test
	void shouldReturnCustomerById() throws Exception {
		Customer customer = createCustomerWithId(1L, "Jack", "Bauer");
		given(customerRepository.findById(1L)).willReturn(customer);

		mockMvc.perform(get("/api/customers/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.firstName").value("Jack"))
				.andExpect(jsonPath("$.lastName").value("Bauer"));

		verify(customerRepository).findById(1L);
	}

	@Test
	void shouldCreateCustomer() throws Exception {
		Customer savedCustomer = createCustomerWithId(10L, "Tony", "Almeida");
		given(customerRepository.save(any(Customer.class))).willReturn(savedCustomer);

		String requestJson = """
				{
					"firstName": "Tony",
					"lastName": "Almeida"
				}
				""";

		mockMvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isCreated())
				.andExpect(content().string("Customer created with ID: 10"));

		verify(customerRepository).save(any(Customer.class));
	}

	@Test
	void shouldDeleteCustomerWhenExists() throws Exception {
		given(customerRepository.existsById(1L)).willReturn(true);

		mockMvc.perform(delete("/api/customers/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(content().string("Deleted customer with ID: 1"));

		verify(customerRepository).existsById(1L);
		verify(customerRepository).deleteById(1L);
	}

	@Test
	void shouldReturnNotFoundWhenDeletingNonExistentCustomer() throws Exception {
		given(customerRepository.existsById(99L)).willReturn(false);

		mockMvc.perform(delete("/api/customers/{id}", 99L))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Customer not found with ID: 99"));

		verify(customerRepository).existsById(99L);
		verify(customerRepository, never()).deleteById(99L);
	}

	@Test
	void shouldFindCustomersByLastName() throws Exception {
		Customer customer = createCustomerWithId(1L, "Jack", "Bauer");
		given(customerRepository.findByLastName("Bauer")).willReturn(List.of(customer));

		mockMvc.perform(get("/api/customers/search")
						.param("lastName", "Bauer"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].firstName").value("Jack"))
				.andExpect(jsonPath("$[0].lastName").value("Bauer"));

		verify(customerRepository).findByLastName("Bauer");
	}

	@Test
	void shouldReturnNoContentWhenNoCustomersMatchLastName() throws Exception {
		given(customerRepository.findByLastName("Unknown")).willReturn(Collections.emptyList());

		mockMvc.perform(get("/api/customers/search")
						.param("lastName", "Unknown"))
				.andExpect(status().isNoContent());

		verify(customerRepository).findByLastName("Unknown");
	}
}
