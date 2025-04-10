package IAP.model.DTO;

import IAP.model.Address;
import IAP.model.Branch;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.sql.Timestamp;

public class AddressDTO {

    private long id;
    private long branchId;
    private long branchUserId;
    private String country;
    private String region;
    private String city;
    private String postalCode;
    private String street;
    private String addressLine1;
    private String addressLine2;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

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
