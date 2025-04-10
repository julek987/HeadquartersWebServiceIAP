package IAP.model.DTO;

import IAP.model.AppUser;

import java.sql.Timestamp;

public class AppUserDTO {

    private long id;
    private long branchId;
    private long branchUserId;
    private long addressId;
    private boolean active;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int role;
    private String login;
    private String password;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

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
