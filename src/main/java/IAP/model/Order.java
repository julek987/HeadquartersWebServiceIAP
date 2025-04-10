package IAP.model;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity_sold", nullable = false)
    private Integer quantitySold;

    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Branch getBranch() {return branch;}

    public void setBranch(Branch branch) {this.branch = branch;}

    public Product getProduct() {return product;}

    public void setProduct(Product product) {this.product = product;}

    public Sale getSale() {return sale;}

    public void setSale(Sale sale) {this.sale = sale;}

    public Integer getQuantitySold() {return quantitySold;}

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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

}
