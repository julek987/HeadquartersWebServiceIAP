package IAP.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "product_change_logs")
public class ProductChangeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "product_id",  nullable = false)
    private long productId;

    @Column(name = "changed_by", nullable = false)
    private long changedBy;

    @Column(name = "change_reason", nullable = true)
    private String changeReason;

    // !!! This is json but I don't know how to do it yet :)
    @Column(name = "changes", nullable = false)
    private String changes;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public ProductChangeLog() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public long getChangedBy() { return changedBy; }
    public void setChangedBy(long changedBy) { this.changedBy = changedBy; }

    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }

    public String getChanges() { return changes; }
    public void setChanges(String changes) { this.changes = changes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

}
