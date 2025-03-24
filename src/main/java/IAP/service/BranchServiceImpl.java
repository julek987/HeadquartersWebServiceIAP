package IAP.service;

import IAP.model.Branch;
import IAP.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {

    private BranchRepository branchRepository;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository) { this.branchRepository = branchRepository; }

    @Override
    public void addBranch(Branch branch) { this.branchRepository.save(branch); }

    @Override
    public void updateBranch(Branch branch) { this.branchRepository.save(branch); }

    @Override
    public void deleteBranch(long id) { this.branchRepository.deleteById(id); }

    @Override
    public List<Branch> listBranch() { return this.branchRepository.findAll(); }

    @Override
    public Branch getBranch(long id) { return this.branchRepository.findById(id); }

}
