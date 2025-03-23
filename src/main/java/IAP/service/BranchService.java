package IAP.service;

import IAP.model.Branch;

import java.util.List;

public interface BranchService {

    public void addBranch(Branch branch);
    public void updateBranch(Branch branch);
    public void deleteBranch(long id);
    public List<Branch> listBranch();
    public Branch getBranch(long id);

}
