package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.DTO.SupplyOrderDTO;
import IAP.model.SupplyOrder;

import java.util.List;

public interface SupplyOrderService {
    SupplyOrder getSupplyOrder(long id) throws ResourceNotFoundException;
    List<SupplyOrder> listSupplyOrder() throws ResourceNotFoundException;
    List<SupplyOrder> listSupplyOrderBySupplyRequestId(long supplyRequestId) throws ResourceNotFoundException;
    SupplyOrder addOrder(SupplyOrder supplyOrder) throws InvalidDataException, ResourceNotFoundException;
    SupplyOrder updateOrder(long id, SupplyOrder supplyOrder) throws InvalidDataException, ResourceNotFoundException;
    void deleteOrder(long id) throws ResourceNotFoundException;
    SupplyOrder saveFromDTO(SupplyOrderDTO dto);
}
