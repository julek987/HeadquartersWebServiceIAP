package IAP.model.DTO;

import IAP.model.Sale;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SaleDTO {

    private Long id;
    private Long branchId;
    private Long appUserId;
    private LocalDateTime saleDate;
    private String annotations;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

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
