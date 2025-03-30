package IAP.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;

@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "name",  nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id", nullable = false)
    private AppUser manager;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    private Timestamp modifiedAt;



    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public boolean isActive() {return active;}
    public void setActive(boolean active) {this.active = active;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public Address getAddress() {return address;}
    public void setAddress(Address address) {this.address = address;}

    public AppUser getManager() {return manager;}
    public void setManager(AppUser manager) {this.manager = manager;}

    public Timestamp getCreatedAt() {return createdAt;}
    public void setCreatedAt(Timestamp createdAt) {this.createdAt = createdAt;}

    public Timestamp getModifiedAt() {return modifiedAt;}
    public void setModifiedAt(Timestamp modifiedAt) {this.modifiedAt = modifiedAt;}

}
