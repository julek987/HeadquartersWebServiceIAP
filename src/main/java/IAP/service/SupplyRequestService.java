package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.DTO.SupplyRequestDTO;
import IAP.model.SupplyRequest;

import java.util.List;

public interface SupplyRequestService {
    SupplyRequest getSupplyRequest(long id) throws ResourceNotFoundException;
    List<SupplyRequest> listSupplyRequest() throws ResourceNotFoundException;
    List<SupplyRequest> listSupplyRequestByStatus(String status) throws ResourceNotFoundException;
    SupplyRequest addRequest(SupplyRequest supplyRequest) throws InvalidDataException, ResourceNotFoundException;
    SupplyRequest updateRequest(long id, SupplyRequest supplyRequest) throws InvalidDataException, ResourceNotFoundException;
    void deleteRequest(long id) throws ResourceNotFoundException;
    SupplyRequest createFromDTO(SupplyRequestDTO dto);
    boolean existsByBranchIdAndBranchRequestId(Long branchId, Long branchRequestID);

}
