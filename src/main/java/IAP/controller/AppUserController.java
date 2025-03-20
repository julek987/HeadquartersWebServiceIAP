package IAP.controller;

import IAP.model.AppUser;
import IAP.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<AppUser> addAppUser(@RequestBody AppUser appUser) {
        appUserService.addAppUser(appUser);
        return new ResponseEntity<>(appUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateAppUser(@PathVariable long id, @RequestBody AppUser appUser) {
        appUser.setId(id);
        appUserService.updateAppUser(appUser);
        return new ResponseEntity<>(appUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppUser(@PathVariable long id) {
        appUserService.deleteAppUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppUser>> listAppUsers() {
        List<AppUser> users = appUserService.listAppUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUser> getAppUser(@PathVariable long id) {
        AppUser user = appUserService.getAppUser(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}