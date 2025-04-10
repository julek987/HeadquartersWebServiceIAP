package IAP.model.DTO;

import IAP.model.AppUser;

import java.time.LocalDateTime;

public class AppUserDTO {

    private long id;
    private long branchId;
    private long branchUserId;
    private long addressId;
    private boolean active;
    public String firstName;
    private String middleName;
    public String lastName;
    public String email;
    public String phoneNumber;
    private int role;
    public String login;
    public String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public AppUserDTO() {}

    public AppUserDTO(AppUser appUser) {
        this.id = appUser.getId();
        this.branchId = appUser.getBranch().getId();
        this.branchUserId = appUser.getBranchUserId();
        this.addressId = appUser.getAddress().getId();
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
