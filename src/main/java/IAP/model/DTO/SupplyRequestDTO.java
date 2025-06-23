package IAP.model.DTO;

import IAP.model.SupplyRequest;

import java.time.LocalDateTime;

public class SupplyRequestDTO {

    public Long id;

    public Long branchId;

    public String status;

    public String reviewedBy;

    public LocalDateTime reviewedAt;

    public String annotation;

    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public SupplyRequestDTO() {}

    public SupplyRequestDTO(SupplyRequest supplyRequest) {
        this.id = supplyRequest.getId();
        this.branchId = supplyRequest.getBranch().getId();
        this.status = supplyRequest.getStatus();
        this.reviewedBy = supplyRequest.getReviewedBy();
        this.reviewedAt = supplyRequest.getReviewedAt();
        this.annotation = supplyRequest.getAnnotation();
        this.createdAt = supplyRequest.getCreatedAt();
        this.modifiedAt = supplyRequest.getModifiedAt();

    }

}
