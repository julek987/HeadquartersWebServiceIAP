package IAP.model.DTO;

import IAP.model.Branch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class BranchDTO {

    public Long id;

    @NotNull(message = "Branch name is required")
    @Size(max = 100, message = "Branch name must be less than 100 characters")
    public String name;

    public boolean active;

    @NotNull(message = "Address ID is required")
    public Long addressId;

    public Long managerId;

    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public BranchDTO() {}

    public BranchDTO(Branch branch) {
        this.id = branch.getId();
        this.name = branch.getName();
        this.active = branch.isActive();
        this.addressId = branch.getAddress() != null ? branch.getAddress().getId() : null;
        this.managerId = branch.getManager() != null ? branch.getManager().getId() : null;
        this.createdAt = branch.getCreatedAt();
        this.modifiedAt = branch.getModifiedAt();
    }
}
