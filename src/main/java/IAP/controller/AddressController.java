package IAP.controller;

import IAP.model.Address;
import IAP.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        address.setCreatedAt(now);
        address.setModifiedAt(now);

        addressService.addAddress(address);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable long id, @RequestBody Address address) {
        Address existingAddress = addressService.getAddress(id);
        if (existingAddress == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Preserve the createdAt field from the existing entity
        address.setCreatedAt(existingAddress.getCreatedAt());
        address.setModifiedAt(new Timestamp(System.currentTimeMillis()  / 1000));
        address.setId(id);

        addressService.updateAddress(address);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable long id) {
        addressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Address>> listAddresses() {
        List<Address> addresses = addressService.listAddress();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Address> getAddress(@PathVariable long id) {
        Address address = addressService.getAddress(id);
        if (address != null) {
            return new ResponseEntity<>(address, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
