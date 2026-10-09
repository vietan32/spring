package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@SpringBootApplication
public class MyApplication {

	@GetMapping("/api/home")
	public ResponseEntity<String> home() {
		return new ResponseEntity<>("Hello World!", HttpStatus.OK);
	}

	public static void main(String[] args) {
		SpringApplication.run(MyApplication.class, args);
	}
}
