package IAP.model;

import IAP.model.objects.ProductChanges;
import IAP.utils.ProductChangesConverter;
import jakarta.persistence.*;


import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_change_logs")
public class ProductChangeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", nullable = false)
    private AppUser changedBy;

    @Column(name = "change_reason", nullable = true)
    private String changeReason;

    @Convert(converter = ProductChangesConverter.class)
    @Column(name = "changes", columnDefinition = "TEXT", nullable = false)
    private ProductChanges changes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    // getters and setters :)

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public AppUser getChangedBy() { return changedBy; }
    public void setChangedBy(AppUser changedBy) { this.changedBy = changedBy; }

    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }

    public ProductChanges getChanges() { return changes; }
    public void setChanges(ProductChanges changes) { this.changes = changes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
