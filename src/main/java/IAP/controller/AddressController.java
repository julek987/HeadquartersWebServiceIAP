package IAP.controller;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Address;
import IAP.model.DTO.AddressDTO;
import IAP.service.AddressService;
import IAP.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    private final BranchService branchService;

    @Autowired
    public AddressController(AddressService addressService, BranchService branchService) {
        this.addressService = addressService;
        this.branchService = branchService;
    }

    @PostMapping
    public ResponseEntity<?> addAddress(@Valid @RequestBody AddressDTO addressDTO) {
        try {
            Address newAddress = new Address();
            newAddress.setBranch(branchService.getBranch(addressDTO.branchId));
            if (newAddress.getBranch() == null) {
                throw new ResourceNotFoundException("Branch with ID " + addressDTO.branchId + " not found");
            }

            newAddress.setBranchUserId(addressDTO.branchUserId);
            newAddress.setCountry(addressDTO.country);
            newAddress.setRegion(addressDTO.region);
            newAddress.setCity(addressDTO.city);
            newAddress.setPostalCode(addressDTO.postalCode);
            newAddress.setStreet(addressDTO.street);
            newAddress.setAddressLine1(addressDTO.addressLine1);
            newAddress.setAddressLine2(addressDTO.addressLine2);
            newAddress.setCreatedAt(LocalDateTime.now());
            newAddress.setModifiedAt(LocalDateTime.now());

            addressService.addAddress(newAddress);
            AddressDTO savedAddressDTO = new AddressDTO(newAddress);
            return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable long id, @Valid @RequestBody AddressDTO addressDTO) {
        try {
            Address existingAddress = addressService.getAddress(id);
            if (existingAddress == null) {
                throw new ResourceNotFoundException("Address with ID " + id + " not found");
            }

            existingAddress.setBranch(branchService.getBranch(addressDTO.branchId));
            if (existingAddress.getBranch() == null) {
                throw new ResourceNotFoundException("Branch with ID " + addressDTO.branchId + " not found");
            }

            existingAddress.setBranchUserId(addressDTO.branchUserId);
            existingAddress.setCountry(addressDTO.country);
            existingAddress.setRegion(addressDTO.region);
            existingAddress.setCity(addressDTO.city);
            existingAddress.setPostalCode(addressDTO.postalCode);
            existingAddress.setStreet(addressDTO.street);
            existingAddress.setAddressLine1(addressDTO.addressLine1);
            existingAddress.setAddressLine2(addressDTO.addressLine2);
            existingAddress.setModifiedAt(LocalDateTime.now());

            addressService.addAddress(existingAddress);
            AddressDTO savedAddressDTO = new AddressDTO(existingAddress);
            return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> listAddresses() {
        List<Address> addresses = addressService.listAddresses();
        List<AddressDTO> dtos = addresses.stream()
                .map(AddressDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddress(@PathVariable long id) {
        try {
            Address address = addressService.getAddress(id);
            if (address == null) {
                throw new ResourceNotFoundException("Address with ID " + id + " not found");
            }
            return ResponseEntity.ok(new AddressDTO(address));
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }
}
