package IAP.model.DTO;

import IAP.model.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class BranchOrderDTO {

    public Long id;

    @NotNull(message = "Sale ID is required")
    public Long saleId;

    @NotNull(message = "Product ID is required")
    public Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    public Long quantitySold;

    @NotNull(message = "Price is required")
    public Double salePrice; // Todo: this is Double now instead of BigDecimal

    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public BranchOrderDTO() {}

    public BranchOrderDTO(Order branchOrder) {
        this.id             = branchOrder.getId();
        this.saleId         = branchOrder.getSale() != null ? branchOrder.getSale().getId() : null;
        this.productId      = branchOrder.getProduct() != null ? branchOrder.getProduct().getId() : null;
        this.quantitySold   = branchOrder.getQuantitySold();
        this.salePrice      = branchOrder.getSalePrice();
        this.createdAt      = branchOrder.getCreatedAt();
        this.modifiedAt     = branchOrder.getModifiedAt();
    }

    @Override
    public String toString() {
        return String.format(
                "{id: %d, saleId: %d, productId: %d, quantitySold: %d, salePrice: %f, createdAt: %s, modifiedAt: %s}",
                id, saleId, productId, quantitySold, salePrice, createdAt, modifiedAt);
    }

    // ===== getters and setters =====
    // they are needed for JSON deserialization


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
