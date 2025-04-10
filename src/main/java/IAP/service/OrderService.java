package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Order;
import java.util.List;

public interface OrderService {
    Order addOrder(Order order) throws InvalidDataException, ResourceNotFoundException;
    Order updateOrder(long id, Order order) throws InvalidDataException, ResourceNotFoundException;
    void deleteOrder(long id) throws ResourceNotFoundException;
    List<Order> listOrders();
    Order getOrder(long id) throws ResourceNotFoundException;
}