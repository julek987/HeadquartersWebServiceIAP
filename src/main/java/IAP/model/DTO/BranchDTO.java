package IAP.model.DTO;

import IAP.model.Branch;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BranchDTO {

    public long             id;
    public boolean          active;
    public String           name;
    public long             addressId;
    public long             managerId;
    public LocalDateTime    createdAt;
    public LocalDateTime    modifiedAt;

    public BranchDTO() {}

    public BranchDTO(Branch branch) {
        this.id         = branch.getId();
        this.active     = branch.isActive();
        this.name       = branch.getName();
        this.addressId  = branch.getAddress().getId();
        this.managerId  = branch.getManager().getId();
        this.createdAt  = branch.getCreatedAt();
        this.modifiedAt = branch.getModifiedAt();
    }

}