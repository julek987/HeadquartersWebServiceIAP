package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.SupplyRequest;
import IAP.repository.SupplyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyRequestServiceImpl implements SupplyRequestService {

    private final SupplyRequestRepository supplyRequestRepository;

    @Autowired
    public SupplyRequestServiceImpl(SupplyRequestRepository supplyRequestRepository) {
        this.supplyRequestRepository = supplyRequestRepository;
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

}
