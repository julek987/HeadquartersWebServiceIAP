package IAP.model.DTO;

import IAP.model.SupplyOrder;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SupplyOrderDTO {

    public Long id;

    @NotNull(message = "Branch ID is required")
    public Long branchId;

    @NotNull(message = "Supply Request ID is required")
    public Long supplyRequestId;

    @NotNull(message = "Product ID is required")
    public Long productId;

    @NotNull(message = "Quantity is required")
    public long quantity;

    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public SupplyOrderDTO() {}

    public SupplyOrderDTO(SupplyOrder supplyOrder) {
        this.id                 = supplyOrder.getId();
        this.branchId           = supplyOrder.getBranch().getId();
        this.supplyRequestId    = supplyOrder.getSupplyRequest().getId();
        this.productId          = supplyOrder.getProduct().getId();
        this.quantity           = supplyOrder.getQuantity();
        this.createdAt          = supplyOrder.getCreatedAt();
        this.modifiedAt         = supplyOrder.getModifiedAt();
    }

}
