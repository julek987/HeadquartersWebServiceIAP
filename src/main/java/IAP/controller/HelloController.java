package IAP.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow React app to access this endpoint
public class HelloController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello from Spring Boot!";
    }

    @GetMapping("/api/welcome")
    public String welcome() {
        return "Welcome to your Spring Boot + React application!";
    }
}