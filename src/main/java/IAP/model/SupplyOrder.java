package IAP.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "supply_order")
public class SupplyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    private SupplyRequest supplyRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private long quantity;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }

    public SupplyRequest getSupplyRequest() { return supplyRequest; }
    public void setSupplyRequest(SupplyRequest supplyRequest) {this.supplyRequest = supplyRequest;}

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public long getQuantity() { return quantity; }
    public void setQuantity(long quantity) { this.quantity = quantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}
