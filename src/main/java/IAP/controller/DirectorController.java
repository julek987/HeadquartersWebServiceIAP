package IAP.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/director")
public class DirectorController {

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> adminDashboard(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("username", authentication.getName());
        response.put("role", "Director");
        response.put("message", "Welcome to Admin Dashboard");
        return ResponseEntity.ok(response);
    }
}
