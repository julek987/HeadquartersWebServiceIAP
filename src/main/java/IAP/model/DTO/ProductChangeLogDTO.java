package IAP.model.DTO;

import IAP.model.ProductChangeLog;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ProductChangeLogDTO {

    public Long id;

    @NotNull(message = "Product ID is required")
    public Long productId;

    @NotNull(message = "Changed by user ID is required")
    public Long changedById;

    @Size(max = 500, message = "Change reason must be less than 500 characters")
    public String changeReason;

    @NotNull(message = "Changes are required")
    public Object changes; // Assuming this will be some object related to the changes

    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public ProductChangeLogDTO() {}

    public ProductChangeLogDTO(ProductChangeLog productChangeLog) {
        this.id = productChangeLog.getId();
        this.productId = productChangeLog.getProduct() != null ? productChangeLog.getProduct().getId() : null;
        this.changedById = productChangeLog.getChangedBy() != null ? productChangeLog.getChangedBy().getId() : null;
        this.changeReason = productChangeLog.getChangeReason();
        this.changes = productChangeLog.getChanges();
        this.createdAt = productChangeLog.getCreatedAt();
    }
}
