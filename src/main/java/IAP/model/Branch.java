package IAP.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn
    private Address address;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private AppUser manager;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;



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

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getModifiedAt() {return modifiedAt;}
    public void setModifiedAt(LocalDateTime modifiedAt) {this.modifiedAt = modifiedAt;}

}
