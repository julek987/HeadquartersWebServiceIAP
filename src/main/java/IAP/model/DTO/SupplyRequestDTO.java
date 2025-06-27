package IAP.model.DTO;

import IAP.model.SupplyRequest;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SupplyRequestDTO {

    @NotNull(message = "ID is required")
    public Long id;

    @NotNull(message = "Branch ID is required")
    public Long branchId;

//    @NotNull(message = "Branch request ID is required")
    public Long branchRequestID;

    @NotNull(message = "Status is required")
    public SupplyRequest.Status status;

    @NotNull(message = "Reviewer name is required")
    public String reviewedBy;

    public LocalDateTime reviewedAt;

    public String annotation;

    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public SupplyRequestDTO() {}

    public SupplyRequestDTO(SupplyRequest supplyRequest) {
        this.id              = supplyRequest.getId();
        this.branchId        = supplyRequest.getBranch().getId();
        this.branchRequestID = supplyRequest.getBranchRequestID();
        this.status          = supplyRequest.getStatus();
        this.reviewedBy      = supplyRequest.getReviewedBy();
        this.reviewedAt      = supplyRequest.getReviewedAt();
        this.annotation      = supplyRequest.getAnnotation();
        this.createdAt       = supplyRequest.getCreatedAt();
        this.modifiedAt      = supplyRequest.getModifiedAt();

    }

    @Override
    public String toString() {
        return String.format(
                "id: %d, branchId: %d, branchRequestID: %d, status: %s, reviewedBy: %s, reviewedAt: %s, annotation: %s, createdAt: %s, modifiedAt: %s",
                id, branchId, branchRequestID, status, reviewedBy, reviewedAt, annotation, createdAt, modifiedAt);
    }

}
