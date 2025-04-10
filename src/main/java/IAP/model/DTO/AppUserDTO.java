package IAP.model.DTO;

import IAP.model.AppUser;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class AppUserDTO {

    public Long id;

    @NotNull(message = "Branch ID is required")
    public Long branchId;

    public Long branchUserId;

    @NotNull(message = "Address ID is required")
    public Long addressId;

    public Boolean active = true;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    public String firstName;

    @Size(max = 50, message = "Middle name must be less than 50 characters")
    public String middleName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    public String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    public String email;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number should be valid")
    @Size(max = 20, message = "Phone number must be less than 20 characters")
    public String phoneNumber;

    @NotNull(message = "Role is required")
    @Min(value = 0, message = "Role must be positive")
    public Integer role;

    @NotBlank(message = "Login is required")
    @Size(min = 3, max = 30, message = "Login must be between 3 and 30 characters")
    public String login;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
    public String password;

    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    // Default constructor
    public AppUserDTO() {}

    // Constructor from entity
    public AppUserDTO(AppUser appUser) {
        this.id = appUser.getId();
        this.branchId = appUser.getBranch() != null ? appUser.getBranch().getId() : null;
        this.branchUserId = appUser.getBranchUserId();
        this.addressId = appUser.getAddress() != null ? appUser.getAddress().getId() : null;
        this.active = appUser.isActive();
        this.firstName = appUser.getFirstName();
        this.middleName = appUser.getMiddleName();
        this.lastName = appUser.getLastName();
        this.email = appUser.getEmail();
        this.phoneNumber = appUser.getPhoneNumber();
        this.role = appUser.getRole();
        this.login = appUser.getLogin();
        this.password = appUser.getPassword();
        this.createdAt = appUser.getCreatedAt();
        this.modifiedAt = appUser.getModifiedAt();
    }
}
