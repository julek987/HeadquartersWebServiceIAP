package IAP.service;

import IAP.model.Address;

import java.util.List;

public interface AddressService {

    public void addAddress(Address address);
    public void updateAddress(Address address);
    public void deleteAddress(long id);
    public List<Address> listAddress();
    public Address getAddress(long id);
}
