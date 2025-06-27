package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Branch;
import IAP.model.DTO.SupplyRequestDTO;
import IAP.model.SupplyRequest;
import IAP.repository.BranchRepository;
import IAP.repository.SupplyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplyRequestServiceImpl implements SupplyRequestService {

    private final SupplyRequestRepository   supplyRequestRepository;
    private final BranchRepository          branchRepository;

    @Autowired
    public SupplyRequestServiceImpl(
            SupplyRequestRepository supplyRequestRepository,
            BranchRepository        branchRepository) {
        this.supplyRequestRepository = supplyRequestRepository;
        this.branchRepository = branchRepository;

    }

    @Override
    public SupplyRequest getSupplyRequest(long id) throws ResourceNotFoundException {
        return supplyRequestRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("SupplyRequest not found with id: " + id));
    }

    @Override
    public List<SupplyRequest> listSupplyRequest() throws ResourceNotFoundException {
        return supplyRequestRepository.findAll();
    }

    @Override
    public List<SupplyRequest> listSupplyRequestByStatus(String status) throws ResourceNotFoundException {
        return supplyRequestRepository.findAllByStatus(status);
    }

    @Override
    public SupplyRequest addRequest(SupplyRequest supplyRequest) throws InvalidDataException, ResourceNotFoundException {
        return supplyRequestRepository.save(supplyRequest);
    }

    @Override
    public SupplyRequest updateRequest(long id, SupplyRequest supplyRequest) throws InvalidDataException, ResourceNotFoundException {
        SupplyRequest existingSR = supplyRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplyRequest not found with id: " + id));

        return supplyRequestRepository.save(supplyRequest);
    }

    @Override
    public void deleteRequest(long id) throws ResourceNotFoundException {
        SupplyRequest existingSR = supplyRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplyRequest not found with id: " + id));

        supplyRequestRepository.deleteById(id);
    }

    @Override
    public SupplyRequest createFromDTO(SupplyRequestDTO dto) {
        SupplyRequest request = new SupplyRequest();

        // Todo: Branch is fixed now!
        Branch branch = branchRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + 1L));
        request.setBranch(branch);

        request.setBranchRequestID(dto.branchRequestID);
        request.setStatus(dto.status);
        request.setReviewedBy(dto.reviewedBy);
        request.setReviewedAt(dto.reviewedAt);
        request.setAnnotation(dto.annotation);
        request.setCreatedAt(dto.createdAt != null ? dto.createdAt : LocalDateTime.now());
        request.setModifiedAt(LocalDateTime.now());

        return supplyRequestRepository.save(request);
    }

    @Override
    public boolean existsByBranchIdAndBranchRequestId(Long branchId, Long branchRequestId) {
        return supplyRequestRepository.existsByBranchIdAndBranchRequestID(branchId, branchRequestId);
    }


}
