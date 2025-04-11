package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Address;
import java.util.List;

public interface AddressService {
    void addAddress(Address address) throws InvalidDataException;
    void updateAddress(Address address) throws ResourceNotFoundException, InvalidDataException;
    void deleteAddress(long id) throws ResourceNotFoundException;
    List<Address> listAddresses();
    Address getAddress(long id) throws ResourceNotFoundException;
    Address getAddressById(String id) throws ResourceNotFoundException;
}