package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.DTO.BranchOrderDTO;
import IAP.model.Order;
import IAP.model.Sale;

import java.util.List;

public interface OrderService {
    Order addOrder(Order order) throws InvalidDataException, ResourceNotFoundException;
    Order updateOrder(long id, Order order) throws InvalidDataException, ResourceNotFoundException;
    void deleteOrder(long id) throws ResourceNotFoundException;
    List<Order> listOrders();
    Order getOrder(long id) throws ResourceNotFoundException;
    List<Order> listOrderBySale(Sale sale);
    List<Order> getOrdersBySale(Sale sale);
    boolean ordersDiffer(List<Order> local, List<BranchOrderDTO> remote);
    Order saveFromDTO(BranchOrderDTO dto, Sale sale);
    void updateOrdersForSale(List<BranchOrderDTO> remoteOrders, Sale sale);
}