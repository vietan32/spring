package com.example;

import org.springframework.stereotype.Service;

/**
 * A simple Spring Service. The @Service annotation tells Spring to
 * manage this class as a bean (a component) in its Application Context.
 */
@Service
public class GreetingService {
    public String getGreeting() {
        return "Hello from GreetingService! Dependency Injection works!";
    }
}
