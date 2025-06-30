package IAP.model.DTO;

import IAP.model.Sale;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class SaleDTO {
    public Long id;

    @NotNull(message = "Branch ID is required")
    public Long branchId;

    @NotNull(message = "Branch sale ID must not be NULL")
    public Long branchSaleId;

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
        this.branchSaleId = sale.getBranchSaleId();
        this.saleDate = sale.getSaleDate();
        this.annotations = sale.getAnnotations();
        this.createdAt = sale.getCreatedAt();
        this.modifiedAt = sale.getModifiedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    public Long getBranchSaleId() { return branchSaleId; }
    public void setBranchSaleId(Long branchSaleId) { this.branchSaleId = branchSaleId; }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }

    public String getAnnotations() { return annotations; }
    public void setAnnotations(String annotations) { this.annotations = annotations; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}