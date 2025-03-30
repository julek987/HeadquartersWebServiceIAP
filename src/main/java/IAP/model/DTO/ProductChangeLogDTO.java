package IAP.model.DTO;

import IAP.model.ProductChangeLog;
import IAP.model.ProductChanges;

import java.sql.Timestamp;

public class ProductChangeLogDTO {
    public long             id;
    public long             productId;
    public long             changedById;
    public String           changeReason;
    public ProductChanges   changes;
    public Timestamp        createdAt;

    public ProductChangeLogDTO() {}

    public ProductChangeLogDTO(ProductChangeLog productChangeLog) {
        this.id = productChangeLog.getId();
        this.productId = productChangeLog.getId();
        this.changedById = productChangeLog.getChangedBy().getId();
        this.changeReason = productChangeLog.getChangeReason();
        this.changes = productChangeLog.getChanges();
        this.createdAt = productChangeLog.getCreatedAt();
    }
}
