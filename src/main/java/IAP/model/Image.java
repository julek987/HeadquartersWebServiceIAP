package IAP.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "product_id",  nullable = false)
    private long productId;

    @Column(name = "url",  nullable = false)
    private String url;

    @Column(name = "show_order",  nullable = true)
    private int showOrder;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    private Timestamp modifiedAt;

    public Image() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public int getShowOrder() { return showOrder; }
    public void setShowOrder(int showOrder) { this.showOrder = showOrder; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(Timestamp modifiedAt) { this.modifiedAt = modifiedAt; }
}
