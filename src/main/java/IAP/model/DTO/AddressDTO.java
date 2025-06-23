package IAP.model.DTO;

import IAP.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class AddressDTO {

    public Long id;
    public Long branchId;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must be less than 100 characters")
    public String country;

    @Size(max = 100, message = "Region must be less than 100 characters")
    public String region;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must be less than 100 characters")
    public String city;

    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must be less than 20 characters")
    public String postalCode;

    @Size(max = 100, message = "Street must be less than 100 characters")
    public String street;

    @NotBlank(message = "Address line 1 is required")
    @Size(max = 200, message = "Address line 1 must be less than 200 characters")
    public String addressLine1;

    @Size(max = 200, message = "Address line 2 must be less than 200 characters")
    public String addressLine2;

    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public AddressDTO() {}

    public AddressDTO(Address address) {
        this.id = address.getId();
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