package IAP.model.DTO;

import IAP.model.Order;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDTO {
    public Long id;

    @NotNull(message = "Sale ID is required")
    public Long saleId;

    @NotNull(message = "Product ID is required")
    public Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    public Long quantitySold;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    public Double salePrice;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public OrderDTO() {}

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.saleId = order.getSale() != null ? order.getSale().getId() : null;
        this.productId = order.getProduct() != null ? order.getProduct().getId() : null;
        this.quantitySold = order.getQuantitySold();
        this.salePrice = order.getSalePrice();
        this.createdAt = order.getCreatedAt();
        this.modifiedAt = order.getModifiedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSaleId() { return saleId; }
    public void setSaleId(Long saleId) { this.saleId = saleId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getQuantitySold() { return quantitySold; }
    public void setQuantitySold(Long quantitySold) { this.quantitySold = quantitySold; }

    public Double getSalePrice() { return salePrice; }
    public void setSalePrice(Double salePrice) { this.salePrice = salePrice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}