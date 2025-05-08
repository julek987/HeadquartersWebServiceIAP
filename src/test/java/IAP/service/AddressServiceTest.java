package IAP.service;

import IAP.configuration.TestConfig;
import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Address;
import IAP.model.Branch;
import IAP.repository.AddressRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    private Address address;

    @Before
    public void setup() {
        address = new Address();
        address.setId(1L); // Make sure the ID is set to match your mock
        address.setBranch(new Branch());
        address.setCountry("Polska");
        address.setRegion("Łódzkie");
        address.setCity("Łódź");
        address.setPostalCode("95-275");
        address.setStreet("Zgierska 243");
        address.setAddressLine1("55/2");
        address.setAddressLine2("123");
        address.setCreatedAt(LocalDateTime.now());
        address.setModifiedAt(LocalDateTime.now());

        when(addressRepository.save(address)).thenReturn(address);
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
    }

    @Test
    public void testAddAddress() throws InvalidDataException {
        addressService.addAddress(address);
        verify(addressRepository).save(address);
    }

    @Test
    public void testUpdateAddress() throws InvalidDataException, ResourceNotFoundException {
        when(addressRepository.existsById(address.getId())).thenReturn(true);
        addressService.updateAddress(address);
        verify(addressRepository).save(address);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateAddress_NotFound() throws InvalidDataException, ResourceNotFoundException {
        when(addressRepository.existsById(address.getId())).thenReturn(false);
        addressService.updateAddress(address);
    }

    @Test
    public void testDeleteAddress() throws ResourceNotFoundException {
        when(addressRepository.existsById(1L)).thenReturn(true);
        addressService.deleteAddress(1L);
        verify(addressRepository).deleteById(1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteAddress_NotFound() throws ResourceNotFoundException {
        when(addressRepository.existsById(1L)).thenReturn(false);
        addressService.deleteAddress(1L);
    }

    @Test
    public void testListAddresses() {
        List<Address> list = List.of(address);
        when(addressRepository.findAll()).thenReturn(list);
        List<Address> result = addressService.listAddresses();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAddress() throws ResourceNotFoundException {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        Address result = addressService.getAddress(1L);
        assertEquals("Polska", result.getCountry());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetAddress_NotFound() throws ResourceNotFoundException {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
        addressService.getAddress(1L);
    }

    @Test
    public void testGetAddressById() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        Address result = addressService.getAddressById("1");
        assertEquals("Polska", result.getCountry());
    }

    @Test
    public void testGetAddressById_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
        Address result = addressService.getAddressById("1");
        assertNull(result);
    }

}
