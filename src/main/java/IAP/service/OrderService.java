package IAP.service;

import IAP.model.Order;
import java.util.List;

public interface OrderService {
    void addOrder(Order order);
    void updateOrder(Order order);
    void deleteOrder(long id);
    List<Order> listOrders();
    Order getOrder(long id);
}
