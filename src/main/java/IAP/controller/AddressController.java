package IAP.controller;

import IAP.model.Address;
import IAP.model.Branch;
import IAP.model.DTO.AddressDTO;
import IAP.service.AddressService;
import IAP.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;
    private final BranchService branchService;

    @Autowired
    public AddressController(AddressService addressService, BranchService branchService) {
        this.addressService = addressService;
        this.branchService = branchService;
    }

    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO) {

        Branch existingBranch = branchService.getBranch(addressDTO.branchId);
        if (existingBranch == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Address newAddress = new Address();
        newAddress.setBranch(existingBranch);
        newAddress.setBranchUserId(addressDTO.branchUserId);
        newAddress.setCity(addressDTO.city);
        newAddress.setCountry(addressDTO.country);
        newAddress.setRegion(addressDTO.region);
        newAddress.setPostalCode(addressDTO.postalCode);
        newAddress.setStreet(addressDTO.street);
        newAddress.setAddressLine1(addressDTO.addressLine1);
        newAddress.setAddressLine2(addressDTO.addressLine2);
        newAddress.setCreatedAt(LocalDateTime.now());
        newAddress.setModifiedAt(LocalDateTime.now());

        addressService.addAddress(newAddress);
        AddressDTO savedAddressDTO = new AddressDTO(newAddress);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable long id, @RequestBody AddressDTO addressDTO) {
        Address existingAddress = addressService.getAddress(id);
        if (existingAddress == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Branch existingBranch = branchService.getBranch(addressDTO.branchId);
        if (existingBranch == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingAddress.setBranch(existingBranch);
        existingAddress.setBranchUserId(addressDTO.branchUserId);
        existingAddress.setCity(addressDTO.city);
        existingAddress.setCountry(addressDTO.country);
        existingAddress.setRegion(addressDTO.region);
        existingAddress.setPostalCode(addressDTO.postalCode);
        existingAddress.setStreet(addressDTO.street);
        existingAddress.setAddressLine1(addressDTO.addressLine1);
        existingAddress.setAddressLine2(addressDTO.addressLine2);
        existingAddress.setModifiedAt(LocalDateTime.now());

        addressService.updateAddress(existingAddress);
        AddressDTO updatedAddressDTO = new AddressDTO(existingAddress);
        return new ResponseEntity<>(updatedAddressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable long id) {
        addressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressDTO>> listAddresses() {
        List<Address> addresses = addressService.listAddress();
        List<AddressDTO> addressDTOs = addresses.stream()
                .map(AddressDTO::new)
                .toList();

        return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressDTO> getAddress(@PathVariable long id) {
        Address address = addressService.getAddress(id);
        if (address != null) {
            return new ResponseEntity<>(new AddressDTO(address), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
