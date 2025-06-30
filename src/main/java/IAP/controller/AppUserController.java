package IAP.controller;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Address;
import IAP.model.AppUser;
import IAP.model.Branch;
import IAP.model.DTO.AppUserDTO;
import IAP.service.AddressService;
import IAP.service.AppUserService;
import IAP.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3001") // Add CORS support
public class AppUserController {

    private final AppUserService appUserService;
    private final AddressService addressService;
    private final BranchService branchService;
    private final PasswordEncoder passwordEncoder; // ADD THIS

    @Autowired
    public AppUserController(AppUserService appUserService, AddressService addressService,
                             BranchService branchService, PasswordEncoder passwordEncoder) { // ADD passwordEncoder
        this.appUserService = appUserService;
        this.addressService = addressService;
        this.branchService = branchService;
        this.passwordEncoder = passwordEncoder; // ADD THIS
    }

    @PostMapping
    public ResponseEntity<?> addAppUser(@Valid @RequestBody AppUserDTO appUserDTO) {
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

            Address address = addressService.getAddressById(String.valueOf(appUserDTO.addressId));
            if (address == null) {
                throw new ResourceNotFoundException("Address with ID " + appUserDTO.addressId + " does not exist");
            }

            Branch branch = branchService.getBranch(appUserDTO.branchId != null ? appUserDTO.branchId : 1);

            AppUser newUser = new AppUser();
            newUser.setFirstName(appUserDTO.firstName);
            newUser.setLastName(appUserDTO.lastName);
            newUser.setEmail(appUserDTO.email);
            newUser.setPhoneNumber(appUserDTO.phoneNumber);
            newUser.setLogin(appUserDTO.login);
            newUser.setPassword(passwordEncoder.encode(appUserDTO.password)); // ENCRYPT PASSWORD HERE
            newUser.setAddress(address);
            newUser.setBranch(branch);
            newUser.setActive(true);
            newUser.setRole(appUserDTO.role);

            // Set timestamps
            LocalDateTime now = LocalDateTime.now();
            newUser.setCreatedAt(now);
            newUser.setModifiedAt(now);

            appUserService.addAppUser(newUser);
            AppUserDTO savedAppUserDTO = new AppUserDTO(newUser);
            return new ResponseEntity<>(savedAppUserDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppUser(@PathVariable long id, @Valid @RequestBody AppUserDTO appUserDTO) {
        try {
            Address address = addressService.getAddressById(String.valueOf(appUserDTO.addressId));
            if (address == null) {
                throw new ResourceNotFoundException("Address with ID " + appUserDTO.addressId + " does not exist");
            }

            AppUser existingUser = appUserService.getAppUser(id);
            existingUser.setFirstName(appUserDTO.firstName);
            existingUser.setLastName(appUserDTO.lastName);
            existingUser.setEmail(appUserDTO.email);
            existingUser.setPhoneNumber(appUserDTO.phoneNumber);
            existingUser.setLogin(appUserDTO.login);

            // Only encrypt password if it's being changed (not empty)
            if (appUserDTO.password != null && !appUserDTO.password.trim().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(appUserDTO.password)); // ENCRYPT PASSWORD HERE TOO
            }

            existingUser.setAddress(address);
            existingUser.setModifiedAt(LocalDateTime.now());

            appUserService.updateAppUser(existingUser.getId(), existingUser);
            AppUserDTO savedAppUserDTO = new AppUserDTO(existingUser);
            return new ResponseEntity<>(savedAppUserDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppUser(@PathVariable long id) {
        try {
            appUserService.deleteAppUser(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AppUserDTO>> listAppUsers() {
        List<AppUser> users = appUserService.listAppUsers();
        List<AppUserDTO> userDTOs = users.stream()
                .map(AppUserDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppUser(@PathVariable long id) {
        try {
            AppUser user = appUserService.getAppUser(id);
            return ResponseEntity.ok(new AppUserDTO(user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}