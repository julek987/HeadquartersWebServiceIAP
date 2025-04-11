package IAP.model.DTO;

import IAP.model.Branch;

import java.time.LocalDateTime;

public class BranchDTO {

    public Long             id;
    public boolean          active;
    public String           name;
    public Long            addressId;
    public Long             managerId;
    public LocalDateTime    createdAt;
    public LocalDateTime    modifiedAt;

    public BranchDTO() {}

    public BranchDTO(Branch branch) {
        this.id         = branch.getId();
        this.active     = branch.isActive();
        this.name       = branch.getName();
        this.addressId  = branch.getAddress() != null ? branch.getAddress().getId() : null;
        this.managerId  = branch.getManager() != null ? branch.getManager().getId() : null;
        this.createdAt  = branch.getCreatedAt();
        this.modifiedAt = branch.getModifiedAt();
    }

}