package IAP.controller;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.AppUser;
import IAP.model.DTO.AppUserDTO;
import IAP.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<?> addAppUser(@Valid @RequestBody AppUserDTO appUserDTO) {
        try {
            AppUser newUser = new AppUser();
            newUser.setFirstName(appUserDTO.firstName);
            newUser.setLastName(appUserDTO.lastName);
            newUser.setEmail(appUserDTO.email);
            newUser.setPhoneNumber(appUserDTO.phoneNumber);
            newUser.setLogin(appUserDTO.login);
            newUser.setPassword(appUserDTO.password);

            appUserService.addAppUser(newUser);
            AppUserDTO savedAppUserDTO = new AppUserDTO(newUser);
            return new ResponseEntity<>(savedAppUserDTO, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppUser(@PathVariable long id, @Valid @RequestBody AppUserDTO appUserDTO) {
        try {
            AppUser existingUser = appUserService.getAppUser(id);
            existingUser.setId(id);
            existingUser.setFirstName(appUserDTO.firstName);
            existingUser.setLastName(appUserDTO.lastName);
            existingUser.setEmail(appUserDTO.email);
            existingUser.setPhoneNumber(appUserDTO.phoneNumber);
            existingUser.setLogin(appUserDTO.login);
            existingUser.setPassword(appUserDTO.password);

            appUserService.addAppUser(existingUser);
            AppUserDTO savedAppUserDTO = new AppUserDTO(existingUser);
            return new ResponseEntity<>(savedAppUserDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppUser(@PathVariable long id) {
        try {
            appUserService.deleteAppUser(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}