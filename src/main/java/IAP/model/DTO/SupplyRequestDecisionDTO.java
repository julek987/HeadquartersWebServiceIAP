package IAP.model.DTO;

import jakarta.validation.constraints.NotNull;

public class SupplyRequestDecisionDTO {

    @NotNull(message = "Status is required")
    public String status;

    public String annotation;

    public SupplyRequestDecisionDTO() {}

    public SupplyRequestDecisionDTO(String status, String annotation) {
        this.status = status;
        this.annotation = annotation;
    }

}
