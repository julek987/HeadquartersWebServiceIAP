package IAP.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "appuser")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "role", nullable = false)
    private int role;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;


    // mapped
    @OneToOne(mappedBy = "manager",  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Branch managedBranch;

    @OneToMany(mappedBy = "addedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> addedProducts = new HashSet<>(0);

    @OneToMany(mappedBy = "changedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductChangeLog> changedProductsLogs = new HashSet<>(0);



    public AppUser() {}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {return address;}

    public void setAddress(Address address) {this.address = address;}

    public Branch getBranch() {return branch;}

    public void setBranch(Branch branch) {this.branch = branch;}

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }


    // mapped getters and setters
    public Set<Product> getListProductAddedByAppUser() {return addedProducts;}

    public void setListProductAddedByAppUser(Set<Product> addedProducts) {this.addedProducts = addedProducts;}

    public Branch getManagedBranch() {return managedBranch;}

    public void setManagedBranch(Branch managedBranch) {this.managedBranch = managedBranch;}

    public Set<ProductChangeLog> getChangedProductsLogs() {return changedProductsLogs;}

    public void setChangedProductsLogs(Set<ProductChangeLog> changedProductsLogs) {this.changedProductsLogs = changedProductsLogs;}

}