package IAP.model.DTO;

import IAP.model.Sale;

import java.time.LocalDateTime;

public class SaleDTO {

    public Long id;
    public Long branchId;
    public Long appUserId;
    public LocalDateTime saleDate;
    public String annotations;
    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public SaleDTO() {}

    public SaleDTO(Sale Sale){
        this.id = Sale.getId();
        this.appUserId = Sale.getAppUser().getId();
        this.branchId = Sale.getBranch().getId();
        this.saleDate = Sale.getSaleDate();
        this.annotations = Sale.getAnnotations();
        this.createdAt = Sale.getCreatedAt();
        this.modifiedAt = Sale.getModifiedAt();
    }
}
