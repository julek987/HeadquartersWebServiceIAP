package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Branch;
import IAP.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public void addBranch(Branch branch) throws InvalidDataException {
        branch.setCreatedAt(LocalDateTime.now());
        branch.setModifiedAt(LocalDateTime.now());
        branchRepository.save(branch);
    }

    @Override
    public void updateBranch(Branch branch) throws ResourceNotFoundException, InvalidDataException {
        validateBranch(branch);

        if (!branchRepository.existsById(branch.getId())) {
            throw new ResourceNotFoundException("Branch not found with id: " + branch.getId());
        }

        branch.setModifiedAt(LocalDateTime.now());
        branchRepository.save(branch);
    }

    @Override
    public void deleteBranch(long id) throws ResourceNotFoundException {
        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Branch not found with id: " + id);
        }
        branchRepository.deleteById(id);
    }

    @Override
    public List<Branch> listBranch() {
        return branchRepository.findAll();
    }

    @Override
    public Branch getBranch(long id) throws ResourceNotFoundException {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
    }

    private void validateBranch(Branch branch) throws InvalidDataException {
        if (!StringUtils.hasText(branch.getName())) {
            throw new InvalidDataException("Branch name is required");
        }
        if (branch.getAddress() == null) {
            throw new InvalidDataException("Address is required for the branch");
        }
    }
}
