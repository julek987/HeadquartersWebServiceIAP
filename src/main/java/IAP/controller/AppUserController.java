package IAP.controller;

import IAP.model.AppUser;
import IAP.model.DTO.AppUserDTO;
import IAP.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<AppUserDTO> addAppUser(@RequestBody AppUserDTO appUserDTO) {
        AppUser newUser = new AppUser();

        newUser.setFirstName(appUserDTO.firstName);
        newUser.setLastName(appUserDTO.lastName);
        newUser.setEmail(appUserDTO.email);
        newUser.setPhoneNumber(appUserDTO.phoneNumber);
        newUser.setLogin(appUserDTO.login);
        newUser.setPassword(appUserDTO.password);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setModifiedAt(LocalDateTime.now());

        appUserService.addAppUser(newUser);

        AppUserDTO savedDTO = new AppUserDTO(newUser);
        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDTO> updateAppUser(@PathVariable long id, @RequestBody AppUserDTO appUserDTO) {
        AppUser existingUser = appUserService.getAppUser(id);
        if (existingUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingUser.setFirstName(appUserDTO.firstName);
        existingUser.setLastName(appUserDTO.lastName);
        existingUser.setEmail(appUserDTO.email);
        existingUser.setPhoneNumber(appUserDTO.phoneNumber);
        existingUser.setLogin(appUserDTO.login);
        existingUser.setPassword(appUserDTO.password);
        existingUser.setModifiedAt(LocalDateTime.now());

        appUserService.updateAppUser(existingUser);

        AppUserDTO updatedDTO = new AppUserDTO(existingUser);
        return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppUser(@PathVariable long id) {
        appUserService.deleteAppUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppUserDTO>> listAppUsers() {
        List<AppUser> users = appUserService.listAppUsers();
        List<AppUserDTO> userDTOs = users.stream()
                .map(AppUserDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUserDTO> getAppUser(@PathVariable long id) {
        AppUser user = appUserService.getAppUser(id);
        if (user != null) {
            return new ResponseEntity<>(new AppUserDTO(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
