package IAP.model.DTO;

import IAP.model.Order;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDTO {
    public Long id;

    @NotNull(message = "Branch ID is required")
    public Long branchId;

    @NotNull(message = "Sale ID is required")
    public Long saleId;

    @NotNull(message = "Product ID is required")
    public Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    public Integer quantitySold;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    public BigDecimal salePrice;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public OrderDTO() {}

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.branchId = order.getBranch() != null ? order.getBranch().getId() : null;
        this.saleId = order.getSale() != null ? order.getSale().getId() : null;
        this.productId = order.getProduct() != null ? order.getProduct().getId() : null;
        this.quantitySold = order.getQuantitySold();
        this.salePrice = order.getSalePrice();
        this.createdAt = order.getCreatedAt();
        this.modifiedAt = order.getModifiedAt();
    }
}