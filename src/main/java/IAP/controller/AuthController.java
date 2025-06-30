package IAP.controller;

import IAP.model.Address;
import IAP.model.AppUser;
import IAP.model.DTO.AppUserDTO;
import IAP.service.AddressService;
import IAP.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3001") // Add this for React frontend
public class AuthController {

    private final AppUserService appUserService;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AppUserService appUserService, AddressService addressService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
    }

    // NEW LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            // Find user by login
            List<AppUser> allUsers = appUserService.listAppUsers();
            AppUser user = allUsers.stream()
                    .filter(u -> u.getLogin().equals(username))
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }

            // Check if user is active
            if (!user.isActive()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Account is deactivated");
            }

            // Verify password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }

            // Create response with user info and role
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", "jwt-token-" + System.currentTimeMillis()); // Simple token for now
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getLogin(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "email", user.getEmail(),
                    "role", user.getRole()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AppUserDTO appUserDTO) {
        try {
            // Validation
            if (appUserDTO.firstName == null || appUserDTO.firstName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("First name is required");
            }
            if (appUserDTO.lastName == null || appUserDTO.lastName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Last name is required");
            }
            if (appUserDTO.email == null || appUserDTO.email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (appUserDTO.login == null || appUserDTO.login.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            if (appUserDTO.password == null || appUserDTO.password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            if (appUserDTO.addressId == null || appUserDTO.addressId <= 0) {
                return ResponseEntity.badRequest().body("Please select an address");
            }

            // Check if login already exists
            List<AppUser> existingUsers = appUserService.listAppUsers();
            boolean loginExists = existingUsers.stream()
                    .anyMatch(user -> user.getLogin().equals(appUserDTO.login));

            if (loginExists) {
                return ResponseEntity.badRequest().body("Username already exists!");
            }

            // Check if email already exists
            boolean emailExists = existingUsers.stream()
                    .anyMatch(user -> user.getEmail().equals(appUserDTO.email));

            if (emailExists) {
                return ResponseEntity.badRequest().body("Email already exists!");
            }

            // Create new AppUser
            AppUser appUser = new AppUser();
            appUser.setActive(true);
            appUser.setFirstName(appUserDTO.firstName);
            appUser.setMiddleName(appUserDTO.middleName);
            appUser.setLastName(appUserDTO.lastName);
            appUser.setEmail(appUserDTO.email);
            appUser.setPhoneNumber(appUserDTO.phoneNumber);
            appUser.setRole(appUserDTO.role != null ? appUserDTO.role : 0);
            appUser.setLogin(appUserDTO.login);
            appUser.setPassword(passwordEncoder.encode(appUserDTO.password));

            // Set address
            if (appUserDTO.addressId > 0) {
                Address address = addressService.getAddress(appUserDTO.addressId);
                if (address != null) {
                    appUser.setAddress(address);
                } else {
                    return ResponseEntity.badRequest().body("Selected address not found!");
                }
            }

            // Set timestamps
            LocalDateTime now = LocalDateTime.now();
            appUser.setCreatedAt(now);
            appUser.setModifiedAt(now);

            // Save user
            appUserService.addAppUser(appUser);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Registration successful!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAddresses() {
        List<Address> addresses = addressService.listAddresses();
        return ResponseEntity.ok(addresses);
    }
}
