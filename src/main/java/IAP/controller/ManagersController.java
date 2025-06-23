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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/managerctrl")
public class ManagersController {

    private final AppUserService appUserService;
    private final BranchService branchService;
    private final AddressService addressService;

    @Autowired
    public ManagersController(
            AppUserService appUserService,
            BranchService branchService,
            AddressService addressService
    ) {
        this.appUserService = appUserService;
        this.branchService = branchService;
        this.addressService = addressService;
    }

    @GetMapping()
    public ResponseEntity<List<AppUserDTO>> listManagers() {
        List<AppUserDTO> managerDTOs = appUserService.listManagers().stream()
                .map(AppUserDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(managerDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getManager(@PathVariable long id) {
        try {
            AppUser user = appUserService.getAppUser(id);
            if (user.getRole() != 0) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("Access denied: user is not a manager.");
            }

            user.setLogin("");
            user.setPassword("");
            return ResponseEntity.ok(new AppUserDTO(user));
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<?> addManager(@Valid @RequestBody AppUserDTO appUserDTO) {
        try {
            Address address = addressService.getAddressById(String.valueOf(appUserDTO.addressId));
            if (address == null) {
                throw new ResourceNotFoundException("Address with ID " + appUserDTO.addressId + " does not exist");
            }

            Branch branch = branchService.getBranch(appUserDTO.branchId);

            AppUser newUser = new AppUser();
            newUser.setFirstName(appUserDTO.firstName);
            newUser.setLastName(appUserDTO.lastName);
            newUser.setEmail(appUserDTO.email);
            newUser.setPhoneNumber(appUserDTO.phoneNumber);
            newUser.setLogin(appUserDTO.login);
            newUser.setPassword(appUserDTO.password);
            newUser.setAddress(address);
            newUser.setBranch(branch);
            newUser.setRole(0);
            newUser.setActive(true);

            appUserService.addAppUser(newUser);
            AppUserDTO savedAppUserDTO = new AppUserDTO(newUser);
            return new ResponseEntity<>(savedAppUserDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateManager(@PathVariable long id, @Valid @RequestBody AppUserDTO appUserDTO) {
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
            existingUser.setPassword(appUserDTO.password);
            existingUser.setAddress(address);

            appUserService.addAppUser(existingUser);
            AppUserDTO savedAppUserDTO = new AppUserDTO(existingUser);
            return new ResponseEntity<>(savedAppUserDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppUser(@PathVariable long id) {
        try {
            appUserService.deleteAppUser(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

}
