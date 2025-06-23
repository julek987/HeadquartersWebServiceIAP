package IAP.model.DTO;

import IAP.model.Sale;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class SaleDTO {
    public Long id;

    @NotNull(message = "Branch ID is required")
    public Long branchId;

    @NotNull(message = "User ID is required")
    public Long appUserId;

    @NotNull(message = "Sale date is required")
    public LocalDateTime saleDate;

    @Size(max = 500, message = "Annotations must be less than 500 characters")
    public String annotations;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SaleDTO() {}

    public SaleDTO(Sale sale) {
        this.id = sale.getId();
        this.branchId = sale.getBranch() != null ? sale.getBranch().getId() : null;
        this.saleDate = sale.getSaleDate();
        this.annotations = sale.getAnnotations();
        this.createdAt = sale.getCreatedAt();
        this.modifiedAt = sale.getModifiedAt();
    }
}