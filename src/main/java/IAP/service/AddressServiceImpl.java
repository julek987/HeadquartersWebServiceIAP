package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Address;
import IAP.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void addAddress(Address address) throws InvalidDataException {
        validateAddress(address);
        address.setCreatedAt(LocalDateTime.now());
        address.setModifiedAt(LocalDateTime.now());
        addressRepository.save(address);
    }

    @Override
    public void updateAddress(Address address) throws ResourceNotFoundException, InvalidDataException {
        validateAddress(address);

        if (!addressRepository.existsById(address.getId())) {
            throw new ResourceNotFoundException("Address not found with id: " + address.getId());
        }

        address.setModifiedAt(LocalDateTime.now());
        addressRepository.save(address);
    }

    @Override
    public void deleteAddress(long id) throws ResourceNotFoundException {
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }
        addressRepository.deleteById(id);
    }

    @Override
    public List<Address> listAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address getAddress(long id) throws ResourceNotFoundException {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
    }

    private void validateAddress(Address address) throws InvalidDataException {
        if (!StringUtils.hasText(address.getCountry())) {
            throw new InvalidDataException("Country is required");
        }
        if (!StringUtils.hasText(address.getCity())) {
            throw new InvalidDataException("City is required");
        }
        if (!StringUtils.hasText(address.getPostalCode())) {
            throw new InvalidDataException("Postal code is required");
        }
        if (!StringUtils.hasText(address.getAddressLine1())) {
            throw new InvalidDataException("Address line 1 is required");
        }
    }
}