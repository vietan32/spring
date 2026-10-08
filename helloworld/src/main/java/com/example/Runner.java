package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * A simple component that runs when the application starts.
 * The @Component annotation tells Spring to manage this class.
 */
@Component
public class Runner implements CommandLineRunner {

    // This is the dependency we want to inject
    private final GreetingService greetingService;

    /**
     * Dependency Injection happens here!
     * Spring looks for a GreetingService bean in its context and passes it 
     * to this constructor automatically when creating the Runner bean.
     */
    @Autowired
    public Runner(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n===============================================");
        System.out.println("EXECUTING COMMAND LINE RUNNER:");
        // Using the injected service
        System.out.println(greetingService.getGreeting());
        System.out.println("===============================================\n");
    }
}
