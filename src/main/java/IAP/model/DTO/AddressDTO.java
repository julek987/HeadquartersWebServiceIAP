package IAP.model.DTO;

import IAP.model.Address;

import java.time.LocalDateTime;

public class AddressDTO {

    public long id;
    public long branchId;
    public long branchUserId;
    public String country;
    public String region;
    public String city;
    public String postalCode;
    public String street;
    public String addressLine1;
    public String addressLine2;
    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public AddressDTO() {}

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.branchId = address.getBranch().getId();
        this.branchUserId = address.getBranchUserId();
        this.country = address.getCountry();
        this.region = address.getRegion();
        this.city = address.getCity();
        this.postalCode = address.getPostalCode();
        this.street = address.getStreet();
        this.addressLine1 = address.getAddressLine1();
        this.addressLine2 = address.getAddressLine2();
        this.createdAt = address.getCreatedAt();
        this.modifiedAt = address.getModifiedAt();
    }
}
