package IAP.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "product_name",  nullable = false)
    private String productName;

    @Column(name = "price",  nullable = false)
    private float price;

    @Column(name = "width",  nullable = true)
    private int width;

    @Column(name = "depth",  nullable = true)
    private int depth;

    @Column(name = "height",  nullable = true)
    private int height;



    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    private Timestamp modifiedAt;

    @Column(name = "archived_at", nullable = true)
    private Timestamp archivedAt;

    public Product() {}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getProductName() {return productName;}
    public void setProductName(String productName) {this.productName = productName;}

    public float getPrice() {return price;}
    public void setPrice(float price) {this.price = price;}

    public int getWidth() {return width;}
    public void setWidth(int width) {this.width = width;}

    public int getDepth() {return depth;}
    public void setDepth(int depth) {this.depth = depth;}

    public int getHeight() {return height;}
    public void setHeight(int height) {this.height = height;}

    public Timestamp getCreatedAt() {return createdAt;}
    public void setCreatedAt(Timestamp createdAt) {this.createdAt = createdAt;}

    public Timestamp getModifiedAt() {return modifiedAt;}
    public void setModifiedAt(Timestamp modifiedAt) {this.modifiedAt = modifiedAt;}

    public Timestamp getArchivedAt() {return archivedAt;}
    public void setArchivedAt(Timestamp archivedAt) {this.archivedAt = archivedAt;}
}
