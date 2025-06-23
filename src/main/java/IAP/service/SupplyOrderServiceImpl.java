package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Sale;
import IAP.model.SupplyOrder;
import IAP.repository.SupplyOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyOrderServiceImpl implements SupplyOrderService {

    private final SupplyOrderRepository supplyOrderRepository;

    @Autowired
    public SupplyOrderServiceImpl(SupplyOrderRepository supplyOrderRepository) {
        this.supplyOrderRepository = supplyOrderRepository;
    }

    @Override
    public SupplyOrder getSupplyOrder(long id) throws ResourceNotFoundException {
        return supplyOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplyOrder not found  with id: " + id));
    }


    @Override
    public List<SupplyOrder> listSupplyOrder() throws ResourceNotFoundException {
        return  supplyOrderRepository.findAll();
    }

    @Override
    public List<SupplyOrder> listSupplyOrderBySupplyRequestId(long supplyRequestId) throws ResourceNotFoundException {
        return supplyOrderRepository.findAllBySupplyRequestId(supplyRequestId);
    }


    @Override
    public SupplyOrder addOrder(SupplyOrder supplyOrder) throws InvalidDataException, ResourceNotFoundException {
        return supplyOrderRepository.save(supplyOrder);
    }

    @Override
    public SupplyOrder updateOrder(long id, SupplyOrder supplyOrder) throws InvalidDataException, ResourceNotFoundException {
        SupplyOrder existingSO =  supplyOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplyOrder not found with id: " + id));

        return supplyOrderRepository.save(supplyOrder);
    }

    @Override
    public void deleteOrder(long id) throws ResourceNotFoundException {
        SupplyOrder existingSO = supplyOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplyOrder not found with id: " + id));

        supplyOrderRepository.delete(existingSO);
    }
}
